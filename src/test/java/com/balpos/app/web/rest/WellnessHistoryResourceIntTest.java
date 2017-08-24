package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.WellnessHistory;
import com.balpos.app.repository.WellnessHistoryRepository;
import com.balpos.app.service.WellnessHistoryService;
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
 * Test class for the WellnessHistoryResource REST controller.
 *
 * @see WellnessHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class WellnessHistoryResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_WELLNESSSCORE = 1;
    private static final Integer UPDATED_WELLNESSSCORE = 2;

    @Autowired
    private WellnessHistoryRepository wellnessHistoryRepository;

    @Autowired
    private WellnessHistoryService wellnessHistoryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWellnessHistoryMockMvc;

    private WellnessHistory wellnessHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WellnessHistoryResource wellnessHistoryResource = new WellnessHistoryResource(wellnessHistoryService);
        this.restWellnessHistoryMockMvc = MockMvcBuilders.standaloneSetup(wellnessHistoryResource)
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
    public static WellnessHistory createEntity(EntityManager em) {
        WellnessHistory wellnessHistory = new WellnessHistory()
            .date(DEFAULT_DATE)
            .wellnessscore(DEFAULT_WELLNESSSCORE);
        return wellnessHistory;
    }

    @Before
    public void initTest() {
        wellnessHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createWellnessHistory() throws Exception {
        int databaseSizeBeforeCreate = wellnessHistoryRepository.findAll().size();

        // Create the WellnessHistory
        restWellnessHistoryMockMvc.perform(post("/api/wellness-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wellnessHistory)))
            .andExpect(status().isCreated());

        // Validate the WellnessHistory in the database
        List<WellnessHistory> wellnessHistoryList = wellnessHistoryRepository.findAll();
        assertThat(wellnessHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        WellnessHistory testWellnessHistory = wellnessHistoryList.get(wellnessHistoryList.size() - 1);
        assertThat(testWellnessHistory.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testWellnessHistory.getWellnessscore()).isEqualTo(DEFAULT_WELLNESSSCORE);
    }

    @Test
    @Transactional
    public void createWellnessHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wellnessHistoryRepository.findAll().size();

        // Create the WellnessHistory with an existing ID
        wellnessHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWellnessHistoryMockMvc.perform(post("/api/wellness-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wellnessHistory)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<WellnessHistory> wellnessHistoryList = wellnessHistoryRepository.findAll();
        assertThat(wellnessHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWellnessHistories() throws Exception {
        // Initialize the database
        wellnessHistoryRepository.saveAndFlush(wellnessHistory);

        // Get all the wellnessHistoryList
        restWellnessHistoryMockMvc.perform(get("/api/wellness-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wellnessHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].wellnessscore").value(hasItem(DEFAULT_WELLNESSSCORE)));
    }

    @Test
    @Transactional
    public void getWellnessHistory() throws Exception {
        // Initialize the database
        wellnessHistoryRepository.saveAndFlush(wellnessHistory);

        // Get the wellnessHistory
        restWellnessHistoryMockMvc.perform(get("/api/wellness-histories/{id}", wellnessHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wellnessHistory.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.wellnessscore").value(DEFAULT_WELLNESSSCORE));
    }

    @Test
    @Transactional
    public void getNonExistingWellnessHistory() throws Exception {
        // Get the wellnessHistory
        restWellnessHistoryMockMvc.perform(get("/api/wellness-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWellnessHistory() throws Exception {
        // Initialize the database
        wellnessHistoryService.save(wellnessHistory);

        int databaseSizeBeforeUpdate = wellnessHistoryRepository.findAll().size();

        // Update the wellnessHistory
        WellnessHistory updatedWellnessHistory = wellnessHistoryRepository.findOne(wellnessHistory.getId());
        updatedWellnessHistory
            .date(UPDATED_DATE)
            .wellnessscore(UPDATED_WELLNESSSCORE);

        restWellnessHistoryMockMvc.perform(put("/api/wellness-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWellnessHistory)))
            .andExpect(status().isOk());

        // Validate the WellnessHistory in the database
        List<WellnessHistory> wellnessHistoryList = wellnessHistoryRepository.findAll();
        assertThat(wellnessHistoryList).hasSize(databaseSizeBeforeUpdate);
        WellnessHistory testWellnessHistory = wellnessHistoryList.get(wellnessHistoryList.size() - 1);
        assertThat(testWellnessHistory.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testWellnessHistory.getWellnessscore()).isEqualTo(UPDATED_WELLNESSSCORE);
    }

    @Test
    @Transactional
    public void updateNonExistingWellnessHistory() throws Exception {
        int databaseSizeBeforeUpdate = wellnessHistoryRepository.findAll().size();

        // Create the WellnessHistory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWellnessHistoryMockMvc.perform(put("/api/wellness-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wellnessHistory)))
            .andExpect(status().isCreated());

        // Validate the WellnessHistory in the database
        List<WellnessHistory> wellnessHistoryList = wellnessHistoryRepository.findAll();
        assertThat(wellnessHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWellnessHistory() throws Exception {
        // Initialize the database
        wellnessHistoryService.save(wellnessHistory);

        int databaseSizeBeforeDelete = wellnessHistoryRepository.findAll().size();

        // Get the wellnessHistory
        restWellnessHistoryMockMvc.perform(delete("/api/wellness-histories/{id}", wellnessHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WellnessHistory> wellnessHistoryList = wellnessHistoryRepository.findAll();
        assertThat(wellnessHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WellnessHistory.class);
        WellnessHistory wellnessHistory1 = new WellnessHistory();
        wellnessHistory1.setId(1L);
        WellnessHistory wellnessHistory2 = new WellnessHistory();
        wellnessHistory2.setId(wellnessHistory1.getId());
        assertThat(wellnessHistory1).isEqualTo(wellnessHistory2);
        wellnessHistory2.setId(2L);
        assertThat(wellnessHistory1).isNotEqualTo(wellnessHistory2);
        wellnessHistory1.setId(null);
        assertThat(wellnessHistory1).isNotEqualTo(wellnessHistory2);
    }
}
