package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.QuoteOfTheDay;
import com.balpos.app.repository.QuoteOfTheDayRepository;
import com.balpos.app.service.QuoteOfTheDayService;
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
 * Test class for the QuoteOfTheDayResource REST controller.
 *
 * @see QuoteOfTheDayResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class QuoteOfTheDayResourceIntTest {

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    private static final String DEFAULT_QUOTE_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_QUOTE_TEXT = "BBBBBBBBBB";

    @Autowired
    private QuoteOfTheDayRepository quoteOfTheDayRepository;

    @Autowired
    private QuoteOfTheDayService quoteOfTheDayService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restQuoteOfTheDayMockMvc;

    private QuoteOfTheDay quoteOfTheDay;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QuoteOfTheDayResource quoteOfTheDayResource = new QuoteOfTheDayResource(quoteOfTheDayService);
        this.restQuoteOfTheDayMockMvc = MockMvcBuilders.standaloneSetup(quoteOfTheDayResource)
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
    public static QuoteOfTheDay createEntity(EntityManager em) {
        QuoteOfTheDay quoteOfTheDay = new QuoteOfTheDay()
            .author(DEFAULT_AUTHOR)
            .quoteText(DEFAULT_QUOTE_TEXT);
        return quoteOfTheDay;
    }

    @Before
    public void initTest() {
        quoteOfTheDay = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuoteOfTheDay() throws Exception {
        int databaseSizeBeforeCreate = quoteOfTheDayRepository.findAll().size();

        // Create the QuoteOfTheDay
        restQuoteOfTheDayMockMvc.perform(post("/api/quote-of-the-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quoteOfTheDay)))
            .andExpect(status().isCreated());

        // Validate the QuoteOfTheDay in the database
        List<QuoteOfTheDay> quoteOfTheDayList = quoteOfTheDayRepository.findAll();
        assertThat(quoteOfTheDayList).hasSize(databaseSizeBeforeCreate + 1);
        QuoteOfTheDay testQuoteOfTheDay = quoteOfTheDayList.get(quoteOfTheDayList.size() - 1);
        assertThat(testQuoteOfTheDay.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testQuoteOfTheDay.getQuoteText()).isEqualTo(DEFAULT_QUOTE_TEXT);
    }

    @Test
    @Transactional
    public void createQuoteOfTheDayWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = quoteOfTheDayRepository.findAll().size();

        // Create the QuoteOfTheDay with an existing ID
        quoteOfTheDay.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuoteOfTheDayMockMvc.perform(post("/api/quote-of-the-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quoteOfTheDay)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<QuoteOfTheDay> quoteOfTheDayList = quoteOfTheDayRepository.findAll();
        assertThat(quoteOfTheDayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllQuoteOfTheDays() throws Exception {
        // Initialize the database
        quoteOfTheDayRepository.saveAndFlush(quoteOfTheDay);

        // Get all the quoteOfTheDayList
        restQuoteOfTheDayMockMvc.perform(get("/api/quote-of-the-days?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quoteOfTheDay.getId().intValue())))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR.toString())))
            .andExpect(jsonPath("$.[*].quoteText").value(hasItem(DEFAULT_QUOTE_TEXT.toString())));
    }

    @Test
    @Transactional
    public void getQuoteOfTheDay() throws Exception {
        // Initialize the database
        quoteOfTheDayRepository.saveAndFlush(quoteOfTheDay);

        // Get the quoteOfTheDay
        restQuoteOfTheDayMockMvc.perform(get("/api/quote-of-the-days/{id}", quoteOfTheDay.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(quoteOfTheDay.getId().intValue()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR.toString()))
            .andExpect(jsonPath("$.quoteText").value(DEFAULT_QUOTE_TEXT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingQuoteOfTheDay() throws Exception {
        // Get the quoteOfTheDay
        restQuoteOfTheDayMockMvc.perform(get("/api/quote-of-the-days/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuoteOfTheDay() throws Exception {
        // Initialize the database
        quoteOfTheDayService.save(quoteOfTheDay);

        int databaseSizeBeforeUpdate = quoteOfTheDayRepository.findAll().size();

        // Update the quoteOfTheDay
        QuoteOfTheDay updatedQuoteOfTheDay = quoteOfTheDayRepository.findOne(quoteOfTheDay.getId());
        updatedQuoteOfTheDay
            .author(UPDATED_AUTHOR)
            .quoteText(UPDATED_QUOTE_TEXT);

        restQuoteOfTheDayMockMvc.perform(put("/api/quote-of-the-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedQuoteOfTheDay)))
            .andExpect(status().isOk());

        // Validate the QuoteOfTheDay in the database
        List<QuoteOfTheDay> quoteOfTheDayList = quoteOfTheDayRepository.findAll();
        assertThat(quoteOfTheDayList).hasSize(databaseSizeBeforeUpdate);
        QuoteOfTheDay testQuoteOfTheDay = quoteOfTheDayList.get(quoteOfTheDayList.size() - 1);
        assertThat(testQuoteOfTheDay.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testQuoteOfTheDay.getQuoteText()).isEqualTo(UPDATED_QUOTE_TEXT);
    }

    @Test
    @Transactional
    public void updateNonExistingQuoteOfTheDay() throws Exception {
        int databaseSizeBeforeUpdate = quoteOfTheDayRepository.findAll().size();

        // Create the QuoteOfTheDay

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restQuoteOfTheDayMockMvc.perform(put("/api/quote-of-the-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quoteOfTheDay)))
            .andExpect(status().isCreated());

        // Validate the QuoteOfTheDay in the database
        List<QuoteOfTheDay> quoteOfTheDayList = quoteOfTheDayRepository.findAll();
        assertThat(quoteOfTheDayList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteQuoteOfTheDay() throws Exception {
        // Initialize the database
        quoteOfTheDayService.save(quoteOfTheDay);

        int databaseSizeBeforeDelete = quoteOfTheDayRepository.findAll().size();

        // Get the quoteOfTheDay
        restQuoteOfTheDayMockMvc.perform(delete("/api/quote-of-the-days/{id}", quoteOfTheDay.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<QuoteOfTheDay> quoteOfTheDayList = quoteOfTheDayRepository.findAll();
        assertThat(quoteOfTheDayList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuoteOfTheDay.class);
        QuoteOfTheDay quoteOfTheDay1 = new QuoteOfTheDay();
        quoteOfTheDay1.setId(1L);
        QuoteOfTheDay quoteOfTheDay2 = new QuoteOfTheDay();
        quoteOfTheDay2.setId(quoteOfTheDay1.getId());
        assertThat(quoteOfTheDay1).isEqualTo(quoteOfTheDay2);
        quoteOfTheDay2.setId(2L);
        assertThat(quoteOfTheDay1).isNotEqualTo(quoteOfTheDay2);
        quoteOfTheDay1.setId(null);
        assertThat(quoteOfTheDay1).isNotEqualTo(quoteOfTheDay2);
    }
}
