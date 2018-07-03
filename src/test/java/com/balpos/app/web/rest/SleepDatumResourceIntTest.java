package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.SleepDatum;
import com.balpos.app.domain.User;
import com.balpos.app.repository.SleepDatumRepository;
import com.balpos.app.service.SleepDatumService;
import com.balpos.app.service.dto.SleepDatumDTO;
import com.balpos.app.service.mapper.SleepDatumMapper;
import com.balpos.app.service.util.UserResourceUtil;
import com.balpos.app.web.rest.errors.ExceptionTranslator;
import com.balpos.app.service.SleepDatumQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SleepDatumResource REST controller.
 *
 * @see SleepDatumResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
@Ignore
public class SleepDatumResourceIntTest {

    private static final Float DEFAULT_DURATION_HOURS = 0F;
    private static final Float UPDATED_DURATION_HOURS = 1F;

    private static final String DEFAULT_FEEL = "AAAAAAAAAA";
    private static final String UPDATED_FEEL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_TIMESTAMP = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TIMESTAMP = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private UserResourceUtil userResourceUtil;

    @Autowired
    private SleepDatumRepository sleepDatumRepository;

    @Autowired
    private SleepDatumMapper sleepDatumMapper;

    @Autowired
    private SleepDatumService sleepDatumService;

    @Autowired
    private SleepDatumQueryService sleepDatumQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSleepDatumMockMvc;

    private SleepDatum sleepDatum;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SleepDatumResource sleepDatumResource = new SleepDatumResource(sleepDatumService, sleepDatumQueryService, userResourceUtil);
        this.restSleepDatumMockMvc = MockMvcBuilders.standaloneSetup(sleepDatumResource)
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
    public static SleepDatum createEntity(EntityManager em) {
        SleepDatum sleepDatum = new SleepDatum()
            .setDurationHours(DEFAULT_DURATION_HOURS)
            .setFeel(DEFAULT_FEEL)
            .setTimestamp(DEFAULT_TIMESTAMP);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        sleepDatum.setUser(user);
        return sleepDatum;
    }

    @Before
    public void initTest() {
        sleepDatum = createEntity(em);
    }

    @Test
    @Transactional
    public void createSleepDatum() throws Exception {
        int databaseSizeBeforeCreate = sleepDatumRepository.findAll().size();

        // Create the SleepDatum
        SleepDatumDTO sleepDatumDTO = sleepDatumMapper.toDto(sleepDatum);
        restSleepDatumMockMvc.perform(post("/api/sleep-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sleepDatumDTO)))
            .andExpect(status().isCreated());

        // Validate the SleepDatum in the database
        List<SleepDatum> sleepDatumList = sleepDatumRepository.findAll();
        assertThat(sleepDatumList).hasSize(databaseSizeBeforeCreate + 1);
        SleepDatum testSleepDatum = sleepDatumList.get(sleepDatumList.size() - 1);
        assertThat(testSleepDatum.getDurationHours()).isEqualTo(DEFAULT_DURATION_HOURS);
        assertThat(testSleepDatum.getFeel()).isEqualTo(DEFAULT_FEEL);
        assertThat(testSleepDatum.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
    }

    @Test
    @Transactional
    public void createSleepDatumWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sleepDatumRepository.findAll().size();

        // Create the SleepDatum with an existing ID
        sleepDatum.setId(1L);
        SleepDatumDTO sleepDatumDTO = sleepDatumMapper.toDto(sleepDatum);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSleepDatumMockMvc.perform(post("/api/sleep-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sleepDatumDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SleepDatum> sleepDatumList = sleepDatumRepository.findAll();
        assertThat(sleepDatumList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDurationHoursIsRequired() throws Exception {
        int databaseSizeBeforeTest = sleepDatumRepository.findAll().size();
        // set the field null
        sleepDatum.setDurationHours(null);

        // Create the SleepDatum, which fails.
        SleepDatumDTO sleepDatumDTO = sleepDatumMapper.toDto(sleepDatum);

        restSleepDatumMockMvc.perform(post("/api/sleep-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sleepDatumDTO)))
            .andExpect(status().isBadRequest());

        List<SleepDatum> sleepDatumList = sleepDatumRepository.findAll();
        assertThat(sleepDatumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFeelIsRequired() throws Exception {
        int databaseSizeBeforeTest = sleepDatumRepository.findAll().size();
        // set the field null
        sleepDatum.setFeel(null);

        // Create the SleepDatum, which fails.
        SleepDatumDTO sleepDatumDTO = sleepDatumMapper.toDto(sleepDatum);

        restSleepDatumMockMvc.perform(post("/api/sleep-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sleepDatumDTO)))
            .andExpect(status().isBadRequest());

        List<SleepDatum> sleepDatumList = sleepDatumRepository.findAll();
        assertThat(sleepDatumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = sleepDatumRepository.findAll().size();
        // set the field null
        sleepDatum.setTimestamp(null);

        // Create the SleepDatum, which fails.
        SleepDatumDTO sleepDatumDTO = sleepDatumMapper.toDto(sleepDatum);

        restSleepDatumMockMvc.perform(post("/api/sleep-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sleepDatumDTO)))
            .andExpect(status().isBadRequest());

        List<SleepDatum> sleepDatumList = sleepDatumRepository.findAll();
        assertThat(sleepDatumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSleepData() throws Exception {
        // Initialize the database
        sleepDatumRepository.saveAndFlush(sleepDatum);

        // Get all the sleepDatumList
        restSleepDatumMockMvc.perform(get("/api/sleep-data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sleepDatum.getId().intValue())))
            .andExpect(jsonPath("$.[*].durationHours").value(hasItem(DEFAULT_DURATION_HOURS.doubleValue())))
            .andExpect(jsonPath("$.[*].feel").value(hasItem(DEFAULT_FEEL.toString())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())));
    }

    @Test
    @Transactional
    public void getSleepDatum() throws Exception {
        // Initialize the database
        sleepDatumRepository.saveAndFlush(sleepDatum);

        // Get the sleepDatum
        restSleepDatumMockMvc.perform(get("/api/sleep-data/{id}", sleepDatum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sleepDatum.getId().intValue()))
            .andExpect(jsonPath("$.durationHours").value(DEFAULT_DURATION_HOURS.doubleValue()))
            .andExpect(jsonPath("$.feel").value(DEFAULT_FEEL.toString()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()));
    }

    @Test
    @Transactional
    public void getAllSleepDataByDurationHoursIsEqualToSomething() throws Exception {
        // Initialize the database
        sleepDatumRepository.saveAndFlush(sleepDatum);

        // Get all the sleepDatumList where durationHours equals to DEFAULT_DURATION_HOURS
        defaultSleepDatumShouldBeFound("durationHours.equals=" + DEFAULT_DURATION_HOURS);

        // Get all the sleepDatumList where durationHours equals to UPDATED_DURATION_HOURS
        defaultSleepDatumShouldNotBeFound("durationHours.equals=" + UPDATED_DURATION_HOURS);
    }

    @Test
    @Transactional
    public void getAllSleepDataByDurationHoursIsInShouldWork() throws Exception {
        // Initialize the database
        sleepDatumRepository.saveAndFlush(sleepDatum);

        // Get all the sleepDatumList where durationHours in DEFAULT_DURATION_HOURS or UPDATED_DURATION_HOURS
        defaultSleepDatumShouldBeFound("durationHours.in=" + DEFAULT_DURATION_HOURS + "," + UPDATED_DURATION_HOURS);

        // Get all the sleepDatumList where durationHours equals to UPDATED_DURATION_HOURS
        defaultSleepDatumShouldNotBeFound("durationHours.in=" + UPDATED_DURATION_HOURS);
    }

    @Test
    @Transactional
    public void getAllSleepDataByDurationHoursIsNullOrNotNull() throws Exception {
        // Initialize the database
        sleepDatumRepository.saveAndFlush(sleepDatum);

        // Get all the sleepDatumList where durationHours is not null
        defaultSleepDatumShouldBeFound("durationHours.specified=true");

        // Get all the sleepDatumList where durationHours is null
        defaultSleepDatumShouldNotBeFound("durationHours.specified=false");
    }

    @Test
    @Transactional
    public void getAllSleepDataByFeelIsEqualToSomething() throws Exception {
        // Initialize the database
        sleepDatumRepository.saveAndFlush(sleepDatum);

        // Get all the sleepDatumList where feel equals to DEFAULT_FEEL
        defaultSleepDatumShouldBeFound("feel.equals=" + DEFAULT_FEEL);

        // Get all the sleepDatumList where feel equals to UPDATED_FEEL
        defaultSleepDatumShouldNotBeFound("feel.equals=" + UPDATED_FEEL);
    }

    @Test
    @Transactional
    public void getAllSleepDataByFeelIsInShouldWork() throws Exception {
        // Initialize the database
        sleepDatumRepository.saveAndFlush(sleepDatum);

        // Get all the sleepDatumList where feel in DEFAULT_FEEL or UPDATED_FEEL
        defaultSleepDatumShouldBeFound("feel.in=" + DEFAULT_FEEL + "," + UPDATED_FEEL);

        // Get all the sleepDatumList where feel equals to UPDATED_FEEL
        defaultSleepDatumShouldNotBeFound("feel.in=" + UPDATED_FEEL);
    }

    @Test
    @Transactional
    public void getAllSleepDataByFeelIsNullOrNotNull() throws Exception {
        // Initialize the database
        sleepDatumRepository.saveAndFlush(sleepDatum);

        // Get all the sleepDatumList where feel is not null
        defaultSleepDatumShouldBeFound("feel.specified=true");

        // Get all the sleepDatumList where feel is null
        defaultSleepDatumShouldNotBeFound("feel.specified=false");
    }

    @Test
    @Transactional
    public void getAllSleepDataByTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        sleepDatumRepository.saveAndFlush(sleepDatum);

        // Get all the sleepDatumList where timestamp equals to DEFAULT_TIMESTAMP
        defaultSleepDatumShouldBeFound("timestamp.equals=" + DEFAULT_TIMESTAMP);

        // Get all the sleepDatumList where timestamp equals to UPDATED_TIMESTAMP
        defaultSleepDatumShouldNotBeFound("timestamp.equals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllSleepDataByTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        sleepDatumRepository.saveAndFlush(sleepDatum);

        // Get all the sleepDatumList where timestamp in DEFAULT_TIMESTAMP or UPDATED_TIMESTAMP
        defaultSleepDatumShouldBeFound("timestamp.in=" + DEFAULT_TIMESTAMP + "," + UPDATED_TIMESTAMP);

        // Get all the sleepDatumList where timestamp equals to UPDATED_TIMESTAMP
        defaultSleepDatumShouldNotBeFound("timestamp.in=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllSleepDataByTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        sleepDatumRepository.saveAndFlush(sleepDatum);

        // Get all the sleepDatumList where timestamp is not null
        defaultSleepDatumShouldBeFound("timestamp.specified=true");

        // Get all the sleepDatumList where timestamp is null
        defaultSleepDatumShouldNotBeFound("timestamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllSleepDataByTimestampIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sleepDatumRepository.saveAndFlush(sleepDatum);

        // Get all the sleepDatumList where timestamp greater than or equals to DEFAULT_TIMESTAMP
        defaultSleepDatumShouldBeFound("timestamp.greaterOrEqualThan=" + DEFAULT_TIMESTAMP);

        // Get all the sleepDatumList where timestamp greater than or equals to UPDATED_TIMESTAMP
        defaultSleepDatumShouldNotBeFound("timestamp.greaterOrEqualThan=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllSleepDataByTimestampIsLessThanSomething() throws Exception {
        // Initialize the database
        sleepDatumRepository.saveAndFlush(sleepDatum);

        // Get all the sleepDatumList where timestamp less than or equals to DEFAULT_TIMESTAMP
        defaultSleepDatumShouldNotBeFound("timestamp.lessThan=" + DEFAULT_TIMESTAMP);

        // Get all the sleepDatumList where timestamp less than or equals to UPDATED_TIMESTAMP
        defaultSleepDatumShouldBeFound("timestamp.lessThan=" + UPDATED_TIMESTAMP);
    }


    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSleepDatumShouldBeFound(String filter) throws Exception {
        restSleepDatumMockMvc.perform(get("/api/sleep-data?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sleepDatum.getId().intValue())))
            .andExpect(jsonPath("$.[*].durationHours").value(hasItem(DEFAULT_DURATION_HOURS.doubleValue())))
            .andExpect(jsonPath("$.[*].feel").value(hasItem(DEFAULT_FEEL.toString())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSleepDatumShouldNotBeFound(String filter) throws Exception {
        restSleepDatumMockMvc.perform(get("/api/sleep-data?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingSleepDatum() throws Exception {
        // Get the sleepDatum
        restSleepDatumMockMvc.perform(get("/api/sleep-data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSleepDatum() throws Exception {
        // Initialize the database
        sleepDatumRepository.saveAndFlush(sleepDatum);
        int databaseSizeBeforeUpdate = sleepDatumRepository.findAll().size();

        // Update the sleepDatum
        SleepDatum updatedSleepDatum = sleepDatumRepository.findOne(sleepDatum.getId());
        updatedSleepDatum
            .setDurationHours(UPDATED_DURATION_HOURS)
            .setFeel(UPDATED_FEEL)
            .setTimestamp(UPDATED_TIMESTAMP);
        SleepDatumDTO sleepDatumDTO = sleepDatumMapper.toDto(updatedSleepDatum);

        restSleepDatumMockMvc.perform(put("/api/sleep-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sleepDatumDTO)))
            .andExpect(status().isOk());

        // Validate the SleepDatum in the database
        List<SleepDatum> sleepDatumList = sleepDatumRepository.findAll();
        assertThat(sleepDatumList).hasSize(databaseSizeBeforeUpdate);
        SleepDatum testSleepDatum = sleepDatumList.get(sleepDatumList.size() - 1);
        assertThat(testSleepDatum.getDurationHours()).isEqualTo(UPDATED_DURATION_HOURS);
        assertThat(testSleepDatum.getFeel()).isEqualTo(UPDATED_FEEL);
        assertThat(testSleepDatum.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void updateNonExistingSleepDatum() throws Exception {
        int databaseSizeBeforeUpdate = sleepDatumRepository.findAll().size();

        // Create the SleepDatum
        SleepDatumDTO sleepDatumDTO = sleepDatumMapper.toDto(sleepDatum);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSleepDatumMockMvc.perform(put("/api/sleep-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sleepDatumDTO)))
            .andExpect(status().isCreated());

        // Validate the SleepDatum in the database
        List<SleepDatum> sleepDatumList = sleepDatumRepository.findAll();
        assertThat(sleepDatumList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSleepDatum() throws Exception {
        // Initialize the database
        sleepDatumRepository.saveAndFlush(sleepDatum);
        int databaseSizeBeforeDelete = sleepDatumRepository.findAll().size();

        // Get the sleepDatum
        restSleepDatumMockMvc.perform(delete("/api/sleep-data/{id}", sleepDatum.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SleepDatum> sleepDatumList = sleepDatumRepository.findAll();
        assertThat(sleepDatumList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        SleepDatum sleepDatum1 = new SleepDatum();
        sleepDatum1.setId(1L);
        SleepDatum sleepDatum2 = new SleepDatum();
        sleepDatum2.setId(sleepDatum1.getId());
        assertThat(sleepDatum1).isEqualTo(sleepDatum2);
        sleepDatum2.setId(2L);
        assertThat(sleepDatum1).isNotEqualTo(sleepDatum2);
        sleepDatum1.setId(null);
        assertThat(sleepDatum1).isNotEqualTo(sleepDatum2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(sleepDatumMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(sleepDatumMapper.fromId(null)).isNull();
    }
}
