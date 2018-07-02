package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.MoodDatum;
import com.balpos.app.domain.User;
import com.balpos.app.repository.MoodDatumRepository;
import com.balpos.app.service.MoodDatumService;
import com.balpos.app.service.dto.MoodDatumDTO;
import com.balpos.app.service.mapper.MoodDatumMapper;
import com.balpos.app.service.util.UserResourceUtil;
import com.balpos.app.web.rest.errors.ExceptionTranslator;
import com.balpos.app.service.MoodDatumQueryService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.balpos.app.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MoodDatumResource REST controller.
 *
 * @see MoodDatumResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
@Ignore
public class MoodDatumResourceIntTest {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private UserResourceUtil userResourceUtil;

    @Autowired
    private MoodDatumRepository moodDatumRepository;

    @Autowired
    private MoodDatumMapper moodDatumMapper;

    @Autowired
    private MoodDatumService moodDatumService;

    @Autowired
    private MoodDatumQueryService moodDatumQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMoodDatumMockMvc;

    private MoodDatum moodDatum;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MoodDatumResource moodDatumResource = new MoodDatumResource(moodDatumService, moodDatumQueryService, userResourceUtil);
        this.restMoodDatumMockMvc = MockMvcBuilders.standaloneSetup(moodDatumResource)
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
    public static MoodDatum createEntity(EntityManager em) {
        MoodDatum moodDatum = new MoodDatum()
            .setValue(DEFAULT_VALUE)
            .setTimestamp(DEFAULT_TIMESTAMP);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        moodDatum.setUser(user);
        return moodDatum;
    }

    @Before
    public void initTest() {
        moodDatum = createEntity(em);
    }

    @Test
    @Transactional
    public void createMoodDatum() throws Exception {
        int databaseSizeBeforeCreate = moodDatumRepository.findAll().size();

        // Create the MoodDatum
        MoodDatumDTO moodDatumDTO = moodDatumMapper.toDto(moodDatum);
        restMoodDatumMockMvc.perform(post("/api/mood-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moodDatumDTO)))
            .andExpect(status().isCreated());

        // Validate the MoodDatum in the database
        List<MoodDatum> moodDatumList = moodDatumRepository.findAll();
        assertThat(moodDatumList).hasSize(databaseSizeBeforeCreate + 1);
        MoodDatum testMoodDatum = moodDatumList.get(moodDatumList.size() - 1);
        assertThat(testMoodDatum.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testMoodDatum.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
    }

    @Test
    @Transactional
    public void createMoodDatumWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = moodDatumRepository.findAll().size();

        // Create the MoodDatum with an existing ID
        moodDatum.setId(1L);
        MoodDatumDTO moodDatumDTO = moodDatumMapper.toDto(moodDatum);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMoodDatumMockMvc.perform(post("/api/mood-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moodDatumDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<MoodDatum> moodDatumList = moodDatumRepository.findAll();
        assertThat(moodDatumList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = moodDatumRepository.findAll().size();
        // set the field null
        moodDatum.setValue(null);

        // Create the MoodDatum, which fails.
        MoodDatumDTO moodDatumDTO = moodDatumMapper.toDto(moodDatum);

        restMoodDatumMockMvc.perform(post("/api/mood-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moodDatumDTO)))
            .andExpect(status().isBadRequest());

        List<MoodDatum> moodDatumList = moodDatumRepository.findAll();
        assertThat(moodDatumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMoodData() throws Exception {
        // Initialize the database
        moodDatumRepository.saveAndFlush(moodDatum);

        // Get all the moodDatumList
        restMoodDatumMockMvc.perform(get("/api/mood-data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moodDatum.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(sameInstant(DEFAULT_TIMESTAMP))));
    }

    @Test
    @Transactional
    public void getMoodDatum() throws Exception {
        // Initialize the database
        moodDatumRepository.saveAndFlush(moodDatum);

        // Get the moodDatum
        restMoodDatumMockMvc.perform(get("/api/mood-data/{id}", moodDatum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(moodDatum.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.timestamp").value(sameInstant(DEFAULT_TIMESTAMP)));
    }

    @Test
    @Transactional
    public void getAllMoodDataByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        moodDatumRepository.saveAndFlush(moodDatum);

        // Get all the moodDatumList where value equals to DEFAULT_VALUE
        defaultMoodDatumShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the moodDatumList where value equals to UPDATED_VALUE
        defaultMoodDatumShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllMoodDataByValueIsInShouldWork() throws Exception {
        // Initialize the database
        moodDatumRepository.saveAndFlush(moodDatum);

        // Get all the moodDatumList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultMoodDatumShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the moodDatumList where value equals to UPDATED_VALUE
        defaultMoodDatumShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllMoodDataByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        moodDatumRepository.saveAndFlush(moodDatum);

        // Get all the moodDatumList where value is not null
        defaultMoodDatumShouldBeFound("value.specified=true");

        // Get all the moodDatumList where value is null
        defaultMoodDatumShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoodDataByTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        moodDatumRepository.saveAndFlush(moodDatum);

        // Get all the moodDatumList where timestamp equals to DEFAULT_TIMESTAMP
        defaultMoodDatumShouldBeFound("timestamp.equals=" + DEFAULT_TIMESTAMP);

        // Get all the moodDatumList where timestamp equals to UPDATED_TIMESTAMP
        defaultMoodDatumShouldNotBeFound("timestamp.equals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllMoodDataByTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        moodDatumRepository.saveAndFlush(moodDatum);

        // Get all the moodDatumList where timestamp in DEFAULT_TIMESTAMP or UPDATED_TIMESTAMP
        defaultMoodDatumShouldBeFound("timestamp.in=" + DEFAULT_TIMESTAMP + "," + UPDATED_TIMESTAMP);

        // Get all the moodDatumList where timestamp equals to UPDATED_TIMESTAMP
        defaultMoodDatumShouldNotBeFound("timestamp.in=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllMoodDataByTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        moodDatumRepository.saveAndFlush(moodDatum);

        // Get all the moodDatumList where timestamp is not null
        defaultMoodDatumShouldBeFound("timestamp.specified=true");

        // Get all the moodDatumList where timestamp is null
        defaultMoodDatumShouldNotBeFound("timestamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoodDataByTimestampIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        moodDatumRepository.saveAndFlush(moodDatum);

        // Get all the moodDatumList where timestamp greater than or equals to DEFAULT_TIMESTAMP
        defaultMoodDatumShouldBeFound("timestamp.greaterOrEqualThan=" + DEFAULT_TIMESTAMP);

        // Get all the moodDatumList where timestamp greater than or equals to UPDATED_TIMESTAMP
        defaultMoodDatumShouldNotBeFound("timestamp.greaterOrEqualThan=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllMoodDataByTimestampIsLessThanSomething() throws Exception {
        // Initialize the database
        moodDatumRepository.saveAndFlush(moodDatum);

        // Get all the moodDatumList where timestamp less than or equals to DEFAULT_TIMESTAMP
        defaultMoodDatumShouldNotBeFound("timestamp.lessThan=" + DEFAULT_TIMESTAMP);

        // Get all the moodDatumList where timestamp less than or equals to UPDATED_TIMESTAMP
        defaultMoodDatumShouldBeFound("timestamp.lessThan=" + UPDATED_TIMESTAMP);
    }


    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMoodDatumShouldBeFound(String filter) throws Exception {
        restMoodDatumMockMvc.perform(get("/api/mood-data?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moodDatum.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(sameInstant(DEFAULT_TIMESTAMP))));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMoodDatumShouldNotBeFound(String filter) throws Exception {
        restMoodDatumMockMvc.perform(get("/api/mood-data?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingMoodDatum() throws Exception {
        // Get the moodDatum
        restMoodDatumMockMvc.perform(get("/api/mood-data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMoodDatum() throws Exception {
        // Initialize the database
        moodDatumRepository.saveAndFlush(moodDatum);
        int databaseSizeBeforeUpdate = moodDatumRepository.findAll().size();

        // Update the moodDatum
        MoodDatum updatedMoodDatum = moodDatumRepository.findOne(moodDatum.getId());
        updatedMoodDatum
            .setValue(UPDATED_VALUE)
            .setTimestamp(UPDATED_TIMESTAMP);
        MoodDatumDTO moodDatumDTO = moodDatumMapper.toDto(updatedMoodDatum);

        restMoodDatumMockMvc.perform(put("/api/mood-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moodDatumDTO)))
            .andExpect(status().isOk());

        // Validate the MoodDatum in the database
        List<MoodDatum> moodDatumList = moodDatumRepository.findAll();
        assertThat(moodDatumList).hasSize(databaseSizeBeforeUpdate);
        MoodDatum testMoodDatum = moodDatumList.get(moodDatumList.size() - 1);
        assertThat(testMoodDatum.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testMoodDatum.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void updateNonExistingMoodDatum() throws Exception {
        int databaseSizeBeforeUpdate = moodDatumRepository.findAll().size();

        // Create the MoodDatum
        MoodDatumDTO moodDatumDTO = moodDatumMapper.toDto(moodDatum);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMoodDatumMockMvc.perform(put("/api/mood-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moodDatumDTO)))
            .andExpect(status().isCreated());

        // Validate the MoodDatum in the database
        List<MoodDatum> moodDatumList = moodDatumRepository.findAll();
        assertThat(moodDatumList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMoodDatum() throws Exception {
        // Initialize the database
        moodDatumRepository.saveAndFlush(moodDatum);
        int databaseSizeBeforeDelete = moodDatumRepository.findAll().size();

        // Get the moodDatum
        restMoodDatumMockMvc.perform(delete("/api/mood-data/{id}", moodDatum.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MoodDatum> moodDatumList = moodDatumRepository.findAll();
        assertThat(moodDatumList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() {
        MoodDatum moodDatum1 = new MoodDatum();
        moodDatum1.setId(1L);
        MoodDatum moodDatum2 = new MoodDatum();
        moodDatum2.setId(moodDatum1.getId());
        assertThat(moodDatum1).isEqualTo(moodDatum2);
        moodDatum2.setId(2L);
        assertThat(moodDatum1).isNotEqualTo(moodDatum2);
        moodDatum1.setId(null);
        assertThat(moodDatum1).isNotEqualTo(moodDatum2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() {
        MoodDatumDTO moodDatumDTO1 = new MoodDatumDTO();
        moodDatumDTO1.setId(1L);
        MoodDatumDTO moodDatumDTO2 = new MoodDatumDTO();
        assertThat(moodDatumDTO1).isNotEqualTo(moodDatumDTO2);
        moodDatumDTO2.setId(moodDatumDTO1.getId());
        assertThat(moodDatumDTO1).isEqualTo(moodDatumDTO2);
        moodDatumDTO2.setId(2L);
        assertThat(moodDatumDTO1).isNotEqualTo(moodDatumDTO2);
        moodDatumDTO1.setId(null);
        assertThat(moodDatumDTO1).isNotEqualTo(moodDatumDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(moodDatumMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(moodDatumMapper.fromId(null)).isNull();
    }
}
