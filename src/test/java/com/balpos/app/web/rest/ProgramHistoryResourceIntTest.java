package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.ProgramHistory;
import com.balpos.app.repository.ProgramHistoryRepository;
import com.balpos.app.service.ProgramHistoryService;
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
 * Test class for the ProgramHistoryResource REST controller.
 *
 * @see ProgramHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class ProgramHistoryResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_RATING = "AAAAAAAAAA";
    private static final String UPDATED_RATING = "BBBBBBBBBB";

    private static final String DEFAULT_FEELING = "AAAAAAAAAA";
    private static final String UPDATED_FEELING = "BBBBBBBBBB";

    @Autowired
    private ProgramHistoryRepository programHistoryRepository;

    @Autowired
    private ProgramHistoryService programHistoryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProgramHistoryMockMvc;

    private ProgramHistory programHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProgramHistoryResource programHistoryResource = new ProgramHistoryResource(programHistoryService);
        this.restProgramHistoryMockMvc = MockMvcBuilders.standaloneSetup(programHistoryResource)
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
    public static ProgramHistory createEntity(EntityManager em) {
        ProgramHistory programHistory = new ProgramHistory()
            .date(DEFAULT_DATE)
            .rating(DEFAULT_RATING)
            .feeling(DEFAULT_FEELING);
        return programHistory;
    }

    @Before
    public void initTest() {
        programHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createProgramHistory() throws Exception {
        int databaseSizeBeforeCreate = programHistoryRepository.findAll().size();

        // Create the ProgramHistory
        restProgramHistoryMockMvc.perform(post("/api/program-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(programHistory)))
            .andExpect(status().isCreated());

        // Validate the ProgramHistory in the database
        List<ProgramHistory> programHistoryList = programHistoryRepository.findAll();
        assertThat(programHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        ProgramHistory testProgramHistory = programHistoryList.get(programHistoryList.size() - 1);
        assertThat(testProgramHistory.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testProgramHistory.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testProgramHistory.getFeeling()).isEqualTo(DEFAULT_FEELING);
    }

    @Test
    @Transactional
    public void createProgramHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = programHistoryRepository.findAll().size();

        // Create the ProgramHistory with an existing ID
        programHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgramHistoryMockMvc.perform(post("/api/program-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(programHistory)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ProgramHistory> programHistoryList = programHistoryRepository.findAll();
        assertThat(programHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProgramHistories() throws Exception {
        // Initialize the database
        programHistoryRepository.saveAndFlush(programHistory);

        // Get all the programHistoryList
        restProgramHistoryMockMvc.perform(get("/api/program-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(programHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.toString())))
            .andExpect(jsonPath("$.[*].feeling").value(hasItem(DEFAULT_FEELING.toString())));
    }

    @Test
    @Transactional
    public void getProgramHistory() throws Exception {
        // Initialize the database
        programHistoryRepository.saveAndFlush(programHistory);

        // Get the programHistory
        restProgramHistoryMockMvc.perform(get("/api/program-histories/{id}", programHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(programHistory.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.toString()))
            .andExpect(jsonPath("$.feeling").value(DEFAULT_FEELING.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProgramHistory() throws Exception {
        // Get the programHistory
        restProgramHistoryMockMvc.perform(get("/api/program-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProgramHistory() throws Exception {
        // Initialize the database
        programHistoryService.save(programHistory);

        int databaseSizeBeforeUpdate = programHistoryRepository.findAll().size();

        // Update the programHistory
        ProgramHistory updatedProgramHistory = programHistoryRepository.findOne(programHistory.getId());
        updatedProgramHistory
            .date(UPDATED_DATE)
            .rating(UPDATED_RATING)
            .feeling(UPDATED_FEELING);

        restProgramHistoryMockMvc.perform(put("/api/program-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProgramHistory)))
            .andExpect(status().isOk());

        // Validate the ProgramHistory in the database
        List<ProgramHistory> programHistoryList = programHistoryRepository.findAll();
        assertThat(programHistoryList).hasSize(databaseSizeBeforeUpdate);
        ProgramHistory testProgramHistory = programHistoryList.get(programHistoryList.size() - 1);
        assertThat(testProgramHistory.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testProgramHistory.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testProgramHistory.getFeeling()).isEqualTo(UPDATED_FEELING);
    }

    @Test
    @Transactional
    public void updateNonExistingProgramHistory() throws Exception {
        int databaseSizeBeforeUpdate = programHistoryRepository.findAll().size();

        // Create the ProgramHistory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProgramHistoryMockMvc.perform(put("/api/program-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(programHistory)))
            .andExpect(status().isCreated());

        // Validate the ProgramHistory in the database
        List<ProgramHistory> programHistoryList = programHistoryRepository.findAll();
        assertThat(programHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProgramHistory() throws Exception {
        // Initialize the database
        programHistoryService.save(programHistory);

        int databaseSizeBeforeDelete = programHistoryRepository.findAll().size();

        // Get the programHistory
        restProgramHistoryMockMvc.perform(delete("/api/program-histories/{id}", programHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProgramHistory> programHistoryList = programHistoryRepository.findAll();
        assertThat(programHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProgramHistory.class);
        ProgramHistory programHistory1 = new ProgramHistory();
        programHistory1.setId(1L);
        ProgramHistory programHistory2 = new ProgramHistory();
        programHistory2.setId(programHistory1.getId());
        assertThat(programHistory1).isEqualTo(programHistory2);
        programHistory2.setId(2L);
        assertThat(programHistory1).isNotEqualTo(programHistory2);
        programHistory1.setId(null);
        assertThat(programHistory1).isNotEqualTo(programHistory2);
    }
}
