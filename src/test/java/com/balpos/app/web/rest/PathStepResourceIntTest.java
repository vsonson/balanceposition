package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.PathStep;
import com.balpos.app.repository.PathStepRepository;
import com.balpos.app.service.PathStepService;
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
 * Test class for the PathStepResource REST controller.
 *
 * @see PathStepResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class PathStepResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    @Autowired
    private PathStepRepository pathStepRepository;

    @Autowired
    private PathStepService pathStepService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPathStepMockMvc;

    private PathStep pathStep;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PathStepResource pathStepResource = new PathStepResource(pathStepService);
        this.restPathStepMockMvc = MockMvcBuilders.standaloneSetup(pathStepResource)
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
    public static PathStep createEntity(EntityManager em) {
        PathStep pathStep = new PathStep()
            .name(DEFAULT_NAME)
            .desc(DEFAULT_DESC);
        return pathStep;
    }

    @Before
    public void initTest() {
        pathStep = createEntity(em);
    }

    @Test
    @Transactional
    public void createPathStep() throws Exception {
        int databaseSizeBeforeCreate = pathStepRepository.findAll().size();

        // Create the PathStep
        restPathStepMockMvc.perform(post("/api/path-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pathStep)))
            .andExpect(status().isCreated());

        // Validate the PathStep in the database
        List<PathStep> pathStepList = pathStepRepository.findAll();
        assertThat(pathStepList).hasSize(databaseSizeBeforeCreate + 1);
        PathStep testPathStep = pathStepList.get(pathStepList.size() - 1);
        assertThat(testPathStep.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPathStep.getDesc()).isEqualTo(DEFAULT_DESC);
    }

    @Test
    @Transactional
    public void createPathStepWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pathStepRepository.findAll().size();

        // Create the PathStep with an existing ID
        pathStep.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPathStepMockMvc.perform(post("/api/path-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pathStep)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PathStep> pathStepList = pathStepRepository.findAll();
        assertThat(pathStepList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pathStepRepository.findAll().size();
        // set the field null
        pathStep.setName(null);

        // Create the PathStep, which fails.

        restPathStepMockMvc.perform(post("/api/path-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pathStep)))
            .andExpect(status().isBadRequest());

        List<PathStep> pathStepList = pathStepRepository.findAll();
        assertThat(pathStepList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPathSteps() throws Exception {
        // Initialize the database
        pathStepRepository.saveAndFlush(pathStep);

        // Get all the pathStepList
        restPathStepMockMvc.perform(get("/api/path-steps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pathStep.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())));
    }

    @Test
    @Transactional
    public void getPathStep() throws Exception {
        // Initialize the database
        pathStepRepository.saveAndFlush(pathStep);

        // Get the pathStep
        restPathStepMockMvc.perform(get("/api/path-steps/{id}", pathStep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pathStep.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPathStep() throws Exception {
        // Get the pathStep
        restPathStepMockMvc.perform(get("/api/path-steps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePathStep() throws Exception {
        // Initialize the database
        pathStepService.save(pathStep);

        int databaseSizeBeforeUpdate = pathStepRepository.findAll().size();

        // Update the pathStep
        PathStep updatedPathStep = pathStepRepository.findOne(pathStep.getId());
        updatedPathStep
            .name(UPDATED_NAME)
            .desc(UPDATED_DESC);

        restPathStepMockMvc.perform(put("/api/path-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPathStep)))
            .andExpect(status().isOk());

        // Validate the PathStep in the database
        List<PathStep> pathStepList = pathStepRepository.findAll();
        assertThat(pathStepList).hasSize(databaseSizeBeforeUpdate);
        PathStep testPathStep = pathStepList.get(pathStepList.size() - 1);
        assertThat(testPathStep.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPathStep.getDesc()).isEqualTo(UPDATED_DESC);
    }

    @Test
    @Transactional
    public void updateNonExistingPathStep() throws Exception {
        int databaseSizeBeforeUpdate = pathStepRepository.findAll().size();

        // Create the PathStep

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPathStepMockMvc.perform(put("/api/path-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pathStep)))
            .andExpect(status().isCreated());

        // Validate the PathStep in the database
        List<PathStep> pathStepList = pathStepRepository.findAll();
        assertThat(pathStepList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePathStep() throws Exception {
        // Initialize the database
        pathStepService.save(pathStep);

        int databaseSizeBeforeDelete = pathStepRepository.findAll().size();

        // Get the pathStep
        restPathStepMockMvc.perform(delete("/api/path-steps/{id}", pathStep.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PathStep> pathStepList = pathStepRepository.findAll();
        assertThat(pathStepList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PathStep.class);
        PathStep pathStep1 = new PathStep();
        pathStep1.setId(1L);
        PathStep pathStep2 = new PathStep();
        pathStep2.setId(pathStep1.getId());
        assertThat(pathStep1).isEqualTo(pathStep2);
        pathStep2.setId(2L);
        assertThat(pathStep1).isNotEqualTo(pathStep2);
        pathStep1.setId(null);
        assertThat(pathStep1).isNotEqualTo(pathStep2);
    }
}
