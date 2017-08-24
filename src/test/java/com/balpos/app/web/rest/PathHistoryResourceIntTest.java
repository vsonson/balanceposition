package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.PathHistory;
import com.balpos.app.repository.PathHistoryRepository;
import com.balpos.app.service.PathHistoryService;
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
 * Test class for the PathHistoryResource REST controller.
 *
 * @see PathHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class PathHistoryResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_COMPLETED = false;
    private static final Boolean UPDATED_IS_COMPLETED = true;

    @Autowired
    private PathHistoryRepository pathHistoryRepository;

    @Autowired
    private PathHistoryService pathHistoryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPathHistoryMockMvc;

    private PathHistory pathHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PathHistoryResource pathHistoryResource = new PathHistoryResource(pathHistoryService);
        this.restPathHistoryMockMvc = MockMvcBuilders.standaloneSetup(pathHistoryResource)
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
    public static PathHistory createEntity(EntityManager em) {
        PathHistory pathHistory = new PathHistory()
            .date(DEFAULT_DATE)
            .isCompleted(DEFAULT_IS_COMPLETED);
        return pathHistory;
    }

    @Before
    public void initTest() {
        pathHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createPathHistory() throws Exception {
        int databaseSizeBeforeCreate = pathHistoryRepository.findAll().size();

        // Create the PathHistory
        restPathHistoryMockMvc.perform(post("/api/path-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pathHistory)))
            .andExpect(status().isCreated());

        // Validate the PathHistory in the database
        List<PathHistory> pathHistoryList = pathHistoryRepository.findAll();
        assertThat(pathHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        PathHistory testPathHistory = pathHistoryList.get(pathHistoryList.size() - 1);
        assertThat(testPathHistory.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPathHistory.isIsCompleted()).isEqualTo(DEFAULT_IS_COMPLETED);
    }

    @Test
    @Transactional
    public void createPathHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pathHistoryRepository.findAll().size();

        // Create the PathHistory with an existing ID
        pathHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPathHistoryMockMvc.perform(post("/api/path-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pathHistory)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PathHistory> pathHistoryList = pathHistoryRepository.findAll();
        assertThat(pathHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPathHistories() throws Exception {
        // Initialize the database
        pathHistoryRepository.saveAndFlush(pathHistory);

        // Get all the pathHistoryList
        restPathHistoryMockMvc.perform(get("/api/path-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pathHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].isCompleted").value(hasItem(DEFAULT_IS_COMPLETED.booleanValue())));
    }

    @Test
    @Transactional
    public void getPathHistory() throws Exception {
        // Initialize the database
        pathHistoryRepository.saveAndFlush(pathHistory);

        // Get the pathHistory
        restPathHistoryMockMvc.perform(get("/api/path-histories/{id}", pathHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pathHistory.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.isCompleted").value(DEFAULT_IS_COMPLETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPathHistory() throws Exception {
        // Get the pathHistory
        restPathHistoryMockMvc.perform(get("/api/path-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePathHistory() throws Exception {
        // Initialize the database
        pathHistoryService.save(pathHistory);

        int databaseSizeBeforeUpdate = pathHistoryRepository.findAll().size();

        // Update the pathHistory
        PathHistory updatedPathHistory = pathHistoryRepository.findOne(pathHistory.getId());
        updatedPathHistory
            .date(UPDATED_DATE)
            .isCompleted(UPDATED_IS_COMPLETED);

        restPathHistoryMockMvc.perform(put("/api/path-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPathHistory)))
            .andExpect(status().isOk());

        // Validate the PathHistory in the database
        List<PathHistory> pathHistoryList = pathHistoryRepository.findAll();
        assertThat(pathHistoryList).hasSize(databaseSizeBeforeUpdate);
        PathHistory testPathHistory = pathHistoryList.get(pathHistoryList.size() - 1);
        assertThat(testPathHistory.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPathHistory.isIsCompleted()).isEqualTo(UPDATED_IS_COMPLETED);
    }

    @Test
    @Transactional
    public void updateNonExistingPathHistory() throws Exception {
        int databaseSizeBeforeUpdate = pathHistoryRepository.findAll().size();

        // Create the PathHistory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPathHistoryMockMvc.perform(put("/api/path-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pathHistory)))
            .andExpect(status().isCreated());

        // Validate the PathHistory in the database
        List<PathHistory> pathHistoryList = pathHistoryRepository.findAll();
        assertThat(pathHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePathHistory() throws Exception {
        // Initialize the database
        pathHistoryService.save(pathHistory);

        int databaseSizeBeforeDelete = pathHistoryRepository.findAll().size();

        // Get the pathHistory
        restPathHistoryMockMvc.perform(delete("/api/path-histories/{id}", pathHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PathHistory> pathHistoryList = pathHistoryRepository.findAll();
        assertThat(pathHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PathHistory.class);
        PathHistory pathHistory1 = new PathHistory();
        pathHistory1.setId(1L);
        PathHistory pathHistory2 = new PathHistory();
        pathHistory2.setId(pathHistory1.getId());
        assertThat(pathHistory1).isEqualTo(pathHistory2);
        pathHistory2.setId(2L);
        assertThat(pathHistory1).isNotEqualTo(pathHistory2);
        pathHistory1.setId(null);
        assertThat(pathHistory1).isNotEqualTo(pathHistory2);
    }
}
