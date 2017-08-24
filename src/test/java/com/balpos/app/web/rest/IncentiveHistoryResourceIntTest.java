package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.IncentiveHistory;
import com.balpos.app.repository.IncentiveHistoryRepository;
import com.balpos.app.service.IncentiveHistoryService;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the IncentiveHistoryResource REST controller.
 *
 * @see IncentiveHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class IncentiveHistoryResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;

    @Autowired
    private IncentiveHistoryRepository incentiveHistoryRepository;

    @Autowired
    private IncentiveHistoryService incentiveHistoryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIncentiveHistoryMockMvc;

    private IncentiveHistory incentiveHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IncentiveHistoryResource incentiveHistoryResource = new IncentiveHistoryResource(incentiveHistoryService);
        this.restIncentiveHistoryMockMvc = MockMvcBuilders.standaloneSetup(incentiveHistoryResource)
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
    public static IncentiveHistory createEntity(EntityManager em) {
        IncentiveHistory incentiveHistory = new IncentiveHistory()
            .date(DEFAULT_DATE)
            .points(DEFAULT_POINTS);
        return incentiveHistory;
    }

    @Before
    public void initTest() {
        incentiveHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createIncentiveHistory() throws Exception {
        int databaseSizeBeforeCreate = incentiveHistoryRepository.findAll().size();

        // Create the IncentiveHistory
        restIncentiveHistoryMockMvc.perform(post("/api/incentive-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(incentiveHistory)))
            .andExpect(status().isCreated());

        // Validate the IncentiveHistory in the database
        List<IncentiveHistory> incentiveHistoryList = incentiveHistoryRepository.findAll();
        assertThat(incentiveHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        IncentiveHistory testIncentiveHistory = incentiveHistoryList.get(incentiveHistoryList.size() - 1);
        assertThat(testIncentiveHistory.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testIncentiveHistory.getPoints()).isEqualTo(DEFAULT_POINTS);
    }

    @Test
    @Transactional
    public void createIncentiveHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = incentiveHistoryRepository.findAll().size();

        // Create the IncentiveHistory with an existing ID
        incentiveHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIncentiveHistoryMockMvc.perform(post("/api/incentive-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(incentiveHistory)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<IncentiveHistory> incentiveHistoryList = incentiveHistoryRepository.findAll();
        assertThat(incentiveHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllIncentiveHistories() throws Exception {
        // Initialize the database
        incentiveHistoryRepository.saveAndFlush(incentiveHistory);

        // Get all the incentiveHistoryList
        restIncentiveHistoryMockMvc.perform(get("/api/incentive-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(incentiveHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)));
    }

    @Test
    @Transactional
    public void getIncentiveHistory() throws Exception {
        // Initialize the database
        incentiveHistoryRepository.saveAndFlush(incentiveHistory);

        // Get the incentiveHistory
        restIncentiveHistoryMockMvc.perform(get("/api/incentive-histories/{id}", incentiveHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(incentiveHistory.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS));
    }

    @Test
    @Transactional
    public void getNonExistingIncentiveHistory() throws Exception {
        // Get the incentiveHistory
        restIncentiveHistoryMockMvc.perform(get("/api/incentive-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIncentiveHistory() throws Exception {
        // Initialize the database
        incentiveHistoryService.save(incentiveHistory);

        int databaseSizeBeforeUpdate = incentiveHistoryRepository.findAll().size();

        // Update the incentiveHistory
        IncentiveHistory updatedIncentiveHistory = incentiveHistoryRepository.findOne(incentiveHistory.getId());
        updatedIncentiveHistory
            .date(UPDATED_DATE)
            .points(UPDATED_POINTS);

        restIncentiveHistoryMockMvc.perform(put("/api/incentive-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIncentiveHistory)))
            .andExpect(status().isOk());

        // Validate the IncentiveHistory in the database
        List<IncentiveHistory> incentiveHistoryList = incentiveHistoryRepository.findAll();
        assertThat(incentiveHistoryList).hasSize(databaseSizeBeforeUpdate);
        IncentiveHistory testIncentiveHistory = incentiveHistoryList.get(incentiveHistoryList.size() - 1);
        assertThat(testIncentiveHistory.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testIncentiveHistory.getPoints()).isEqualTo(UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void updateNonExistingIncentiveHistory() throws Exception {
        int databaseSizeBeforeUpdate = incentiveHistoryRepository.findAll().size();

        // Create the IncentiveHistory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restIncentiveHistoryMockMvc.perform(put("/api/incentive-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(incentiveHistory)))
            .andExpect(status().isCreated());

        // Validate the IncentiveHistory in the database
        List<IncentiveHistory> incentiveHistoryList = incentiveHistoryRepository.findAll();
        assertThat(incentiveHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteIncentiveHistory() throws Exception {
        // Initialize the database
        incentiveHistoryService.save(incentiveHistory);

        int databaseSizeBeforeDelete = incentiveHistoryRepository.findAll().size();

        // Get the incentiveHistory
        restIncentiveHistoryMockMvc.perform(delete("/api/incentive-histories/{id}", incentiveHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<IncentiveHistory> incentiveHistoryList = incentiveHistoryRepository.findAll();
        assertThat(incentiveHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IncentiveHistory.class);
        IncentiveHistory incentiveHistory1 = new IncentiveHistory();
        incentiveHistory1.setId(1L);
        IncentiveHistory incentiveHistory2 = new IncentiveHistory();
        incentiveHistory2.setId(incentiveHistory1.getId());
        assertThat(incentiveHistory1).isEqualTo(incentiveHistory2);
        incentiveHistory2.setId(2L);
        assertThat(incentiveHistory1).isNotEqualTo(incentiveHistory2);
        incentiveHistory1.setId(null);
        assertThat(incentiveHistory1).isNotEqualTo(incentiveHistory2);
    }
}
