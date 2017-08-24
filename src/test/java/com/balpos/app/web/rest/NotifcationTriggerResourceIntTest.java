package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.NotifcationTrigger;
import com.balpos.app.repository.NotifcationTriggerRepository;
import com.balpos.app.service.NotifcationTriggerService;
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
 * Test class for the NotifcationTriggerResource REST controller.
 *
 * @see NotifcationTriggerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class NotifcationTriggerResourceIntTest {

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    @Autowired
    private NotifcationTriggerRepository notifcationTriggerRepository;

    @Autowired
    private NotifcationTriggerService notifcationTriggerService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restNotifcationTriggerMockMvc;

    private NotifcationTrigger notifcationTrigger;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NotifcationTriggerResource notifcationTriggerResource = new NotifcationTriggerResource(notifcationTriggerService);
        this.restNotifcationTriggerMockMvc = MockMvcBuilders.standaloneSetup(notifcationTriggerResource)
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
    public static NotifcationTrigger createEntity(EntityManager em) {
        NotifcationTrigger notifcationTrigger = new NotifcationTrigger()
            .desc(DEFAULT_DESC);
        return notifcationTrigger;
    }

    @Before
    public void initTest() {
        notifcationTrigger = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotifcationTrigger() throws Exception {
        int databaseSizeBeforeCreate = notifcationTriggerRepository.findAll().size();

        // Create the NotifcationTrigger
        restNotifcationTriggerMockMvc.perform(post("/api/notifcation-triggers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notifcationTrigger)))
            .andExpect(status().isCreated());

        // Validate the NotifcationTrigger in the database
        List<NotifcationTrigger> notifcationTriggerList = notifcationTriggerRepository.findAll();
        assertThat(notifcationTriggerList).hasSize(databaseSizeBeforeCreate + 1);
        NotifcationTrigger testNotifcationTrigger = notifcationTriggerList.get(notifcationTriggerList.size() - 1);
        assertThat(testNotifcationTrigger.getDesc()).isEqualTo(DEFAULT_DESC);
    }

    @Test
    @Transactional
    public void createNotifcationTriggerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notifcationTriggerRepository.findAll().size();

        // Create the NotifcationTrigger with an existing ID
        notifcationTrigger.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotifcationTriggerMockMvc.perform(post("/api/notifcation-triggers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notifcationTrigger)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<NotifcationTrigger> notifcationTriggerList = notifcationTriggerRepository.findAll();
        assertThat(notifcationTriggerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllNotifcationTriggers() throws Exception {
        // Initialize the database
        notifcationTriggerRepository.saveAndFlush(notifcationTrigger);

        // Get all the notifcationTriggerList
        restNotifcationTriggerMockMvc.perform(get("/api/notifcation-triggers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notifcationTrigger.getId().intValue())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())));
    }

    @Test
    @Transactional
    public void getNotifcationTrigger() throws Exception {
        // Initialize the database
        notifcationTriggerRepository.saveAndFlush(notifcationTrigger);

        // Get the notifcationTrigger
        restNotifcationTriggerMockMvc.perform(get("/api/notifcation-triggers/{id}", notifcationTrigger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(notifcationTrigger.getId().intValue()))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNotifcationTrigger() throws Exception {
        // Get the notifcationTrigger
        restNotifcationTriggerMockMvc.perform(get("/api/notifcation-triggers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotifcationTrigger() throws Exception {
        // Initialize the database
        notifcationTriggerService.save(notifcationTrigger);

        int databaseSizeBeforeUpdate = notifcationTriggerRepository.findAll().size();

        // Update the notifcationTrigger
        NotifcationTrigger updatedNotifcationTrigger = notifcationTriggerRepository.findOne(notifcationTrigger.getId());
        updatedNotifcationTrigger
            .desc(UPDATED_DESC);

        restNotifcationTriggerMockMvc.perform(put("/api/notifcation-triggers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNotifcationTrigger)))
            .andExpect(status().isOk());

        // Validate the NotifcationTrigger in the database
        List<NotifcationTrigger> notifcationTriggerList = notifcationTriggerRepository.findAll();
        assertThat(notifcationTriggerList).hasSize(databaseSizeBeforeUpdate);
        NotifcationTrigger testNotifcationTrigger = notifcationTriggerList.get(notifcationTriggerList.size() - 1);
        assertThat(testNotifcationTrigger.getDesc()).isEqualTo(UPDATED_DESC);
    }

    @Test
    @Transactional
    public void updateNonExistingNotifcationTrigger() throws Exception {
        int databaseSizeBeforeUpdate = notifcationTriggerRepository.findAll().size();

        // Create the NotifcationTrigger

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restNotifcationTriggerMockMvc.perform(put("/api/notifcation-triggers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notifcationTrigger)))
            .andExpect(status().isCreated());

        // Validate the NotifcationTrigger in the database
        List<NotifcationTrigger> notifcationTriggerList = notifcationTriggerRepository.findAll();
        assertThat(notifcationTriggerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteNotifcationTrigger() throws Exception {
        // Initialize the database
        notifcationTriggerService.save(notifcationTrigger);

        int databaseSizeBeforeDelete = notifcationTriggerRepository.findAll().size();

        // Get the notifcationTrigger
        restNotifcationTriggerMockMvc.perform(delete("/api/notifcation-triggers/{id}", notifcationTrigger.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<NotifcationTrigger> notifcationTriggerList = notifcationTriggerRepository.findAll();
        assertThat(notifcationTriggerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotifcationTrigger.class);
        NotifcationTrigger notifcationTrigger1 = new NotifcationTrigger();
        notifcationTrigger1.setId(1L);
        NotifcationTrigger notifcationTrigger2 = new NotifcationTrigger();
        notifcationTrigger2.setId(notifcationTrigger1.getId());
        assertThat(notifcationTrigger1).isEqualTo(notifcationTrigger2);
        notifcationTrigger2.setId(2L);
        assertThat(notifcationTrigger1).isNotEqualTo(notifcationTrigger2);
        notifcationTrigger1.setId(null);
        assertThat(notifcationTrigger1).isNotEqualTo(notifcationTrigger2);
    }
}
