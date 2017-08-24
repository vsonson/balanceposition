package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.ThoughtOfDay;
import com.balpos.app.repository.ThoughtOfDayRepository;
import com.balpos.app.service.ThoughtOfDayService;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ThoughtOfDayResource REST controller.
 *
 * @see ThoughtOfDayResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class ThoughtOfDayResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_LONG_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_LONG_TEXT = "BBBBBBBBBB";

    @Autowired
    private ThoughtOfDayRepository thoughtOfDayRepository;

    @Autowired
    private ThoughtOfDayService thoughtOfDayService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restThoughtOfDayMockMvc;

    private ThoughtOfDay thoughtOfDay;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ThoughtOfDayResource thoughtOfDayResource = new ThoughtOfDayResource(thoughtOfDayService);
        this.restThoughtOfDayMockMvc = MockMvcBuilders.standaloneSetup(thoughtOfDayResource)
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
    public static ThoughtOfDay createEntity(EntityManager em) {
        ThoughtOfDay thoughtOfDay = new ThoughtOfDay()
            .date(DEFAULT_DATE)
            .title(DEFAULT_TITLE)
            .url(DEFAULT_URL)
            .shortText(DEFAULT_SHORT_TEXT)
            .longText(DEFAULT_LONG_TEXT);
        return thoughtOfDay;
    }

    @Before
    public void initTest() {
        thoughtOfDay = createEntity(em);
    }

    @Test
    @Transactional
    public void createThoughtOfDay() throws Exception {
        int databaseSizeBeforeCreate = thoughtOfDayRepository.findAll().size();

        // Create the ThoughtOfDay
        restThoughtOfDayMockMvc.perform(post("/api/thought-of-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(thoughtOfDay)))
            .andExpect(status().isCreated());

        // Validate the ThoughtOfDay in the database
        List<ThoughtOfDay> thoughtOfDayList = thoughtOfDayRepository.findAll();
        assertThat(thoughtOfDayList).hasSize(databaseSizeBeforeCreate + 1);
        ThoughtOfDay testThoughtOfDay = thoughtOfDayList.get(thoughtOfDayList.size() - 1);
        assertThat(testThoughtOfDay.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testThoughtOfDay.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testThoughtOfDay.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testThoughtOfDay.getShortText()).isEqualTo(DEFAULT_SHORT_TEXT);
        assertThat(testThoughtOfDay.getLongText()).isEqualTo(DEFAULT_LONG_TEXT);
    }

    @Test
    @Transactional
    public void createThoughtOfDayWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = thoughtOfDayRepository.findAll().size();

        // Create the ThoughtOfDay with an existing ID
        thoughtOfDay.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restThoughtOfDayMockMvc.perform(post("/api/thought-of-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(thoughtOfDay)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ThoughtOfDay> thoughtOfDayList = thoughtOfDayRepository.findAll();
        assertThat(thoughtOfDayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllThoughtOfDays() throws Exception {
        // Initialize the database
        thoughtOfDayRepository.saveAndFlush(thoughtOfDay);

        // Get all the thoughtOfDayList
        restThoughtOfDayMockMvc.perform(get("/api/thought-of-days?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(thoughtOfDay.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].shortText").value(hasItem(DEFAULT_SHORT_TEXT.toString())))
            .andExpect(jsonPath("$.[*].longText").value(hasItem(DEFAULT_LONG_TEXT.toString())));
    }

    @Test
    @Transactional
    public void getThoughtOfDay() throws Exception {
        // Initialize the database
        thoughtOfDayRepository.saveAndFlush(thoughtOfDay);

        // Get the thoughtOfDay
        restThoughtOfDayMockMvc.perform(get("/api/thought-of-days/{id}", thoughtOfDay.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(thoughtOfDay.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.shortText").value(DEFAULT_SHORT_TEXT.toString()))
            .andExpect(jsonPath("$.longText").value(DEFAULT_LONG_TEXT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingThoughtOfDay() throws Exception {
        // Get the thoughtOfDay
        restThoughtOfDayMockMvc.perform(get("/api/thought-of-days/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateThoughtOfDay() throws Exception {
        // Initialize the database
        thoughtOfDayService.save(thoughtOfDay);

        int databaseSizeBeforeUpdate = thoughtOfDayRepository.findAll().size();

        // Update the thoughtOfDay
        ThoughtOfDay updatedThoughtOfDay = thoughtOfDayRepository.findOne(thoughtOfDay.getId());
        updatedThoughtOfDay
            .date(UPDATED_DATE)
            .title(UPDATED_TITLE)
            .url(UPDATED_URL)
            .shortText(UPDATED_SHORT_TEXT)
            .longText(UPDATED_LONG_TEXT);

        restThoughtOfDayMockMvc.perform(put("/api/thought-of-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedThoughtOfDay)))
            .andExpect(status().isOk());

        // Validate the ThoughtOfDay in the database
        List<ThoughtOfDay> thoughtOfDayList = thoughtOfDayRepository.findAll();
        assertThat(thoughtOfDayList).hasSize(databaseSizeBeforeUpdate);
        ThoughtOfDay testThoughtOfDay = thoughtOfDayList.get(thoughtOfDayList.size() - 1);
        assertThat(testThoughtOfDay.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testThoughtOfDay.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testThoughtOfDay.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testThoughtOfDay.getShortText()).isEqualTo(UPDATED_SHORT_TEXT);
        assertThat(testThoughtOfDay.getLongText()).isEqualTo(UPDATED_LONG_TEXT);
    }

    @Test
    @Transactional
    public void updateNonExistingThoughtOfDay() throws Exception {
        int databaseSizeBeforeUpdate = thoughtOfDayRepository.findAll().size();

        // Create the ThoughtOfDay

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restThoughtOfDayMockMvc.perform(put("/api/thought-of-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(thoughtOfDay)))
            .andExpect(status().isCreated());

        // Validate the ThoughtOfDay in the database
        List<ThoughtOfDay> thoughtOfDayList = thoughtOfDayRepository.findAll();
        assertThat(thoughtOfDayList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteThoughtOfDay() throws Exception {
        // Initialize the database
        thoughtOfDayService.save(thoughtOfDay);

        int databaseSizeBeforeDelete = thoughtOfDayRepository.findAll().size();

        // Get the thoughtOfDay
        restThoughtOfDayMockMvc.perform(delete("/api/thought-of-days/{id}", thoughtOfDay.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ThoughtOfDay> thoughtOfDayList = thoughtOfDayRepository.findAll();
        assertThat(thoughtOfDayList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ThoughtOfDay.class);
        ThoughtOfDay thoughtOfDay1 = new ThoughtOfDay();
        thoughtOfDay1.setId(1L);
        ThoughtOfDay thoughtOfDay2 = new ThoughtOfDay();
        thoughtOfDay2.setId(thoughtOfDay1.getId());
        assertThat(thoughtOfDay1).isEqualTo(thoughtOfDay2);
        thoughtOfDay2.setId(2L);
        assertThat(thoughtOfDay1).isNotEqualTo(thoughtOfDay2);
        thoughtOfDay1.setId(null);
        assertThat(thoughtOfDay1).isNotEqualTo(thoughtOfDay2);
    }
}
