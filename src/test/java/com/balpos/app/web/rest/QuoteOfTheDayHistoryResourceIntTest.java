package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.QuoteOfTheDayHistory;
import com.balpos.app.repository.QuoteOfTheDayHistoryRepository;
import com.balpos.app.service.QuoteOfTheDayHistoryService;
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
 * Test class for the QuoteOfTheDayHistoryResource REST controller.
 *
 * @see QuoteOfTheDayHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class QuoteOfTheDayHistoryResourceIntTest {

    @Autowired
    private QuoteOfTheDayHistoryRepository quoteOfTheDayHistoryRepository;

    @Autowired
    private QuoteOfTheDayHistoryService quoteOfTheDayHistoryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restQuoteOfTheDayHistoryMockMvc;

    private QuoteOfTheDayHistory quoteOfTheDayHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QuoteOfTheDayHistoryResource quoteOfTheDayHistoryResource = new QuoteOfTheDayHistoryResource(quoteOfTheDayHistoryService);
        this.restQuoteOfTheDayHistoryMockMvc = MockMvcBuilders.standaloneSetup(quoteOfTheDayHistoryResource)
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
    public static QuoteOfTheDayHistory createEntity(EntityManager em) {
        QuoteOfTheDayHistory quoteOfTheDayHistory = new QuoteOfTheDayHistory();
        return quoteOfTheDayHistory;
    }

    @Before
    public void initTest() {
        quoteOfTheDayHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuoteOfTheDayHistory() throws Exception {
        int databaseSizeBeforeCreate = quoteOfTheDayHistoryRepository.findAll().size();

        // Create the QuoteOfTheDayHistory
        restQuoteOfTheDayHistoryMockMvc.perform(post("/api/quote-of-the-day-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quoteOfTheDayHistory)))
            .andExpect(status().isCreated());

        // Validate the QuoteOfTheDayHistory in the database
        List<QuoteOfTheDayHistory> quoteOfTheDayHistoryList = quoteOfTheDayHistoryRepository.findAll();
        assertThat(quoteOfTheDayHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        QuoteOfTheDayHistory testQuoteOfTheDayHistory = quoteOfTheDayHistoryList.get(quoteOfTheDayHistoryList.size() - 1);
    }

    @Test
    @Transactional
    public void createQuoteOfTheDayHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = quoteOfTheDayHistoryRepository.findAll().size();

        // Create the QuoteOfTheDayHistory with an existing ID
        quoteOfTheDayHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuoteOfTheDayHistoryMockMvc.perform(post("/api/quote-of-the-day-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quoteOfTheDayHistory)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<QuoteOfTheDayHistory> quoteOfTheDayHistoryList = quoteOfTheDayHistoryRepository.findAll();
        assertThat(quoteOfTheDayHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllQuoteOfTheDayHistories() throws Exception {
        // Initialize the database
        quoteOfTheDayHistoryRepository.saveAndFlush(quoteOfTheDayHistory);

        // Get all the quoteOfTheDayHistoryList
        restQuoteOfTheDayHistoryMockMvc.perform(get("/api/quote-of-the-day-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quoteOfTheDayHistory.getId().intValue())));
    }

    @Test
    @Transactional
    public void getQuoteOfTheDayHistory() throws Exception {
        // Initialize the database
        quoteOfTheDayHistoryRepository.saveAndFlush(quoteOfTheDayHistory);

        // Get the quoteOfTheDayHistory
        restQuoteOfTheDayHistoryMockMvc.perform(get("/api/quote-of-the-day-histories/{id}", quoteOfTheDayHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(quoteOfTheDayHistory.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingQuoteOfTheDayHistory() throws Exception {
        // Get the quoteOfTheDayHistory
        restQuoteOfTheDayHistoryMockMvc.perform(get("/api/quote-of-the-day-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuoteOfTheDayHistory() throws Exception {
        // Initialize the database
        quoteOfTheDayHistoryService.save(quoteOfTheDayHistory);

        int databaseSizeBeforeUpdate = quoteOfTheDayHistoryRepository.findAll().size();

        // Update the quoteOfTheDayHistory
        QuoteOfTheDayHistory updatedQuoteOfTheDayHistory = quoteOfTheDayHistoryRepository.findOne(quoteOfTheDayHistory.getId());

        restQuoteOfTheDayHistoryMockMvc.perform(put("/api/quote-of-the-day-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedQuoteOfTheDayHistory)))
            .andExpect(status().isOk());

        // Validate the QuoteOfTheDayHistory in the database
        List<QuoteOfTheDayHistory> quoteOfTheDayHistoryList = quoteOfTheDayHistoryRepository.findAll();
        assertThat(quoteOfTheDayHistoryList).hasSize(databaseSizeBeforeUpdate);
        QuoteOfTheDayHistory testQuoteOfTheDayHistory = quoteOfTheDayHistoryList.get(quoteOfTheDayHistoryList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingQuoteOfTheDayHistory() throws Exception {
        int databaseSizeBeforeUpdate = quoteOfTheDayHistoryRepository.findAll().size();

        // Create the QuoteOfTheDayHistory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restQuoteOfTheDayHistoryMockMvc.perform(put("/api/quote-of-the-day-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quoteOfTheDayHistory)))
            .andExpect(status().isCreated());

        // Validate the QuoteOfTheDayHistory in the database
        List<QuoteOfTheDayHistory> quoteOfTheDayHistoryList = quoteOfTheDayHistoryRepository.findAll();
        assertThat(quoteOfTheDayHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteQuoteOfTheDayHistory() throws Exception {
        // Initialize the database
        quoteOfTheDayHistoryService.save(quoteOfTheDayHistory);

        int databaseSizeBeforeDelete = quoteOfTheDayHistoryRepository.findAll().size();

        // Get the quoteOfTheDayHistory
        restQuoteOfTheDayHistoryMockMvc.perform(delete("/api/quote-of-the-day-histories/{id}", quoteOfTheDayHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<QuoteOfTheDayHistory> quoteOfTheDayHistoryList = quoteOfTheDayHistoryRepository.findAll();
        assertThat(quoteOfTheDayHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuoteOfTheDayHistory.class);
        QuoteOfTheDayHistory quoteOfTheDayHistory1 = new QuoteOfTheDayHistory();
        quoteOfTheDayHistory1.setId(1L);
        QuoteOfTheDayHistory quoteOfTheDayHistory2 = new QuoteOfTheDayHistory();
        quoteOfTheDayHistory2.setId(quoteOfTheDayHistory1.getId());
        assertThat(quoteOfTheDayHistory1).isEqualTo(quoteOfTheDayHistory2);
        quoteOfTheDayHistory2.setId(2L);
        assertThat(quoteOfTheDayHistory1).isNotEqualTo(quoteOfTheDayHistory2);
        quoteOfTheDayHistory1.setId(null);
        assertThat(quoteOfTheDayHistory1).isNotEqualTo(quoteOfTheDayHistory2);
    }
}
