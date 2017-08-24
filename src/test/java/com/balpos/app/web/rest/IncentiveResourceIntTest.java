package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.Incentive;
import com.balpos.app.repository.IncentiveRepository;
import com.balpos.app.service.IncentiveService;
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
 * Test class for the IncentiveResource REST controller.
 *
 * @see IncentiveResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class IncentiveResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    private static final Integer DEFAULT_POINTVALUE = 1;
    private static final Integer UPDATED_POINTVALUE = 2;

    @Autowired
    private IncentiveRepository incentiveRepository;

    @Autowired
    private IncentiveService incentiveService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIncentiveMockMvc;

    private Incentive incentive;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IncentiveResource incentiveResource = new IncentiveResource(incentiveService);
        this.restIncentiveMockMvc = MockMvcBuilders.standaloneSetup(incentiveResource)
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
    public static Incentive createEntity(EntityManager em) {
        Incentive incentive = new Incentive()
            .name(DEFAULT_NAME)
            .desc(DEFAULT_DESC)
            .pointvalue(DEFAULT_POINTVALUE);
        return incentive;
    }

    @Before
    public void initTest() {
        incentive = createEntity(em);
    }

    @Test
    @Transactional
    public void createIncentive() throws Exception {
        int databaseSizeBeforeCreate = incentiveRepository.findAll().size();

        // Create the Incentive
        restIncentiveMockMvc.perform(post("/api/incentives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(incentive)))
            .andExpect(status().isCreated());

        // Validate the Incentive in the database
        List<Incentive> incentiveList = incentiveRepository.findAll();
        assertThat(incentiveList).hasSize(databaseSizeBeforeCreate + 1);
        Incentive testIncentive = incentiveList.get(incentiveList.size() - 1);
        assertThat(testIncentive.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testIncentive.getDesc()).isEqualTo(DEFAULT_DESC);
        assertThat(testIncentive.getPointvalue()).isEqualTo(DEFAULT_POINTVALUE);
    }

    @Test
    @Transactional
    public void createIncentiveWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = incentiveRepository.findAll().size();

        // Create the Incentive with an existing ID
        incentive.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIncentiveMockMvc.perform(post("/api/incentives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(incentive)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Incentive> incentiveList = incentiveRepository.findAll();
        assertThat(incentiveList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = incentiveRepository.findAll().size();
        // set the field null
        incentive.setName(null);

        // Create the Incentive, which fails.

        restIncentiveMockMvc.perform(post("/api/incentives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(incentive)))
            .andExpect(status().isBadRequest());

        List<Incentive> incentiveList = incentiveRepository.findAll();
        assertThat(incentiveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIncentives() throws Exception {
        // Initialize the database
        incentiveRepository.saveAndFlush(incentive);

        // Get all the incentiveList
        restIncentiveMockMvc.perform(get("/api/incentives?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(incentive.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())))
            .andExpect(jsonPath("$.[*].pointvalue").value(hasItem(DEFAULT_POINTVALUE)));
    }

    @Test
    @Transactional
    public void getIncentive() throws Exception {
        // Initialize the database
        incentiveRepository.saveAndFlush(incentive);

        // Get the incentive
        restIncentiveMockMvc.perform(get("/api/incentives/{id}", incentive.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(incentive.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC.toString()))
            .andExpect(jsonPath("$.pointvalue").value(DEFAULT_POINTVALUE));
    }

    @Test
    @Transactional
    public void getNonExistingIncentive() throws Exception {
        // Get the incentive
        restIncentiveMockMvc.perform(get("/api/incentives/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIncentive() throws Exception {
        // Initialize the database
        incentiveService.save(incentive);

        int databaseSizeBeforeUpdate = incentiveRepository.findAll().size();

        // Update the incentive
        Incentive updatedIncentive = incentiveRepository.findOne(incentive.getId());
        updatedIncentive
            .name(UPDATED_NAME)
            .desc(UPDATED_DESC)
            .pointvalue(UPDATED_POINTVALUE);

        restIncentiveMockMvc.perform(put("/api/incentives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIncentive)))
            .andExpect(status().isOk());

        // Validate the Incentive in the database
        List<Incentive> incentiveList = incentiveRepository.findAll();
        assertThat(incentiveList).hasSize(databaseSizeBeforeUpdate);
        Incentive testIncentive = incentiveList.get(incentiveList.size() - 1);
        assertThat(testIncentive.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testIncentive.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testIncentive.getPointvalue()).isEqualTo(UPDATED_POINTVALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingIncentive() throws Exception {
        int databaseSizeBeforeUpdate = incentiveRepository.findAll().size();

        // Create the Incentive

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restIncentiveMockMvc.perform(put("/api/incentives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(incentive)))
            .andExpect(status().isCreated());

        // Validate the Incentive in the database
        List<Incentive> incentiveList = incentiveRepository.findAll();
        assertThat(incentiveList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteIncentive() throws Exception {
        // Initialize the database
        incentiveService.save(incentive);

        int databaseSizeBeforeDelete = incentiveRepository.findAll().size();

        // Get the incentive
        restIncentiveMockMvc.perform(delete("/api/incentives/{id}", incentive.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Incentive> incentiveList = incentiveRepository.findAll();
        assertThat(incentiveList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Incentive.class);
        Incentive incentive1 = new Incentive();
        incentive1.setId(1L);
        Incentive incentive2 = new Incentive();
        incentive2.setId(incentive1.getId());
        assertThat(incentive1).isEqualTo(incentive2);
        incentive2.setId(2L);
        assertThat(incentive1).isNotEqualTo(incentive2);
        incentive1.setId(null);
        assertThat(incentive1).isNotEqualTo(incentive2);
    }
}
