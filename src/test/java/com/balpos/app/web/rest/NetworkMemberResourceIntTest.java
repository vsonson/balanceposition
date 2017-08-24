package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.NetworkMember;
import com.balpos.app.repository.NetworkMemberRepository;
import com.balpos.app.service.NetworkMemberService;
import com.balpos.app.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the NetworkMemberResource REST controller.
 *
 * @see NetworkMemberResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class NetworkMemberResourceIntTest {

    private static final Boolean DEFAULT_IS_DATA_SHARED = false;
    private static final Boolean UPDATED_IS_DATA_SHARED = true;

    private static final Boolean DEFAULT_SEND_ALERTS = false;
    private static final Boolean UPDATED_SEND_ALERTS = true;

    @Autowired
    private NetworkMemberRepository networkMemberRepository;

    @Autowired
    private NetworkMemberService networkMemberService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restNetworkMemberMockMvc;

    private NetworkMember networkMember;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NetworkMemberResource networkMemberResource = new NetworkMemberResource(networkMemberService);
        this.restNetworkMemberMockMvc = MockMvcBuilders.standaloneSetup(networkMemberResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NetworkMember createEntity(EntityManager em) {
        NetworkMember networkMember = new NetworkMember()
            .isDataShared(DEFAULT_IS_DATA_SHARED)
            .sendAlerts(DEFAULT_SEND_ALERTS);
        return networkMember;
    }

    @Before
    public void initTest() {
        networkMember = createEntity(em);
    }

    @Test
    @Transactional
    public void createNetworkMember() throws Exception {
        int databaseSizeBeforeCreate = networkMemberRepository.findAll().size();

        // Create the NetworkMember
        restNetworkMemberMockMvc.perform(post("/api/network-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(networkMember)))
            .andExpect(status().isCreated());

        // Validate the NetworkMember in the database
        List<NetworkMember> networkMemberList = networkMemberRepository.findAll();
        assertThat(networkMemberList).hasSize(databaseSizeBeforeCreate + 1);
        NetworkMember testNetworkMember = networkMemberList.get(networkMemberList.size() - 1);
        assertThat(testNetworkMember.isIsDataShared()).isEqualTo(DEFAULT_IS_DATA_SHARED);
        assertThat(testNetworkMember.isSendAlerts()).isEqualTo(DEFAULT_SEND_ALERTS);
    }

    @Test
    @Transactional
    public void createNetworkMemberWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = networkMemberRepository.findAll().size();

        // Create the NetworkMember with an existing ID
        networkMember.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNetworkMemberMockMvc.perform(post("/api/network-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(networkMember)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<NetworkMember> networkMemberList = networkMemberRepository.findAll();
        assertThat(networkMemberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllNetworkMembers() throws Exception {
        // Initialize the database
        networkMemberRepository.saveAndFlush(networkMember);

        // Get all the networkMemberList
        restNetworkMemberMockMvc.perform(get("/api/network-members?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(networkMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].isDataShared").value(hasItem(DEFAULT_IS_DATA_SHARED.booleanValue())))
            .andExpect(jsonPath("$.[*].sendAlerts").value(hasItem(DEFAULT_SEND_ALERTS.booleanValue())));
    }

    @Test
    @Transactional
    public void getNetworkMember() throws Exception {
        // Initialize the database
        networkMemberRepository.saveAndFlush(networkMember);

        // Get the networkMember
        restNetworkMemberMockMvc.perform(get("/api/network-members/{id}", networkMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(networkMember.getId().intValue()))
            .andExpect(jsonPath("$.isDataShared").value(DEFAULT_IS_DATA_SHARED.booleanValue()))
            .andExpect(jsonPath("$.sendAlerts").value(DEFAULT_SEND_ALERTS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingNetworkMember() throws Exception {
        // Get the networkMember
        restNetworkMemberMockMvc.perform(get("/api/network-members/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNetworkMember() throws Exception {
        // Initialize the database
        networkMemberService.save(networkMember);

        int databaseSizeBeforeUpdate = networkMemberRepository.findAll().size();

        // Update the networkMember
        NetworkMember updatedNetworkMember = networkMemberRepository.findOne(networkMember.getId());
        updatedNetworkMember
            .isDataShared(UPDATED_IS_DATA_SHARED)
            .sendAlerts(UPDATED_SEND_ALERTS);

        restNetworkMemberMockMvc.perform(put("/api/network-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNetworkMember)))
            .andExpect(status().isOk());

        // Validate the NetworkMember in the database
        List<NetworkMember> networkMemberList = networkMemberRepository.findAll();
        assertThat(networkMemberList).hasSize(databaseSizeBeforeUpdate);
        NetworkMember testNetworkMember = networkMemberList.get(networkMemberList.size() - 1);
        assertThat(testNetworkMember.isIsDataShared()).isEqualTo(UPDATED_IS_DATA_SHARED);
        assertThat(testNetworkMember.isSendAlerts()).isEqualTo(UPDATED_SEND_ALERTS);
    }

    @Test
    @Transactional
    public void updateNonExistingNetworkMember() throws Exception {
        int databaseSizeBeforeUpdate = networkMemberRepository.findAll().size();

        // Create the NetworkMember

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restNetworkMemberMockMvc.perform(put("/api/network-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(networkMember)))
            .andExpect(status().isCreated());

        // Validate the NetworkMember in the database
        List<NetworkMember> networkMemberList = networkMemberRepository.findAll();
        assertThat(networkMemberList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteNetworkMember() throws Exception {
        // Initialize the database
        networkMemberService.save(networkMember);

        int databaseSizeBeforeDelete = networkMemberRepository.findAll().size();

        // Get the networkMember
        restNetworkMemberMockMvc.perform(delete("/api/network-members/{id}", networkMember.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<NetworkMember> networkMemberList = networkMemberRepository.findAll();
        assertThat(networkMemberList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NetworkMember.class);
        NetworkMember networkMember1 = new NetworkMember();
        networkMember1.setId(1L);
        NetworkMember networkMember2 = new NetworkMember();
        networkMember2.setId(networkMember1.getId());
        assertThat(networkMember1).isEqualTo(networkMember2);
        networkMember2.setId(2L);
        assertThat(networkMember1).isNotEqualTo(networkMember2);
        networkMember1.setId(null);
        assertThat(networkMember1).isNotEqualTo(networkMember2);
    }
}
