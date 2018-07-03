package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;
import com.balpos.app.domain.BodyDatum;
import com.balpos.app.domain.User;
import com.balpos.app.domain.enumeration.DigestiveLevel;
import com.balpos.app.domain.enumeration.HeadacheLevel;
import com.balpos.app.repository.BodyDatumRepository;
import com.balpos.app.service.BodyDatumQueryService;
import com.balpos.app.service.BodyDatumService;
import com.balpos.app.service.dto.BodyDatumDTO;
import com.balpos.app.service.mapper.BodyDatumMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BodyDatumResource REST controller.
 *
 * @see BodyDatumResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
@Ignore
public class BodyDatumResourceIntTest {

    private static final HeadacheLevel DEFAULT_HEADACHE = HeadacheLevel.NO;
    private static final HeadacheLevel UPDATED_HEADACHE = HeadacheLevel.MILD;

    private static final DigestiveLevel DEFAULT_DIGESTIVE = DigestiveLevel.NO;
    private static final DigestiveLevel UPDATED_DIGESTIVE = DigestiveLevel.MILD;

    private static final LocalDate DEFAULT_TIMESTAMP = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TIMESTAMP = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private UserResourceUtil userResourceUtil;

    @Autowired
    private BodyDatumRepository bodyDatumRepository;

    @Autowired
    private BodyDatumMapper bodyDatumMapper;

    @Autowired
    private BodyDatumService bodyDatumService;

    @Autowired
    private BodyDatumQueryService bodyDatumQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBodyDatumMockMvc;

    private BodyDatum bodyDatum;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BodyDatum createEntity(EntityManager em) {
        BodyDatum bodyDatum = new BodyDatum()
            .setHeadache(DEFAULT_HEADACHE)
            .setDigestive(DEFAULT_DIGESTIVE)
            .setTimestamp(DEFAULT_TIMESTAMP);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        bodyDatum.setUser(user);
        return bodyDatum;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BodyDatumResource bodyDatumResource = new BodyDatumResource(bodyDatumService, bodyDatumQueryService, userResourceUtil);
        this.restBodyDatumMockMvc = MockMvcBuilders.standaloneSetup(bodyDatumResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        bodyDatum = createEntity(em);
    }

    @Test
    @Transactional
    public void createBodyDatum() throws Exception {
        int databaseSizeBeforeCreate = bodyDatumRepository.findAll().size();

        // Create the BodyDatum
        BodyDatumDTO bodyDatumDTO = bodyDatumMapper.toDto(bodyDatum);
        restBodyDatumMockMvc.perform(post("/api/body-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bodyDatumDTO)))
            .andExpect(status().isCreated());

        // Validate the BodyDatum in the database
        List<BodyDatum> bodyDatumList = bodyDatumRepository.findAll();
        assertThat(bodyDatumList).hasSize(databaseSizeBeforeCreate + 1);
        BodyDatum testBodyDatum = bodyDatumList.get(bodyDatumList.size() - 1);
        assertThat(testBodyDatum.getHeadache()).isEqualTo(DEFAULT_HEADACHE);
        assertThat(testBodyDatum.getDigestive()).isEqualTo(DEFAULT_DIGESTIVE);
        assertThat(testBodyDatum.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
    }

    @Test
    @Transactional
    public void createBodyDatumWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bodyDatumRepository.findAll().size();

        // Create the BodyDatum with an existing ID
        bodyDatum.setId(1L);
        BodyDatumDTO bodyDatumDTO = bodyDatumMapper.toDto(bodyDatum);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBodyDatumMockMvc.perform(post("/api/body-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bodyDatumDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<BodyDatum> bodyDatumList = bodyDatumRepository.findAll();
        assertThat(bodyDatumList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkHeadacheIsRequired() throws Exception {
        int databaseSizeBeforeTest = bodyDatumRepository.findAll().size();
        // set the field null
        bodyDatum.setHeadache(null);

        // Create the BodyDatum, which fails.
        BodyDatumDTO bodyDatumDTO = bodyDatumMapper.toDto(bodyDatum);

        restBodyDatumMockMvc.perform(post("/api/body-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bodyDatumDTO)))
            .andExpect(status().isBadRequest());

        List<BodyDatum> bodyDatumList = bodyDatumRepository.findAll();
        assertThat(bodyDatumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDigestiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = bodyDatumRepository.findAll().size();
        // set the field null
        bodyDatum.setDigestive(null);

        // Create the BodyDatum, which fails.
        BodyDatumDTO bodyDatumDTO = bodyDatumMapper.toDto(bodyDatum);

        restBodyDatumMockMvc.perform(post("/api/body-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bodyDatumDTO)))
            .andExpect(status().isBadRequest());

        List<BodyDatum> bodyDatumList = bodyDatumRepository.findAll();
        assertThat(bodyDatumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = bodyDatumRepository.findAll().size();
        // set the field null
        bodyDatum.setTimestamp(null);

        // Create the BodyDatum, which fails.
        BodyDatumDTO bodyDatumDTO = bodyDatumMapper.toDto(bodyDatum);

        restBodyDatumMockMvc.perform(post("/api/body-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bodyDatumDTO)))
            .andExpect(status().isBadRequest());

        List<BodyDatum> bodyDatumList = bodyDatumRepository.findAll();
        assertThat(bodyDatumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBodyData() throws Exception {
        // Initialize the database
        bodyDatumRepository.saveAndFlush(bodyDatum);

        // Get all the bodyDatumList
        restBodyDatumMockMvc.perform(get("/api/body-data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bodyDatum.getId().intValue())))
            .andExpect(jsonPath("$.[*].headache").value(hasItem(DEFAULT_HEADACHE.toString())))
            .andExpect(jsonPath("$.[*].digestive").value(hasItem(DEFAULT_DIGESTIVE.toString())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())));
    }

    @Test
    @Transactional
    public void getBodyDatum() throws Exception {
        // Initialize the database
        bodyDatumRepository.saveAndFlush(bodyDatum);

        // Get the bodyDatum
        restBodyDatumMockMvc.perform(get("/api/body-data/{id}", bodyDatum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bodyDatum.getId().intValue()))
            .andExpect(jsonPath("$.headache").value(DEFAULT_HEADACHE.toString()))
            .andExpect(jsonPath("$.digestive").value(DEFAULT_DIGESTIVE.toString()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()));
    }

    @Test
    @Transactional
    public void getAllBodyDataByHeadacheIsEqualToSomething() throws Exception {
        // Initialize the database
        bodyDatumRepository.saveAndFlush(bodyDatum);

        // Get all the bodyDatumList where headache equals to DEFAULT_HEADACHE
        defaultBodyDatumShouldBeFound("headache.equals=" + DEFAULT_HEADACHE);

        // Get all the bodyDatumList where headache equals to UPDATED_HEADACHE
        defaultBodyDatumShouldNotBeFound("headache.equals=" + UPDATED_HEADACHE);
    }

    @Test
    @Transactional
    public void getAllBodyDataByHeadacheIsInShouldWork() throws Exception {
        // Initialize the database
        bodyDatumRepository.saveAndFlush(bodyDatum);

        // Get all the bodyDatumList where headache in DEFAULT_HEADACHE or UPDATED_HEADACHE
        defaultBodyDatumShouldBeFound("headache.in=" + DEFAULT_HEADACHE + "," + UPDATED_HEADACHE);

        // Get all the bodyDatumList where headache equals to UPDATED_HEADACHE
        defaultBodyDatumShouldNotBeFound("headache.in=" + UPDATED_HEADACHE);
    }

    @Test
    @Transactional
    public void getAllBodyDataByHeadacheIsNullOrNotNull() throws Exception {
        // Initialize the database
        bodyDatumRepository.saveAndFlush(bodyDatum);

        // Get all the bodyDatumList where headache is not null
        defaultBodyDatumShouldBeFound("headache.specified=true");

        // Get all the bodyDatumList where headache is null
        defaultBodyDatumShouldNotBeFound("headache.specified=false");
    }

    @Test
    @Transactional
    public void getAllBodyDataByDigestiveIsEqualToSomething() throws Exception {
        // Initialize the database
        bodyDatumRepository.saveAndFlush(bodyDatum);

        // Get all the bodyDatumList where digestive equals to DEFAULT_DIGESTIVE
        defaultBodyDatumShouldBeFound("digestive.equals=" + DEFAULT_DIGESTIVE);

        // Get all the bodyDatumList where digestive equals to UPDATED_DIGESTIVE
        defaultBodyDatumShouldNotBeFound("digestive.equals=" + UPDATED_DIGESTIVE);
    }

    @Test
    @Transactional
    public void getAllBodyDataByDigestiveIsInShouldWork() throws Exception {
        // Initialize the database
        bodyDatumRepository.saveAndFlush(bodyDatum);

        // Get all the bodyDatumList where digestive in DEFAULT_DIGESTIVE or UPDATED_DIGESTIVE
        defaultBodyDatumShouldBeFound("digestive.in=" + DEFAULT_DIGESTIVE + "," + UPDATED_DIGESTIVE);

        // Get all the bodyDatumList where digestive equals to UPDATED_DIGESTIVE
        defaultBodyDatumShouldNotBeFound("digestive.in=" + UPDATED_DIGESTIVE);
    }

    @Test
    @Transactional
    public void getAllBodyDataByDigestiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        bodyDatumRepository.saveAndFlush(bodyDatum);

        // Get all the bodyDatumList where digestive is not null
        defaultBodyDatumShouldBeFound("digestive.specified=true");

        // Get all the bodyDatumList where digestive is null
        defaultBodyDatumShouldNotBeFound("digestive.specified=false");
    }

    @Test
    @Transactional
    public void getAllBodyDataByTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        bodyDatumRepository.saveAndFlush(bodyDatum);

        // Get all the bodyDatumList where timestamp equals to DEFAULT_TIMESTAMP
        defaultBodyDatumShouldBeFound("timestamp.equals=" + DEFAULT_TIMESTAMP);

        // Get all the bodyDatumList where timestamp equals to UPDATED_TIMESTAMP
        defaultBodyDatumShouldNotBeFound("timestamp.equals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllBodyDataByTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        bodyDatumRepository.saveAndFlush(bodyDatum);

        // Get all the bodyDatumList where timestamp in DEFAULT_TIMESTAMP or UPDATED_TIMESTAMP
        defaultBodyDatumShouldBeFound("timestamp.in=" + DEFAULT_TIMESTAMP + "," + UPDATED_TIMESTAMP);

        // Get all the bodyDatumList where timestamp equals to UPDATED_TIMESTAMP
        defaultBodyDatumShouldNotBeFound("timestamp.in=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllBodyDataByTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        bodyDatumRepository.saveAndFlush(bodyDatum);

        // Get all the bodyDatumList where timestamp is not null
        defaultBodyDatumShouldBeFound("timestamp.specified=true");

        // Get all the bodyDatumList where timestamp is null
        defaultBodyDatumShouldNotBeFound("timestamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllBodyDataByTimestampIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bodyDatumRepository.saveAndFlush(bodyDatum);

        // Get all the bodyDatumList where timestamp greater than or equals to DEFAULT_TIMESTAMP
        defaultBodyDatumShouldBeFound("timestamp.greaterOrEqualThan=" + DEFAULT_TIMESTAMP);

        // Get all the bodyDatumList where timestamp greater than or equals to UPDATED_TIMESTAMP
        defaultBodyDatumShouldNotBeFound("timestamp.greaterOrEqualThan=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllBodyDataByTimestampIsLessThanSomething() throws Exception {
        // Initialize the database
        bodyDatumRepository.saveAndFlush(bodyDatum);

        // Get all the bodyDatumList where timestamp less than or equals to DEFAULT_TIMESTAMP
        defaultBodyDatumShouldNotBeFound("timestamp.lessThan=" + DEFAULT_TIMESTAMP);

        // Get all the bodyDatumList where timestamp less than or equals to UPDATED_TIMESTAMP
        defaultBodyDatumShouldBeFound("timestamp.lessThan=" + UPDATED_TIMESTAMP);
    }


    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultBodyDatumShouldBeFound(String filter) throws Exception {
        restBodyDatumMockMvc.perform(get("/api/body-data?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bodyDatum.getId().intValue())))
            .andExpect(jsonPath("$.[*].headache").value(hasItem(DEFAULT_HEADACHE.toString())))
            .andExpect(jsonPath("$.[*].digestive").value(hasItem(DEFAULT_DIGESTIVE.toString())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultBodyDatumShouldNotBeFound(String filter) throws Exception {
        restBodyDatumMockMvc.perform(get("/api/body-data?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingBodyDatum() throws Exception {
        // Get the bodyDatum
        restBodyDatumMockMvc.perform(get("/api/body-data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBodyDatum() throws Exception {
        // Initialize the database
        bodyDatumRepository.saveAndFlush(bodyDatum);
        int databaseSizeBeforeUpdate = bodyDatumRepository.findAll().size();

        // Update the bodyDatum
        BodyDatum updatedBodyDatum = bodyDatumRepository.findOne(bodyDatum.getId());
        updatedBodyDatum
            .setHeadache(UPDATED_HEADACHE)
            .setDigestive(UPDATED_DIGESTIVE)
            .setTimestamp(UPDATED_TIMESTAMP);
        BodyDatumDTO bodyDatumDTO = bodyDatumMapper.toDto(updatedBodyDatum);

        restBodyDatumMockMvc.perform(put("/api/body-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bodyDatumDTO)))
            .andExpect(status().isOk());

        // Validate the BodyDatum in the database
        List<BodyDatum> bodyDatumList = bodyDatumRepository.findAll();
        assertThat(bodyDatumList).hasSize(databaseSizeBeforeUpdate);
        BodyDatum testBodyDatum = bodyDatumList.get(bodyDatumList.size() - 1);
        assertThat(testBodyDatum.getHeadache()).isEqualTo(UPDATED_HEADACHE);
        assertThat(testBodyDatum.getDigestive()).isEqualTo(UPDATED_DIGESTIVE);
        assertThat(testBodyDatum.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void updateNonExistingBodyDatum() throws Exception {
        int databaseSizeBeforeUpdate = bodyDatumRepository.findAll().size();

        // Create the BodyDatum
        BodyDatumDTO bodyDatumDTO = bodyDatumMapper.toDto(bodyDatum);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBodyDatumMockMvc.perform(put("/api/body-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bodyDatumDTO)))
            .andExpect(status().isCreated());

        // Validate the BodyDatum in the database
        List<BodyDatum> bodyDatumList = bodyDatumRepository.findAll();
        assertThat(bodyDatumList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBodyDatum() throws Exception {
        // Initialize the database
        bodyDatumRepository.saveAndFlush(bodyDatum);
        int databaseSizeBeforeDelete = bodyDatumRepository.findAll().size();

        // Get the bodyDatum
        restBodyDatumMockMvc.perform(delete("/api/body-data/{id}", bodyDatum.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BodyDatum> bodyDatumList = bodyDatumRepository.findAll();
        assertThat(bodyDatumList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        BodyDatum bodyDatum1 = new BodyDatum();
        bodyDatum1.setId(1L);
        BodyDatum bodyDatum2 = new BodyDatum();
        bodyDatum2.setId(bodyDatum1.getId());
        assertThat(bodyDatum1).isEqualTo(bodyDatum2);
        bodyDatum2.setId(2L);
        assertThat(bodyDatum1).isNotEqualTo(bodyDatum2);
        bodyDatum1.setId(null);
        assertThat(bodyDatum1).isNotEqualTo(bodyDatum2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(bodyDatumMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(bodyDatumMapper.fromId(null)).isNull();
    }
}
