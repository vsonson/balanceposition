package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.MetricHistory;
import com.balpos.app.repository.MetricHistoryRepository;
import com.balpos.app.service.MetricHistoryService;
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
 * Test class for the MetricHistoryResource REST controller.
 *
 * @see MetricHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class MetricHistoryResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_METRIC_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_METRIC_VALUE = "BBBBBBBBBB";

    @Autowired
    private MetricHistoryRepository metricHistoryRepository;

    @Autowired
    private MetricHistoryService metricHistoryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMetricHistoryMockMvc;

    private MetricHistory metricHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MetricHistoryResource metricHistoryResource = new MetricHistoryResource(metricHistoryService);
        this.restMetricHistoryMockMvc = MockMvcBuilders.standaloneSetup(metricHistoryResource)
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
    public static MetricHistory createEntity(EntityManager em) {
        MetricHistory metricHistory = new MetricHistory()
            .date(DEFAULT_DATE)
            .metricValue(DEFAULT_METRIC_VALUE);
        return metricHistory;
    }

    @Before
    public void initTest() {
        metricHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createMetricHistory() throws Exception {
        int databaseSizeBeforeCreate = metricHistoryRepository.findAll().size();

        // Create the MetricHistory
        restMetricHistoryMockMvc.perform(post("/api/metric-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metricHistory)))
            .andExpect(status().isCreated());

        // Validate the MetricHistory in the database
        List<MetricHistory> metricHistoryList = metricHistoryRepository.findAll();
        assertThat(metricHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        MetricHistory testMetricHistory = metricHistoryList.get(metricHistoryList.size() - 1);
        assertThat(testMetricHistory.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testMetricHistory.getMetricValue()).isEqualTo(DEFAULT_METRIC_VALUE);
    }

    @Test
    @Transactional
    public void createMetricHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = metricHistoryRepository.findAll().size();

        // Create the MetricHistory with an existing ID
        metricHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetricHistoryMockMvc.perform(post("/api/metric-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metricHistory)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<MetricHistory> metricHistoryList = metricHistoryRepository.findAll();
        assertThat(metricHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkMetricValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = metricHistoryRepository.findAll().size();
        // set the field null
        metricHistory.setMetricValue(null);

        // Create the MetricHistory, which fails.

        restMetricHistoryMockMvc.perform(post("/api/metric-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metricHistory)))
            .andExpect(status().isBadRequest());

        List<MetricHistory> metricHistoryList = metricHistoryRepository.findAll();
        assertThat(metricHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMetricHistories() throws Exception {
        // Initialize the database
        metricHistoryRepository.saveAndFlush(metricHistory);

        // Get all the metricHistoryList
        restMetricHistoryMockMvc.perform(get("/api/metric-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metricHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].metricValue").value(hasItem(DEFAULT_METRIC_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getMetricHistory() throws Exception {
        // Initialize the database
        metricHistoryRepository.saveAndFlush(metricHistory);

        // Get the metricHistory
        restMetricHistoryMockMvc.perform(get("/api/metric-histories/{id}", metricHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(metricHistory.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.metricValue").value(DEFAULT_METRIC_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMetricHistory() throws Exception {
        // Get the metricHistory
        restMetricHistoryMockMvc.perform(get("/api/metric-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMetricHistory() throws Exception {
        // Initialize the database
        metricHistoryService.save(metricHistory);

        int databaseSizeBeforeUpdate = metricHistoryRepository.findAll().size();

        // Update the metricHistory
        MetricHistory updatedMetricHistory = metricHistoryRepository.findOne(metricHistory.getId());
        updatedMetricHistory
            .date(UPDATED_DATE)
            .metricValue(UPDATED_METRIC_VALUE);

        restMetricHistoryMockMvc.perform(put("/api/metric-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMetricHistory)))
            .andExpect(status().isOk());

        // Validate the MetricHistory in the database
        List<MetricHistory> metricHistoryList = metricHistoryRepository.findAll();
        assertThat(metricHistoryList).hasSize(databaseSizeBeforeUpdate);
        MetricHistory testMetricHistory = metricHistoryList.get(metricHistoryList.size() - 1);
        assertThat(testMetricHistory.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMetricHistory.getMetricValue()).isEqualTo(UPDATED_METRIC_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingMetricHistory() throws Exception {
        int databaseSizeBeforeUpdate = metricHistoryRepository.findAll().size();

        // Create the MetricHistory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMetricHistoryMockMvc.perform(put("/api/metric-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metricHistory)))
            .andExpect(status().isCreated());

        // Validate the MetricHistory in the database
        List<MetricHistory> metricHistoryList = metricHistoryRepository.findAll();
        assertThat(metricHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMetricHistory() throws Exception {
        // Initialize the database
        metricHistoryService.save(metricHistory);

        int databaseSizeBeforeDelete = metricHistoryRepository.findAll().size();

        // Get the metricHistory
        restMetricHistoryMockMvc.perform(delete("/api/metric-histories/{id}", metricHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MetricHistory> metricHistoryList = metricHistoryRepository.findAll();
        assertThat(metricHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetricHistory.class);
        MetricHistory metricHistory1 = new MetricHistory();
        metricHistory1.setId(1L);
        MetricHistory metricHistory2 = new MetricHistory();
        metricHistory2.setId(metricHistory1.getId());
        assertThat(metricHistory1).isEqualTo(metricHistory2);
        metricHistory2.setId(2L);
        assertThat(metricHistory1).isNotEqualTo(metricHistory2);
        metricHistory1.setId(null);
        assertThat(metricHistory1).isNotEqualTo(metricHistory2);
    }
}
