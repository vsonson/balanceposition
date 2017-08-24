package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.PathWay;
import com.balpos.app.repository.PathWayRepository;
import com.balpos.app.service.PathWayService;
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
 * Test class for the PathWayResource REST controller.
 *
 * @see PathWayResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class PathWayResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    @Autowired
    private PathWayRepository pathWayRepository;

    @Autowired
    private PathWayService pathWayService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPathWayMockMvc;

    private PathWay pathWay;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PathWayResource pathWayResource = new PathWayResource(pathWayService);
        this.restPathWayMockMvc = MockMvcBuilders.standaloneSetup(pathWayResource)
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
    public static PathWay createEntity(EntityManager em) {
        PathWay pathWay = new PathWay()
            .name(DEFAULT_NAME)
            .desc(DEFAULT_DESC);
        return pathWay;
    }

    @Before
    public void initTest() {
        pathWay = createEntity(em);
    }

    @Test
    @Transactional
    public void createPathWay() throws Exception {
        int databaseSizeBeforeCreate = pathWayRepository.findAll().size();

        // Create the PathWay
        restPathWayMockMvc.perform(post("/api/path-ways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pathWay)))
            .andExpect(status().isCreated());

        // Validate the PathWay in the database
        List<PathWay> pathWayList = pathWayRepository.findAll();
        assertThat(pathWayList).hasSize(databaseSizeBeforeCreate + 1);
        PathWay testPathWay = pathWayList.get(pathWayList.size() - 1);
        assertThat(testPathWay.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPathWay.getDesc()).isEqualTo(DEFAULT_DESC);
    }

    @Test
    @Transactional
    public void createPathWayWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pathWayRepository.findAll().size();

        // Create the PathWay with an existing ID
        pathWay.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPathWayMockMvc.perform(post("/api/path-ways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pathWay)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PathWay> pathWayList = pathWayRepository.findAll();
        assertThat(pathWayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pathWayRepository.findAll().size();
        // set the field null
        pathWay.setName(null);

        // Create the PathWay, which fails.

        restPathWayMockMvc.perform(post("/api/path-ways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pathWay)))
            .andExpect(status().isBadRequest());

        List<PathWay> pathWayList = pathWayRepository.findAll();
        assertThat(pathWayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPathWays() throws Exception {
        // Initialize the database
        pathWayRepository.saveAndFlush(pathWay);

        // Get all the pathWayList
        restPathWayMockMvc.perform(get("/api/path-ways?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pathWay.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())));
    }

    @Test
    @Transactional
    public void getPathWay() throws Exception {
        // Initialize the database
        pathWayRepository.saveAndFlush(pathWay);

        // Get the pathWay
        restPathWayMockMvc.perform(get("/api/path-ways/{id}", pathWay.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pathWay.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPathWay() throws Exception {
        // Get the pathWay
        restPathWayMockMvc.perform(get("/api/path-ways/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePathWay() throws Exception {
        // Initialize the database
        pathWayService.save(pathWay);

        int databaseSizeBeforeUpdate = pathWayRepository.findAll().size();

        // Update the pathWay
        PathWay updatedPathWay = pathWayRepository.findOne(pathWay.getId());
        updatedPathWay
            .name(UPDATED_NAME)
            .desc(UPDATED_DESC);

        restPathWayMockMvc.perform(put("/api/path-ways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPathWay)))
            .andExpect(status().isOk());

        // Validate the PathWay in the database
        List<PathWay> pathWayList = pathWayRepository.findAll();
        assertThat(pathWayList).hasSize(databaseSizeBeforeUpdate);
        PathWay testPathWay = pathWayList.get(pathWayList.size() - 1);
        assertThat(testPathWay.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPathWay.getDesc()).isEqualTo(UPDATED_DESC);
    }

    @Test
    @Transactional
    public void updateNonExistingPathWay() throws Exception {
        int databaseSizeBeforeUpdate = pathWayRepository.findAll().size();

        // Create the PathWay

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPathWayMockMvc.perform(put("/api/path-ways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pathWay)))
            .andExpect(status().isCreated());

        // Validate the PathWay in the database
        List<PathWay> pathWayList = pathWayRepository.findAll();
        assertThat(pathWayList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePathWay() throws Exception {
        // Initialize the database
        pathWayService.save(pathWay);

        int databaseSizeBeforeDelete = pathWayRepository.findAll().size();

        // Get the pathWay
        restPathWayMockMvc.perform(delete("/api/path-ways/{id}", pathWay.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PathWay> pathWayList = pathWayRepository.findAll();
        assertThat(pathWayList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PathWay.class);
        PathWay pathWay1 = new PathWay();
        pathWay1.setId(1L);
        PathWay pathWay2 = new PathWay();
        pathWay2.setId(pathWay1.getId());
        assertThat(pathWay1).isEqualTo(pathWay2);
        pathWay2.setId(2L);
        assertThat(pathWay1).isNotEqualTo(pathWay2);
        pathWay1.setId(null);
        assertThat(pathWay1).isNotEqualTo(pathWay2);
    }
}
