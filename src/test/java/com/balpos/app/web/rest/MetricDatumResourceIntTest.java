package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;
import com.balpos.app.domain.DataPoint;
import com.balpos.app.domain.MetricDatum;
import com.balpos.app.domain.User;
import com.balpos.app.repository.MetricDatumRepository;
import com.balpos.app.service.MetricDatumQueryService;
import com.balpos.app.service.MetricDatumService;
import com.balpos.app.service.dto.MetricDatumDTO;
import com.balpos.app.service.mapper.MetricDatumMapper;
import com.balpos.app.service.util.UserResourceUtil;
import com.balpos.app.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Ignore;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MetricDatumResource REST controller.
 *
 * @see MetricDatumResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
@Ignore
public class MetricDatumResourceIntTest {

    private static final String DEFAULT_METRIC_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_METRIC_VALUE = "BBBBBBBBBB";

    private static final LocalDateTime DEFAULT_TIMESTAMP = LocalDateTime.now();
    private static final LocalDateTime UPDATED_TIMESTAMP = LocalDateTime.now().plusDays(1);

    @Autowired
    private UserResourceUtil userResourceUtil;

    @Autowired
    private MetricDatumRepository metricDatumRepository;

    @Autowired
    private MetricDatumMapper metricDatumMapper;

    @Autowired
    private MetricDatumService metricDatumService;

    @Autowired
    private MetricDatumQueryService metricDatumQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMetricDatumMockMvc;

    private MetricDatum metricDatum;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MetricDatum createEntity(EntityManager em) {
        MetricDatum metricDatum = new MetricDatum()
            .setDatumValue(DEFAULT_METRIC_VALUE)
            .setTimestamp(DEFAULT_TIMESTAMP);
        // Add required entity
        DataPoint dataPoint = DataPointResourceIntTest.createEntity(em);
        em.persist(dataPoint);
        em.flush();
        metricDatum.setDataPoint(dataPoint);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        metricDatum.setUser(user);
        return metricDatum;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MetricDatumResource metricDatumResource = new MetricDatumResource(metricDatumService, metricDatumQueryService, userResourceUtil);
        this.restMetricDatumMockMvc = MockMvcBuilders.standaloneSetup(metricDatumResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        metricDatum = createEntity(em);
    }

    @Test
    @Transactional
    public void createMetricDatum() throws Exception {
        int databaseSizeBeforeCreate = metricDatumRepository.findAll().size();

        // Create the MetricDatum
        MetricDatumDTO metricDatumDTO = metricDatumMapper.toDto(metricDatum);
        restMetricDatumMockMvc.perform(post("/api/metric-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metricDatumDTO)))
            .andExpect(status().isCreated());

        // Validate the MetricDatum in the database
        List<MetricDatum> metricDatumList = metricDatumRepository.findAll();
        assertThat(metricDatumList).hasSize(databaseSizeBeforeCreate + 1);
        MetricDatum testMetricDatum = metricDatumList.get(metricDatumList.size() - 1);
        assertThat(testMetricDatum.getDatumValue()).isEqualTo(DEFAULT_METRIC_VALUE);
        assertThat(testMetricDatum.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
    }

    @Test
    @Transactional
    public void createMetricDatumWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = metricDatumRepository.findAll().size();

        // Create the MetricDatum with an existing ID
        metricDatum.setId(1L);
        MetricDatumDTO metricDatumDTO = metricDatumMapper.toDto(metricDatum);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetricDatumMockMvc.perform(post("/api/metric-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metricDatumDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<MetricDatum> metricDatumList = metricDatumRepository.findAll();
        assertThat(metricDatumList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkMetricValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = metricDatumRepository.findAll().size();
        // set the field null
        metricDatum.setDatumValue(null);

        // Create the MetricDatum, which fails.
        MetricDatumDTO metricDatumDTO = metricDatumMapper.toDto(metricDatum);

        restMetricDatumMockMvc.perform(post("/api/metric-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metricDatumDTO)))
            .andExpect(status().isBadRequest());

        List<MetricDatum> metricDatumList = metricDatumRepository.findAll();
        assertThat(metricDatumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = metricDatumRepository.findAll().size();
        // set the field null
        metricDatum.setTimestamp(null);

        // Create the MetricDatum, which fails.
        MetricDatumDTO metricDatumDTO = metricDatumMapper.toDto(metricDatum);

        restMetricDatumMockMvc.perform(post("/api/metric-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metricDatumDTO)))
            .andExpect(status().isBadRequest());

        List<MetricDatum> metricDatumList = metricDatumRepository.findAll();
        assertThat(metricDatumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMetricData() throws Exception {
        // Initialize the database
        metricDatumRepository.saveAndFlush(metricDatum);

        // Get all the metricDatumList
        restMetricDatumMockMvc.perform(get("/api/metric-data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metricDatum.getId().intValue())))
            .andExpect(jsonPath("$.[*].metricValue").value(hasItem(DEFAULT_METRIC_VALUE)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP)));
    }

    @Test
    @Transactional
    public void getMetricDatum() throws Exception {
        // Initialize the database
        metricDatumRepository.saveAndFlush(metricDatum);

        // Get the metricDatum
        restMetricDatumMockMvc.perform(get("/api/metric-data/{id}", metricDatum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(metricDatum.getId().intValue()))
            .andExpect(jsonPath("$.metricValue").value(DEFAULT_METRIC_VALUE))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP));
    }

    @Test
    @Transactional
    public void getAllMetricDataByMetricValueIsEqualToSomething() throws Exception {
        // Initialize the database
        metricDatumRepository.saveAndFlush(metricDatum);

        // Get all the metricDatumList where metricValue equals to DEFAULT_METRIC_VALUE
        defaultMetricDatumShouldBeFound("metricValue.equals=" + DEFAULT_METRIC_VALUE);

        // Get all the metricDatumList where metricValue equals to UPDATED_METRIC_VALUE
        defaultMetricDatumShouldNotBeFound("metricValue.equals=" + UPDATED_METRIC_VALUE);
    }

    @Test
    @Transactional
    public void getAllMetricDataByMetricValueIsInShouldWork() throws Exception {
        // Initialize the database
        metricDatumRepository.saveAndFlush(metricDatum);

        // Get all the metricDatumList where metricValue in DEFAULT_METRIC_VALUE or UPDATED_METRIC_VALUE
        defaultMetricDatumShouldBeFound("metricValue.in=" + DEFAULT_METRIC_VALUE + "," + UPDATED_METRIC_VALUE);

        // Get all the metricDatumList where metricValue equals to UPDATED_METRIC_VALUE
        defaultMetricDatumShouldNotBeFound("metricValue.in=" + UPDATED_METRIC_VALUE);
    }

    @Test
    @Transactional
    public void getAllMetricDataByMetricValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        metricDatumRepository.saveAndFlush(metricDatum);

        // Get all the metricDatumList where metricValue is not null
        defaultMetricDatumShouldBeFound("metricValue.specified=true");

        // Get all the metricDatumList where metricValue is null
        defaultMetricDatumShouldNotBeFound("metricValue.specified=false");
    }

    @Test
    @Transactional
    public void getAllMetricDataByTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        metricDatumRepository.saveAndFlush(metricDatum);

        // Get all the metricDatumList where timestamp equals to DEFAULT_TIMESTAMP
        defaultMetricDatumShouldBeFound("timestamp.equals=" + DEFAULT_TIMESTAMP);

        // Get all the metricDatumList where timestamp equals to UPDATED_TIMESTAMP
        defaultMetricDatumShouldNotBeFound("timestamp.equals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllMetricDataByTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        metricDatumRepository.saveAndFlush(metricDatum);

        // Get all the metricDatumList where timestamp in DEFAULT_TIMESTAMP or UPDATED_TIMESTAMP
        defaultMetricDatumShouldBeFound("timestamp.in=" + DEFAULT_TIMESTAMP + "," + UPDATED_TIMESTAMP);

        // Get all the metricDatumList where timestamp equals to UPDATED_TIMESTAMP
        defaultMetricDatumShouldNotBeFound("timestamp.in=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllMetricDataByTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        metricDatumRepository.saveAndFlush(metricDatum);

        // Get all the metricDatumList where timestamp is not null
        defaultMetricDatumShouldBeFound("timestamp.specified=true");

        // Get all the metricDatumList where timestamp is null
        defaultMetricDatumShouldNotBeFound("timestamp.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMetricDatumShouldBeFound(String filter) throws Exception {
        restMetricDatumMockMvc.perform(get("/api/metric-data?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metricDatum.getId().intValue())))
            .andExpect(jsonPath("$.[*].metricValue").value(hasItem(DEFAULT_METRIC_VALUE)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMetricDatumShouldNotBeFound(String filter) throws Exception {
        restMetricDatumMockMvc.perform(get("/api/metric-data?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingMetricDatum() throws Exception {
        // Get the metricDatum
        restMetricDatumMockMvc.perform(get("/api/metric-data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMetricDatum() throws Exception {
        // Initialize the database
        metricDatumRepository.saveAndFlush(metricDatum);
        int databaseSizeBeforeUpdate = metricDatumRepository.findAll().size();

        // Update the metricDatum
        MetricDatum updatedMetricDatum = metricDatumRepository.findOne(metricDatum.getId());
        updatedMetricDatum
            .setDatumValue(UPDATED_METRIC_VALUE)
            .setTimestamp(UPDATED_TIMESTAMP);
        MetricDatumDTO metricDatumDTO = metricDatumMapper.toDto(updatedMetricDatum);

        restMetricDatumMockMvc.perform(put("/api/metric-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metricDatumDTO)))
            .andExpect(status().isOk());

        // Validate the MetricDatum in the database
        List<MetricDatum> metricDatumList = metricDatumRepository.findAll();
        assertThat(metricDatumList).hasSize(databaseSizeBeforeUpdate);
        MetricDatum testMetricDatum = metricDatumList.get(metricDatumList.size() - 1);
        assertThat(testMetricDatum.getDatumValue()).isEqualTo(UPDATED_METRIC_VALUE);
        assertThat(testMetricDatum.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void updateNonExistingMetricDatum() throws Exception {
        int databaseSizeBeforeUpdate = metricDatumRepository.findAll().size();

        // Create the MetricDatum
        MetricDatumDTO metricDatumDTO = metricDatumMapper.toDto(metricDatum);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMetricDatumMockMvc.perform(put("/api/metric-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metricDatumDTO)))
            .andExpect(status().isCreated());

        // Validate the MetricDatum in the database
        List<MetricDatum> metricDatumList = metricDatumRepository.findAll();
        assertThat(metricDatumList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMetricDatum() throws Exception {
        // Initialize the database
        metricDatumRepository.saveAndFlush(metricDatum);
        int databaseSizeBeforeDelete = metricDatumRepository.findAll().size();

        // Get the metricDatum
        restMetricDatumMockMvc.perform(delete("/api/metric-data/{id}", metricDatum.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MetricDatum> metricDatumList = metricDatumRepository.findAll();
        assertThat(metricDatumList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        MetricDatum metricDatum1 = new MetricDatum();
        metricDatum1.setId(1L);
        MetricDatum metricDatum2 = new MetricDatum();
        metricDatum2.setId(metricDatum1.getId());
        assertThat(metricDatum1).isEqualTo(metricDatum2);
        metricDatum2.setId(2L);
        assertThat(metricDatum1).isNotEqualTo(metricDatum2);
        metricDatum1.setId(null);
        assertThat(metricDatum1).isNotEqualTo(metricDatum2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(metricDatumMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(metricDatumMapper.fromId(null)).isNull();
    }
}
