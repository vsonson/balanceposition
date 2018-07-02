package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.FocusDatum;
import com.balpos.app.domain.User;
import com.balpos.app.repository.FocusDatumRepository;
import com.balpos.app.service.FocusDatumService;
import com.balpos.app.service.dto.FocusDatumDTO;
import com.balpos.app.service.mapper.FocusDatumMapper;
import com.balpos.app.service.util.UserResourceUtil;
import com.balpos.app.web.rest.errors.ExceptionTranslator;
import com.balpos.app.service.FocusDatumQueryService;

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
 * Test class for the FocusDatumResource REST controller.
 *
 * @see FocusDatumResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
@Ignore
public class FocusDatumResourceIntTest {

    private static final String DEFAULT_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_LEVEL = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private UserResourceUtil userResourceUtil;

    @Autowired
    private FocusDatumRepository focusDatumRepository;

    @Autowired
    private FocusDatumMapper focusDatumMapper;

    @Autowired
    private FocusDatumService focusDatumService;

    @Autowired
    private FocusDatumQueryService focusDatumQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFocusDatumMockMvc;

    private FocusDatum focusDatum;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FocusDatumResource focusDatumResource = new FocusDatumResource(focusDatumService, focusDatumQueryService, userResourceUtil);
        this.restFocusDatumMockMvc = MockMvcBuilders.standaloneSetup(focusDatumResource)
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
    public static FocusDatum createEntity(EntityManager em) {
        FocusDatum focusDatum = new FocusDatum()
            .setLevel(DEFAULT_LEVEL)
            .setTimestamp(DEFAULT_TIMESTAMP);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        focusDatum.setUser(user);
        return focusDatum;
    }

    @Before
    public void initTest() {
        focusDatum = createEntity(em);
    }

    @Test
    @Transactional
    public void createFocusDatum() throws Exception {
        int databaseSizeBeforeCreate = focusDatumRepository.findAll().size();

        // Create the FocusDatum
        FocusDatumDTO focusDatumDTO = focusDatumMapper.toDto(focusDatum);
        restFocusDatumMockMvc.perform(post("/api/focus-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(focusDatumDTO)))
            .andExpect(status().isCreated());

        // Validate the FocusDatum in the database
        List<FocusDatum> focusDatumList = focusDatumRepository.findAll();
        assertThat(focusDatumList).hasSize(databaseSizeBeforeCreate + 1);
        FocusDatum testFocusDatum = focusDatumList.get(focusDatumList.size() - 1);
        assertThat(testFocusDatum.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testFocusDatum.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
    }

    @Test
    @Transactional
    public void createFocusDatumWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = focusDatumRepository.findAll().size();

        // Create the FocusDatum with an existing ID
        focusDatum.setId(1L);
        FocusDatumDTO focusDatumDTO = focusDatumMapper.toDto(focusDatum);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFocusDatumMockMvc.perform(post("/api/focus-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(focusDatumDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<FocusDatum> focusDatumList = focusDatumRepository.findAll();
        assertThat(focusDatumList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = focusDatumRepository.findAll().size();
        // set the field null
        focusDatum.setLevel(null);

        // Create the FocusDatum, which fails.
        FocusDatumDTO focusDatumDTO = focusDatumMapper.toDto(focusDatum);

        restFocusDatumMockMvc.perform(post("/api/focus-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(focusDatumDTO)))
            .andExpect(status().isBadRequest());

        List<FocusDatum> focusDatumList = focusDatumRepository.findAll();
        assertThat(focusDatumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = focusDatumRepository.findAll().size();
        // set the field null
        focusDatum.setTimestamp(null);

        // Create the FocusDatum, which fails.
        FocusDatumDTO focusDatumDTO = focusDatumMapper.toDto(focusDatum);

        restFocusDatumMockMvc.perform(post("/api/focus-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(focusDatumDTO)))
            .andExpect(status().isBadRequest());

        List<FocusDatum> focusDatumList = focusDatumRepository.findAll();
        assertThat(focusDatumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFocusData() throws Exception {
        // Initialize the database
        focusDatumRepository.saveAndFlush(focusDatum);

        // Get all the focusDatumList
        restFocusDatumMockMvc.perform(get("/api/focus-data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(focusDatum.getId().intValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(sameInstant(DEFAULT_TIMESTAMP))));
    }

    @Test
    @Transactional
    public void getFocusDatum() throws Exception {
        // Initialize the database
        focusDatumRepository.saveAndFlush(focusDatum);

        // Get the focusDatum
        restFocusDatumMockMvc.perform(get("/api/focus-data/{id}", focusDatum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(focusDatum.getId().intValue()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()))
            .andExpect(jsonPath("$.timestamp").value(sameInstant(DEFAULT_TIMESTAMP)));
    }

    @Test
    @Transactional
    public void getAllFocusDataByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        focusDatumRepository.saveAndFlush(focusDatum);

        // Get all the focusDatumList where level equals to DEFAULT_LEVEL
        defaultFocusDatumShouldBeFound("level.equals=" + DEFAULT_LEVEL);

        // Get all the focusDatumList where level equals to UPDATED_LEVEL
        defaultFocusDatumShouldNotBeFound("level.equals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void getAllFocusDataByLevelIsInShouldWork() throws Exception {
        // Initialize the database
        focusDatumRepository.saveAndFlush(focusDatum);

        // Get all the focusDatumList where level in DEFAULT_LEVEL or UPDATED_LEVEL
        defaultFocusDatumShouldBeFound("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL);

        // Get all the focusDatumList where level equals to UPDATED_LEVEL
        defaultFocusDatumShouldNotBeFound("level.in=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void getAllFocusDataByLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        focusDatumRepository.saveAndFlush(focusDatum);

        // Get all the focusDatumList where level is not null
        defaultFocusDatumShouldBeFound("level.specified=true");

        // Get all the focusDatumList where level is null
        defaultFocusDatumShouldNotBeFound("level.specified=false");
    }

    @Test
    @Transactional
    public void getAllFocusDataByTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        focusDatumRepository.saveAndFlush(focusDatum);

        // Get all the focusDatumList where timestamp equals to DEFAULT_TIMESTAMP
        defaultFocusDatumShouldBeFound("timestamp.equals=" + DEFAULT_TIMESTAMP);

        // Get all the focusDatumList where timestamp equals to UPDATED_TIMESTAMP
        defaultFocusDatumShouldNotBeFound("timestamp.equals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllFocusDataByTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        focusDatumRepository.saveAndFlush(focusDatum);

        // Get all the focusDatumList where timestamp in DEFAULT_TIMESTAMP or UPDATED_TIMESTAMP
        defaultFocusDatumShouldBeFound("timestamp.in=" + DEFAULT_TIMESTAMP + "," + UPDATED_TIMESTAMP);

        // Get all the focusDatumList where timestamp equals to UPDATED_TIMESTAMP
        defaultFocusDatumShouldNotBeFound("timestamp.in=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllFocusDataByTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        focusDatumRepository.saveAndFlush(focusDatum);

        // Get all the focusDatumList where timestamp is not null
        defaultFocusDatumShouldBeFound("timestamp.specified=true");

        // Get all the focusDatumList where timestamp is null
        defaultFocusDatumShouldNotBeFound("timestamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllFocusDataByTimestampIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        focusDatumRepository.saveAndFlush(focusDatum);

        // Get all the focusDatumList where timestamp greater than or equals to DEFAULT_TIMESTAMP
        defaultFocusDatumShouldBeFound("timestamp.greaterOrEqualThan=" + DEFAULT_TIMESTAMP);

        // Get all the focusDatumList where timestamp greater than or equals to UPDATED_TIMESTAMP
        defaultFocusDatumShouldNotBeFound("timestamp.greaterOrEqualThan=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllFocusDataByTimestampIsLessThanSomething() throws Exception {
        // Initialize the database
        focusDatumRepository.saveAndFlush(focusDatum);

        // Get all the focusDatumList where timestamp less than or equals to DEFAULT_TIMESTAMP
        defaultFocusDatumShouldNotBeFound("timestamp.lessThan=" + DEFAULT_TIMESTAMP);

        // Get all the focusDatumList where timestamp less than or equals to UPDATED_TIMESTAMP
        defaultFocusDatumShouldBeFound("timestamp.lessThan=" + UPDATED_TIMESTAMP);
    }


    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultFocusDatumShouldBeFound(String filter) throws Exception {
        restFocusDatumMockMvc.perform(get("/api/focus-data?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(focusDatum.getId().intValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(sameInstant(DEFAULT_TIMESTAMP))));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultFocusDatumShouldNotBeFound(String filter) throws Exception {
        restFocusDatumMockMvc.perform(get("/api/focus-data?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingFocusDatum() throws Exception {
        // Get the focusDatum
        restFocusDatumMockMvc.perform(get("/api/focus-data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFocusDatum() throws Exception {
        // Initialize the database
        focusDatumRepository.saveAndFlush(focusDatum);
        int databaseSizeBeforeUpdate = focusDatumRepository.findAll().size();

        // Update the focusDatum
        FocusDatum updatedFocusDatum = focusDatumRepository.findOne(focusDatum.getId());
        updatedFocusDatum
            .setLevel(UPDATED_LEVEL)
            .setTimestamp(UPDATED_TIMESTAMP);
        FocusDatumDTO focusDatumDTO = focusDatumMapper.toDto(updatedFocusDatum);

        restFocusDatumMockMvc.perform(put("/api/focus-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(focusDatumDTO)))
            .andExpect(status().isOk());

        // Validate the FocusDatum in the database
        List<FocusDatum> focusDatumList = focusDatumRepository.findAll();
        assertThat(focusDatumList).hasSize(databaseSizeBeforeUpdate);
        FocusDatum testFocusDatum = focusDatumList.get(focusDatumList.size() - 1);
        assertThat(testFocusDatum.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testFocusDatum.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void updateNonExistingFocusDatum() throws Exception {
        int databaseSizeBeforeUpdate = focusDatumRepository.findAll().size();

        // Create the FocusDatum
        FocusDatumDTO focusDatumDTO = focusDatumMapper.toDto(focusDatum);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFocusDatumMockMvc.perform(put("/api/focus-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(focusDatumDTO)))
            .andExpect(status().isCreated());

        // Validate the FocusDatum in the database
        List<FocusDatum> focusDatumList = focusDatumRepository.findAll();
        assertThat(focusDatumList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFocusDatum() throws Exception {
        // Initialize the database
        focusDatumRepository.saveAndFlush(focusDatum);
        int databaseSizeBeforeDelete = focusDatumRepository.findAll().size();

        // Get the focusDatum
        restFocusDatumMockMvc.perform(delete("/api/focus-data/{id}", focusDatum.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FocusDatum> focusDatumList = focusDatumRepository.findAll();
        assertThat(focusDatumList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        FocusDatum focusDatum1 = new FocusDatum();
        focusDatum1.setId(1L);
        FocusDatum focusDatum2 = new FocusDatum();
        focusDatum2.setId(focusDatum1.getId());
        assertThat(focusDatum1).isEqualTo(focusDatum2);
        focusDatum2.setId(2L);
        assertThat(focusDatum1).isNotEqualTo(focusDatum2);
        focusDatum1.setId(null);
        assertThat(focusDatum1).isNotEqualTo(focusDatum2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(focusDatumMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(focusDatumMapper.fromId(null)).isNull();
    }
}
