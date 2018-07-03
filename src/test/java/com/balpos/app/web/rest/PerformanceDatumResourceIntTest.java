package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.PerformanceDatum;
import com.balpos.app.domain.User;
import com.balpos.app.repository.PerformanceDatumRepository;
import com.balpos.app.service.PerformanceDatumService;
import com.balpos.app.service.dto.PerformanceDatumDTO;
import com.balpos.app.service.mapper.PerformanceDatumMapper;
import com.balpos.app.web.rest.errors.ExceptionTranslator;
import com.balpos.app.service.dto.PerformanceDatumCriteria;
import com.balpos.app.service.PerformanceDatumQueryService;

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
 * Test class for the PerformanceDatumResource REST controller.
 *
 * @see PerformanceDatumResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class PerformanceDatumResourceIntTest {

    private static final String DEFAULT_FEEL = "AAAAAAAAAA";
    private static final String UPDATED_FEEL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_TIMESTAMP = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TIMESTAMP = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private PerformanceDatumRepository performanceDatumRepository;

    @Autowired
    private PerformanceDatumMapper performanceDatumMapper;

    @Autowired
    private PerformanceDatumService performanceDatumService;

    @Autowired
    private PerformanceDatumQueryService performanceDatumQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPerformanceDatumMockMvc;

    private PerformanceDatum performanceDatum;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PerformanceDatumResource performanceDatumResource = new PerformanceDatumResource(performanceDatumService, performanceDatumQueryService);
        this.restPerformanceDatumMockMvc = MockMvcBuilders.standaloneSetup(performanceDatumResource)
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
    public static PerformanceDatum createEntity(EntityManager em) {
        PerformanceDatum performanceDatum = new PerformanceDatum()
            .feel(DEFAULT_FEEL)
            .timestamp(DEFAULT_TIMESTAMP);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        performanceDatum.setUser(user);
        return performanceDatum;
    }

    @Before
    public void initTest() {
        performanceDatum = createEntity(em);
    }

    @Test
    @Transactional
    public void createPerformanceDatum() throws Exception {
        int databaseSizeBeforeCreate = performanceDatumRepository.findAll().size();

        // Create the PerformanceDatum
        PerformanceDatumDTO performanceDatumDTO = performanceDatumMapper.toDto(performanceDatum);
        restPerformanceDatumMockMvc.perform(post("/api/performance-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(performanceDatumDTO)))
            .andExpect(status().isCreated());

        // Validate the PerformanceDatum in the database
        List<PerformanceDatum> performanceDatumList = performanceDatumRepository.findAll();
        assertThat(performanceDatumList).hasSize(databaseSizeBeforeCreate + 1);
        PerformanceDatum testPerformanceDatum = performanceDatumList.get(performanceDatumList.size() - 1);
        assertThat(testPerformanceDatum.getFeel()).isEqualTo(DEFAULT_FEEL);
        assertThat(testPerformanceDatum.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
    }

    @Test
    @Transactional
    public void createPerformanceDatumWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = performanceDatumRepository.findAll().size();

        // Create the PerformanceDatum with an existing ID
        performanceDatum.setId(1L);
        PerformanceDatumDTO performanceDatumDTO = performanceDatumMapper.toDto(performanceDatum);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPerformanceDatumMockMvc.perform(post("/api/performance-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(performanceDatumDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PerformanceDatum> performanceDatumList = performanceDatumRepository.findAll();
        assertThat(performanceDatumList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFeelIsRequired() throws Exception {
        int databaseSizeBeforeTest = performanceDatumRepository.findAll().size();
        // set the field null
        performanceDatum.setFeel(null);

        // Create the PerformanceDatum, which fails.
        PerformanceDatumDTO performanceDatumDTO = performanceDatumMapper.toDto(performanceDatum);

        restPerformanceDatumMockMvc.perform(post("/api/performance-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(performanceDatumDTO)))
            .andExpect(status().isBadRequest());

        List<PerformanceDatum> performanceDatumList = performanceDatumRepository.findAll();
        assertThat(performanceDatumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = performanceDatumRepository.findAll().size();
        // set the field null
        performanceDatum.setTimestamp(null);

        // Create the PerformanceDatum, which fails.
        PerformanceDatumDTO performanceDatumDTO = performanceDatumMapper.toDto(performanceDatum);

        restPerformanceDatumMockMvc.perform(post("/api/performance-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(performanceDatumDTO)))
            .andExpect(status().isBadRequest());

        List<PerformanceDatum> performanceDatumList = performanceDatumRepository.findAll();
        assertThat(performanceDatumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPerformanceData() throws Exception {
        // Initialize the database
        performanceDatumRepository.saveAndFlush(performanceDatum);

        // Get all the performanceDatumList
        restPerformanceDatumMockMvc.perform(get("/api/performance-data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(performanceDatum.getId().intValue())))
            .andExpect(jsonPath("$.[*].feel").value(hasItem(DEFAULT_FEEL.toString())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())));
    }

    @Test
    @Transactional
    public void getPerformanceDatum() throws Exception {
        // Initialize the database
        performanceDatumRepository.saveAndFlush(performanceDatum);

        // Get the performanceDatum
        restPerformanceDatumMockMvc.perform(get("/api/performance-data/{id}", performanceDatum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(performanceDatum.getId().intValue()))
            .andExpect(jsonPath("$.feel").value(DEFAULT_FEEL.toString()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()));
    }

    @Test
    @Transactional
    public void getAllPerformanceDataByFeelIsEqualToSomething() throws Exception {
        // Initialize the database
        performanceDatumRepository.saveAndFlush(performanceDatum);

        // Get all the performanceDatumList where feel equals to DEFAULT_FEEL
        defaultPerformanceDatumShouldBeFound("feel.equals=" + DEFAULT_FEEL);

        // Get all the performanceDatumList where feel equals to UPDATED_FEEL
        defaultPerformanceDatumShouldNotBeFound("feel.equals=" + UPDATED_FEEL);
    }

    @Test
    @Transactional
    public void getAllPerformanceDataByFeelIsInShouldWork() throws Exception {
        // Initialize the database
        performanceDatumRepository.saveAndFlush(performanceDatum);

        // Get all the performanceDatumList where feel in DEFAULT_FEEL or UPDATED_FEEL
        defaultPerformanceDatumShouldBeFound("feel.in=" + DEFAULT_FEEL + "," + UPDATED_FEEL);

        // Get all the performanceDatumList where feel equals to UPDATED_FEEL
        defaultPerformanceDatumShouldNotBeFound("feel.in=" + UPDATED_FEEL);
    }

    @Test
    @Transactional
    public void getAllPerformanceDataByFeelIsNullOrNotNull() throws Exception {
        // Initialize the database
        performanceDatumRepository.saveAndFlush(performanceDatum);

        // Get all the performanceDatumList where feel is not null
        defaultPerformanceDatumShouldBeFound("feel.specified=true");

        // Get all the performanceDatumList where feel is null
        defaultPerformanceDatumShouldNotBeFound("feel.specified=false");
    }

    @Test
    @Transactional
    public void getAllPerformanceDataByTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        performanceDatumRepository.saveAndFlush(performanceDatum);

        // Get all the performanceDatumList where timestamp equals to DEFAULT_TIMESTAMP
        defaultPerformanceDatumShouldBeFound("timestamp.equals=" + DEFAULT_TIMESTAMP);

        // Get all the performanceDatumList where timestamp equals to UPDATED_TIMESTAMP
        defaultPerformanceDatumShouldNotBeFound("timestamp.equals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllPerformanceDataByTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        performanceDatumRepository.saveAndFlush(performanceDatum);

        // Get all the performanceDatumList where timestamp in DEFAULT_TIMESTAMP or UPDATED_TIMESTAMP
        defaultPerformanceDatumShouldBeFound("timestamp.in=" + DEFAULT_TIMESTAMP + "," + UPDATED_TIMESTAMP);

        // Get all the performanceDatumList where timestamp equals to UPDATED_TIMESTAMP
        defaultPerformanceDatumShouldNotBeFound("timestamp.in=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllPerformanceDataByTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        performanceDatumRepository.saveAndFlush(performanceDatum);

        // Get all the performanceDatumList where timestamp is not null
        defaultPerformanceDatumShouldBeFound("timestamp.specified=true");

        // Get all the performanceDatumList where timestamp is null
        defaultPerformanceDatumShouldNotBeFound("timestamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllPerformanceDataByTimestampIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceDatumRepository.saveAndFlush(performanceDatum);

        // Get all the performanceDatumList where timestamp greater than or equals to DEFAULT_TIMESTAMP
        defaultPerformanceDatumShouldBeFound("timestamp.greaterOrEqualThan=" + DEFAULT_TIMESTAMP);

        // Get all the performanceDatumList where timestamp greater than or equals to UPDATED_TIMESTAMP
        defaultPerformanceDatumShouldNotBeFound("timestamp.greaterOrEqualThan=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllPerformanceDataByTimestampIsLessThanSomething() throws Exception {
        // Initialize the database
        performanceDatumRepository.saveAndFlush(performanceDatum);

        // Get all the performanceDatumList where timestamp less than or equals to DEFAULT_TIMESTAMP
        defaultPerformanceDatumShouldNotBeFound("timestamp.lessThan=" + DEFAULT_TIMESTAMP);

        // Get all the performanceDatumList where timestamp less than or equals to UPDATED_TIMESTAMP
        defaultPerformanceDatumShouldBeFound("timestamp.lessThan=" + UPDATED_TIMESTAMP);
    }


    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPerformanceDatumShouldBeFound(String filter) throws Exception {
        restPerformanceDatumMockMvc.perform(get("/api/performance-data?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(performanceDatum.getId().intValue())))
            .andExpect(jsonPath("$.[*].feel").value(hasItem(DEFAULT_FEEL.toString())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPerformanceDatumShouldNotBeFound(String filter) throws Exception {
        restPerformanceDatumMockMvc.perform(get("/api/performance-data?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingPerformanceDatum() throws Exception {
        // Get the performanceDatum
        restPerformanceDatumMockMvc.perform(get("/api/performance-data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePerformanceDatum() throws Exception {
        // Initialize the database
        performanceDatumRepository.saveAndFlush(performanceDatum);
        int databaseSizeBeforeUpdate = performanceDatumRepository.findAll().size();

        // Update the performanceDatum
        PerformanceDatum updatedPerformanceDatum = performanceDatumRepository.findOne(performanceDatum.getId());
        updatedPerformanceDatum
            .feel(UPDATED_FEEL)
            .timestamp(UPDATED_TIMESTAMP);
        PerformanceDatumDTO performanceDatumDTO = performanceDatumMapper.toDto(updatedPerformanceDatum);

        restPerformanceDatumMockMvc.perform(put("/api/performance-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(performanceDatumDTO)))
            .andExpect(status().isOk());

        // Validate the PerformanceDatum in the database
        List<PerformanceDatum> performanceDatumList = performanceDatumRepository.findAll();
        assertThat(performanceDatumList).hasSize(databaseSizeBeforeUpdate);
        PerformanceDatum testPerformanceDatum = performanceDatumList.get(performanceDatumList.size() - 1);
        assertThat(testPerformanceDatum.getFeel()).isEqualTo(UPDATED_FEEL);
        assertThat(testPerformanceDatum.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void updateNonExistingPerformanceDatum() throws Exception {
        int databaseSizeBeforeUpdate = performanceDatumRepository.findAll().size();

        // Create the PerformanceDatum
        PerformanceDatumDTO performanceDatumDTO = performanceDatumMapper.toDto(performanceDatum);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPerformanceDatumMockMvc.perform(put("/api/performance-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(performanceDatumDTO)))
            .andExpect(status().isCreated());

        // Validate the PerformanceDatum in the database
        List<PerformanceDatum> performanceDatumList = performanceDatumRepository.findAll();
        assertThat(performanceDatumList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePerformanceDatum() throws Exception {
        // Initialize the database
        performanceDatumRepository.saveAndFlush(performanceDatum);
        int databaseSizeBeforeDelete = performanceDatumRepository.findAll().size();

        // Get the performanceDatum
        restPerformanceDatumMockMvc.perform(delete("/api/performance-data/{id}", performanceDatum.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PerformanceDatum> performanceDatumList = performanceDatumRepository.findAll();
        assertThat(performanceDatumList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PerformanceDatum.class);
        PerformanceDatum performanceDatum1 = new PerformanceDatum();
        performanceDatum1.setId(1L);
        PerformanceDatum performanceDatum2 = new PerformanceDatum();
        performanceDatum2.setId(performanceDatum1.getId());
        assertThat(performanceDatum1).isEqualTo(performanceDatum2);
        performanceDatum2.setId(2L);
        assertThat(performanceDatum1).isNotEqualTo(performanceDatum2);
        performanceDatum1.setId(null);
        assertThat(performanceDatum1).isNotEqualTo(performanceDatum2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PerformanceDatumDTO.class);
        PerformanceDatumDTO performanceDatumDTO1 = new PerformanceDatumDTO();
        performanceDatumDTO1.setId(1L);
        PerformanceDatumDTO performanceDatumDTO2 = new PerformanceDatumDTO();
        assertThat(performanceDatumDTO1).isNotEqualTo(performanceDatumDTO2);
        performanceDatumDTO2.setId(performanceDatumDTO1.getId());
        assertThat(performanceDatumDTO1).isEqualTo(performanceDatumDTO2);
        performanceDatumDTO2.setId(2L);
        assertThat(performanceDatumDTO1).isNotEqualTo(performanceDatumDTO2);
        performanceDatumDTO1.setId(null);
        assertThat(performanceDatumDTO1).isNotEqualTo(performanceDatumDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(performanceDatumMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(performanceDatumMapper.fromId(null)).isNull();
    }
}
