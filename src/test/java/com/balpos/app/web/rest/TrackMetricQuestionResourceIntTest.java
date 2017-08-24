package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.TrackMetricQuestion;
import com.balpos.app.repository.TrackMetricQuestionRepository;
import com.balpos.app.service.TrackMetricQuestionService;
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
 * Test class for the TrackMetricQuestionResource REST controller.
 *
 * @see TrackMetricQuestionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class TrackMetricQuestionResourceIntTest {

    private static final String DEFAULT_QUESTION = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_MANDATORY = false;
    private static final Boolean UPDATED_IS_MANDATORY = true;

    @Autowired
    private TrackMetricQuestionRepository trackMetricQuestionRepository;

    @Autowired
    private TrackMetricQuestionService trackMetricQuestionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTrackMetricQuestionMockMvc;

    private TrackMetricQuestion trackMetricQuestion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TrackMetricQuestionResource trackMetricQuestionResource = new TrackMetricQuestionResource(trackMetricQuestionService);
        this.restTrackMetricQuestionMockMvc = MockMvcBuilders.standaloneSetup(trackMetricQuestionResource)
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
    public static TrackMetricQuestion createEntity(EntityManager em) {
        TrackMetricQuestion trackMetricQuestion = new TrackMetricQuestion()
            .question(DEFAULT_QUESTION)
            .isMandatory(DEFAULT_IS_MANDATORY);
        return trackMetricQuestion;
    }

    @Before
    public void initTest() {
        trackMetricQuestion = createEntity(em);
    }

    @Test
    @Transactional
    public void createTrackMetricQuestion() throws Exception {
        int databaseSizeBeforeCreate = trackMetricQuestionRepository.findAll().size();

        // Create the TrackMetricQuestion
        restTrackMetricQuestionMockMvc.perform(post("/api/track-metric-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trackMetricQuestion)))
            .andExpect(status().isCreated());

        // Validate the TrackMetricQuestion in the database
        List<TrackMetricQuestion> trackMetricQuestionList = trackMetricQuestionRepository.findAll();
        assertThat(trackMetricQuestionList).hasSize(databaseSizeBeforeCreate + 1);
        TrackMetricQuestion testTrackMetricQuestion = trackMetricQuestionList.get(trackMetricQuestionList.size() - 1);
        assertThat(testTrackMetricQuestion.getQuestion()).isEqualTo(DEFAULT_QUESTION);
        assertThat(testTrackMetricQuestion.isIsMandatory()).isEqualTo(DEFAULT_IS_MANDATORY);
    }

    @Test
    @Transactional
    public void createTrackMetricQuestionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = trackMetricQuestionRepository.findAll().size();

        // Create the TrackMetricQuestion with an existing ID
        trackMetricQuestion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrackMetricQuestionMockMvc.perform(post("/api/track-metric-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trackMetricQuestion)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TrackMetricQuestion> trackMetricQuestionList = trackMetricQuestionRepository.findAll();
        assertThat(trackMetricQuestionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkQuestionIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackMetricQuestionRepository.findAll().size();
        // set the field null
        trackMetricQuestion.setQuestion(null);

        // Create the TrackMetricQuestion, which fails.

        restTrackMetricQuestionMockMvc.perform(post("/api/track-metric-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trackMetricQuestion)))
            .andExpect(status().isBadRequest());

        List<TrackMetricQuestion> trackMetricQuestionList = trackMetricQuestionRepository.findAll();
        assertThat(trackMetricQuestionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTrackMetricQuestions() throws Exception {
        // Initialize the database
        trackMetricQuestionRepository.saveAndFlush(trackMetricQuestion);

        // Get all the trackMetricQuestionList
        restTrackMetricQuestionMockMvc.perform(get("/api/track-metric-questions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trackMetricQuestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION.toString())))
            .andExpect(jsonPath("$.[*].isMandatory").value(hasItem(DEFAULT_IS_MANDATORY.booleanValue())));
    }

    @Test
    @Transactional
    public void getTrackMetricQuestion() throws Exception {
        // Initialize the database
        trackMetricQuestionRepository.saveAndFlush(trackMetricQuestion);

        // Get the trackMetricQuestion
        restTrackMetricQuestionMockMvc.perform(get("/api/track-metric-questions/{id}", trackMetricQuestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(trackMetricQuestion.getId().intValue()))
            .andExpect(jsonPath("$.question").value(DEFAULT_QUESTION.toString()))
            .andExpect(jsonPath("$.isMandatory").value(DEFAULT_IS_MANDATORY.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTrackMetricQuestion() throws Exception {
        // Get the trackMetricQuestion
        restTrackMetricQuestionMockMvc.perform(get("/api/track-metric-questions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrackMetricQuestion() throws Exception {
        // Initialize the database
        trackMetricQuestionService.save(trackMetricQuestion);

        int databaseSizeBeforeUpdate = trackMetricQuestionRepository.findAll().size();

        // Update the trackMetricQuestion
        TrackMetricQuestion updatedTrackMetricQuestion = trackMetricQuestionRepository.findOne(trackMetricQuestion.getId());
        updatedTrackMetricQuestion
            .question(UPDATED_QUESTION)
            .isMandatory(UPDATED_IS_MANDATORY);

        restTrackMetricQuestionMockMvc.perform(put("/api/track-metric-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTrackMetricQuestion)))
            .andExpect(status().isOk());

        // Validate the TrackMetricQuestion in the database
        List<TrackMetricQuestion> trackMetricQuestionList = trackMetricQuestionRepository.findAll();
        assertThat(trackMetricQuestionList).hasSize(databaseSizeBeforeUpdate);
        TrackMetricQuestion testTrackMetricQuestion = trackMetricQuestionList.get(trackMetricQuestionList.size() - 1);
        assertThat(testTrackMetricQuestion.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testTrackMetricQuestion.isIsMandatory()).isEqualTo(UPDATED_IS_MANDATORY);
    }

    @Test
    @Transactional
    public void updateNonExistingTrackMetricQuestion() throws Exception {
        int databaseSizeBeforeUpdate = trackMetricQuestionRepository.findAll().size();

        // Create the TrackMetricQuestion

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTrackMetricQuestionMockMvc.perform(put("/api/track-metric-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trackMetricQuestion)))
            .andExpect(status().isCreated());

        // Validate the TrackMetricQuestion in the database
        List<TrackMetricQuestion> trackMetricQuestionList = trackMetricQuestionRepository.findAll();
        assertThat(trackMetricQuestionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTrackMetricQuestion() throws Exception {
        // Initialize the database
        trackMetricQuestionService.save(trackMetricQuestion);

        int databaseSizeBeforeDelete = trackMetricQuestionRepository.findAll().size();

        // Get the trackMetricQuestion
        restTrackMetricQuestionMockMvc.perform(delete("/api/track-metric-questions/{id}", trackMetricQuestion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TrackMetricQuestion> trackMetricQuestionList = trackMetricQuestionRepository.findAll();
        assertThat(trackMetricQuestionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrackMetricQuestion.class);
        TrackMetricQuestion trackMetricQuestion1 = new TrackMetricQuestion();
        trackMetricQuestion1.setId(1L);
        TrackMetricQuestion trackMetricQuestion2 = new TrackMetricQuestion();
        trackMetricQuestion2.setId(trackMetricQuestion1.getId());
        assertThat(trackMetricQuestion1).isEqualTo(trackMetricQuestion2);
        trackMetricQuestion2.setId(2L);
        assertThat(trackMetricQuestion1).isNotEqualTo(trackMetricQuestion2);
        trackMetricQuestion1.setId(null);
        assertThat(trackMetricQuestion1).isNotEqualTo(trackMetricQuestion2);
    }
}
