package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;
import com.balpos.app.domain.StressDatum;
import com.balpos.app.domain.User;
import com.balpos.app.repository.StressDatumRepository;
import com.balpos.app.service.StressDatumQueryService;
import com.balpos.app.service.StressDatumService;
import com.balpos.app.service.UserService;
import com.balpos.app.service.dto.StressDatumDTO;
import com.balpos.app.service.mapper.StressDatumMapper;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static com.balpos.app.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the StressDatumResource REST controller.
 *
 * @see StressDatumResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
@Ignore
public class StressDatumResourceIntTest {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private StressDatumRepository stressDatumRepository;

    @Autowired
    private StressDatumMapper stressDatumMapper;

    @Autowired
    private StressDatumService stressDatumService;

    @Autowired
    private StressDatumQueryService stressDatumQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private UserService userService;

    @Autowired
    private EntityManager em;

    private MockMvc restStressDatumMockMvc;

    private StressDatum stressDatum;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StressDatum createEntity(EntityManager em) {
        StressDatum stressDatum = new StressDatum()
            .setValue(DEFAULT_VALUE)
            .setTimestamp(DEFAULT_TIMESTAMP);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        stressDatum.setUser(user);
        return stressDatum;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StressDatumResource stressDatumResource = new StressDatumResource(stressDatumService, stressDatumQueryService, stressDatumRepository, userService);
        this.restStressDatumMockMvc = MockMvcBuilders.standaloneSetup(stressDatumResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        stressDatum = createEntity(em);
    }

    @Test
    @Transactional
    public void createStressDatum() throws Exception {
        int databaseSizeBeforeCreate = stressDatumRepository.findAll().size();

        // Create the StressDatum
        StressDatumDTO stressDatumDTO = stressDatumMapper.toDto(stressDatum);
        restStressDatumMockMvc.perform(post("/api/stress-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stressDatumDTO)))
            .andExpect(status().isCreated());

        // Validate the StressDatum in the database
        List<StressDatum> stressDatumList = stressDatumRepository.findAll();
        assertThat(stressDatumList).hasSize(databaseSizeBeforeCreate + 1);
        StressDatum testStressDatum = stressDatumList.get(stressDatumList.size() - 1);
        assertThat(testStressDatum.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testStressDatum.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = stressDatumRepository.findAll().size();
        // set the field null
        stressDatum.setValue(null);

        // Create the StressDatum, which fails.
        StressDatumDTO stressDatumDTO = stressDatumMapper.toDto(stressDatum);

        restStressDatumMockMvc.perform(post("/api/stress-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stressDatumDTO)))
            .andExpect(status().isBadRequest());

        List<StressDatum> stressDatumList = stressDatumRepository.findAll();
        assertThat(stressDatumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = stressDatumRepository.findAll().size();
        // set the field null
        stressDatum.setTimestamp(null);

        // Create the StressDatum, which fails.
        StressDatumDTO stressDatumDTO = stressDatumMapper.toDto(stressDatum);

        restStressDatumMockMvc.perform(post("/api/stress-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stressDatumDTO)))
            .andExpect(status().isBadRequest());

        List<StressDatum> stressDatumList = stressDatumRepository.findAll();
        assertThat(stressDatumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStressData() throws Exception {
        // Initialize the database
        stressDatumRepository.saveAndFlush(stressDatum);

        // Get all the stressDatumList
        restStressDatumMockMvc.perform(get("/api/stress-data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stressDatum.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(sameInstant(DEFAULT_TIMESTAMP))));
    }

    @Test
    @Transactional
    public void getStressDatum() throws Exception {
        // Initialize the database
        stressDatumRepository.saveAndFlush(stressDatum);

        // Get the stressDatum
        restStressDatumMockMvc.perform(get("/api/stress-data/{id}", stressDatum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stressDatum.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.timestamp").value(sameInstant(DEFAULT_TIMESTAMP)));
    }

    @Test
    @Transactional
    @WithUserDetails
    public void getAllStressDataByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        stressDatumRepository.saveAndFlush(stressDatum);

        // Get all the stressDatumList where value equals to DEFAULT_VALUE
        defaultStressDatumShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the stressDatumList where value equals to UPDATED_VALUE
        defaultStressDatumShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllStressDataByValueIsInShouldWork() throws Exception {
        // Initialize the database
        stressDatumRepository.saveAndFlush(stressDatum);

        // Get all the stressDatumList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultStressDatumShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the stressDatumList where value equals to UPDATED_VALUE
        defaultStressDatumShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllStressDataByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        stressDatumRepository.saveAndFlush(stressDatum);

        // Get all the stressDatumList where value is not null
        defaultStressDatumShouldBeFound("value.specified=true");

        // Get all the stressDatumList where value is null
        defaultStressDatumShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    public void getAllStressDataByTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        stressDatumRepository.saveAndFlush(stressDatum);

        // Get all the stressDatumList where timestamp equals to DEFAULT_TIMESTAMP
        defaultStressDatumShouldBeFound("timestamp.equals=" + DEFAULT_TIMESTAMP);

        // Get all the stressDatumList where timestamp equals to UPDATED_TIMESTAMP
        defaultStressDatumShouldNotBeFound("timestamp.equals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllStressDataByTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        stressDatumRepository.saveAndFlush(stressDatum);

        // Get all the stressDatumList where timestamp in DEFAULT_TIMESTAMP or UPDATED_TIMESTAMP
        defaultStressDatumShouldBeFound("timestamp.in=" + DEFAULT_TIMESTAMP + "," + UPDATED_TIMESTAMP);

        // Get all the stressDatumList where timestamp equals to UPDATED_TIMESTAMP
        defaultStressDatumShouldNotBeFound("timestamp.in=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllStressDataByTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        stressDatumRepository.saveAndFlush(stressDatum);

        // Get all the stressDatumList where timestamp is not null
        defaultStressDatumShouldBeFound("timestamp.specified=true");

        // Get all the stressDatumList where timestamp is null
        defaultStressDatumShouldNotBeFound("timestamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllStressDataByTimestampIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stressDatumRepository.saveAndFlush(stressDatum);

        // Get all the stressDatumList where timestamp greater than or equals to DEFAULT_TIMESTAMP
        defaultStressDatumShouldBeFound("timestamp.greaterOrEqualThan=" + DEFAULT_TIMESTAMP);

        // Get all the stressDatumList where timestamp greater than or equals to UPDATED_TIMESTAMP
        defaultStressDatumShouldNotBeFound("timestamp.greaterOrEqualThan=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllStressDataByTimestampIsLessThanSomething() throws Exception {
        // Initialize the database
        stressDatumRepository.saveAndFlush(stressDatum);

        // Get all the stressDatumList where timestamp less than or equals to DEFAULT_TIMESTAMP
        defaultStressDatumShouldNotBeFound("timestamp.lessThan=" + DEFAULT_TIMESTAMP);

        // Get all the stressDatumList where timestamp less than or equals to UPDATED_TIMESTAMP
        defaultStressDatumShouldBeFound("timestamp.lessThan=" + UPDATED_TIMESTAMP);
    }


    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultStressDatumShouldBeFound(String filter) throws Exception {
        restStressDatumMockMvc.perform(get("/api/stress-data?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(sameInstant(DEFAULT_TIMESTAMP))));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultStressDatumShouldNotBeFound(String filter) throws Exception {
        restStressDatumMockMvc.perform(get("/api/stress-data?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingStressDatum() throws Exception {
        // Get the stressDatum
        restStressDatumMockMvc.perform(get("/api/stress-data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void deleteStressDatum() throws Exception {
        // Initialize the database
        stressDatumRepository.saveAndFlush(stressDatum);
        int databaseSizeBeforeDelete = stressDatumRepository.findAll().size();

        // Get the stressDatum
        restStressDatumMockMvc.perform(delete("/api/stress-data/{id}", stressDatum.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StressDatum> stressDatumList = stressDatumRepository.findAll();
        assertThat(stressDatumList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StressDatum.class);
        StressDatum stressDatum1 = new StressDatum();
        stressDatum1.setId(1L);
        StressDatum stressDatum2 = new StressDatum();
        stressDatum2.setId(stressDatum1.getId());
        assertThat(stressDatum1).isEqualTo(stressDatum2);
        stressDatum2.setId(2L);
        assertThat(stressDatum1).isNotEqualTo(stressDatum2);
        stressDatum1.setId(null);
        assertThat(stressDatum1).isNotEqualTo(stressDatum2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(stressDatumMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(stressDatumMapper.fromId(null)).isNull();
    }
}
