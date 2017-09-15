package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.TrackMetric;
import com.balpos.app.repository.TrackMetricRepository;
import com.balpos.app.service.TrackMetricService;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TrackMetricResource REST controller.
 *
 * @see TrackMetricResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class TrackMetricResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TRACK_ICON = "AAAAAAAAAA";
    private static final String UPDATED_TRACK_ICON = "BBBBBBBBBB";

    private static final byte[] DEFAULT_INFO_BUBBLE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_INFO_BUBBLE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_INFO_BUBBLE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_INFO_BUBBLE_CONTENT_TYPE = "image/png";

    @Autowired
    private TrackMetricRepository trackMetricRepository;

    @Autowired
    private TrackMetricService trackMetricService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTrackMetricMockMvc;

    private TrackMetric trackMetric;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TrackMetricResource trackMetricResource = new TrackMetricResource(trackMetricService);
        this.restTrackMetricMockMvc = MockMvcBuilders.standaloneSetup(trackMetricResource)
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
    public static TrackMetric createEntity(EntityManager em) {
        TrackMetric trackMetric = new TrackMetric()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .trackIcon(DEFAULT_TRACK_ICON)
            .infoBubble(DEFAULT_INFO_BUBBLE)
            .infoBubbleContentType(DEFAULT_INFO_BUBBLE_CONTENT_TYPE);
        return trackMetric;
    }

    @Before
    public void initTest() {
        trackMetric = createEntity(em);
    }

    @Test
    @Transactional
    public void createTrackMetric() throws Exception {
        int databaseSizeBeforeCreate = trackMetricRepository.findAll().size();

        // Create the TrackMetric
        restTrackMetricMockMvc.perform(post("/api/track-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trackMetric)))
            .andExpect(status().isCreated());

        // Validate the TrackMetric in the database
        List<TrackMetric> trackMetricList = trackMetricRepository.findAll();
        assertThat(trackMetricList).hasSize(databaseSizeBeforeCreate + 1);
        TrackMetric testTrackMetric = trackMetricList.get(trackMetricList.size() - 1);
        assertThat(testTrackMetric.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTrackMetric.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTrackMetric.getTrackIcon()).isEqualTo(DEFAULT_TRACK_ICON);
        assertThat(testTrackMetric.getInfoBubble()).isEqualTo(DEFAULT_INFO_BUBBLE);
        assertThat(testTrackMetric.getInfoBubbleContentType()).isEqualTo(DEFAULT_INFO_BUBBLE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createTrackMetricWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = trackMetricRepository.findAll().size();

        // Create the TrackMetric with an existing ID
        trackMetric.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrackMetricMockMvc.perform(post("/api/track-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trackMetric)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TrackMetric> trackMetricList = trackMetricRepository.findAll();
        assertThat(trackMetricList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackMetricRepository.findAll().size();
        // set the field null
        trackMetric.setName(null);

        // Create the TrackMetric, which fails.

        restTrackMetricMockMvc.perform(post("/api/track-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trackMetric)))
            .andExpect(status().isBadRequest());

        List<TrackMetric> trackMetricList = trackMetricRepository.findAll();
        assertThat(trackMetricList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackMetricRepository.findAll().size();
        // set the field null
        trackMetric.setDescription(null);

        // Create the TrackMetric, which fails.

        restTrackMetricMockMvc.perform(post("/api/track-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trackMetric)))
            .andExpect(status().isBadRequest());

        List<TrackMetric> trackMetricList = trackMetricRepository.findAll();
        assertThat(trackMetricList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTrackMetrics() throws Exception {
        // Initialize the database
        trackMetricRepository.saveAndFlush(trackMetric);

        // Get all the trackMetricList
        restTrackMetricMockMvc.perform(get("/api/track-metrics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trackMetric.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].trackIcon").value(hasItem(DEFAULT_TRACK_ICON.toString())))
            .andExpect(jsonPath("$.[*].infoBubbleContentType").value(hasItem(DEFAULT_INFO_BUBBLE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].infoBubble").value(hasItem(Base64Utils.encodeToString(DEFAULT_INFO_BUBBLE))));
    }

    @Test
    @Transactional
    public void getTrackMetric() throws Exception {
        // Initialize the database
        trackMetricRepository.saveAndFlush(trackMetric);

        // Get the trackMetric
        restTrackMetricMockMvc.perform(get("/api/track-metrics/{id}", trackMetric.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(trackMetric.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.trackIcon").value(DEFAULT_TRACK_ICON.toString()))
            .andExpect(jsonPath("$.infoBubbleContentType").value(DEFAULT_INFO_BUBBLE_CONTENT_TYPE))
            .andExpect(jsonPath("$.infoBubble").value(Base64Utils.encodeToString(DEFAULT_INFO_BUBBLE)));
    }

    @Test
    @Transactional
    public void getNonExistingTrackMetric() throws Exception {
        // Get the trackMetric
        restTrackMetricMockMvc.perform(get("/api/track-metrics/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrackMetric() throws Exception {
        // Initialize the database
        trackMetricService.save(trackMetric);

        int databaseSizeBeforeUpdate = trackMetricRepository.findAll().size();

        // Update the trackMetric
        TrackMetric updatedTrackMetric = trackMetricRepository.findOne(trackMetric.getId());
        updatedTrackMetric
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .trackIcon(UPDATED_TRACK_ICON)
            .infoBubble(UPDATED_INFO_BUBBLE)
            .infoBubbleContentType(UPDATED_INFO_BUBBLE_CONTENT_TYPE);

        restTrackMetricMockMvc.perform(put("/api/track-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTrackMetric)))
            .andExpect(status().isOk());

        // Validate the TrackMetric in the database
        List<TrackMetric> trackMetricList = trackMetricRepository.findAll();
        assertThat(trackMetricList).hasSize(databaseSizeBeforeUpdate);
        TrackMetric testTrackMetric = trackMetricList.get(trackMetricList.size() - 1);
        assertThat(testTrackMetric.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTrackMetric.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTrackMetric.getTrackIcon()).isEqualTo(UPDATED_TRACK_ICON);
        assertThat(testTrackMetric.getInfoBubble()).isEqualTo(UPDATED_INFO_BUBBLE);
        assertThat(testTrackMetric.getInfoBubbleContentType()).isEqualTo(UPDATED_INFO_BUBBLE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingTrackMetric() throws Exception {
        int databaseSizeBeforeUpdate = trackMetricRepository.findAll().size();

        // Create the TrackMetric

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTrackMetricMockMvc.perform(put("/api/track-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trackMetric)))
            .andExpect(status().isCreated());

        // Validate the TrackMetric in the database
        List<TrackMetric> trackMetricList = trackMetricRepository.findAll();
        assertThat(trackMetricList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTrackMetric() throws Exception {
        // Initialize the database
        trackMetricService.save(trackMetric);

        int databaseSizeBeforeDelete = trackMetricRepository.findAll().size();

        // Get the trackMetric
        restTrackMetricMockMvc.perform(delete("/api/track-metrics/{id}", trackMetric.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TrackMetric> trackMetricList = trackMetricRepository.findAll();
        assertThat(trackMetricList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrackMetric.class);
        TrackMetric trackMetric1 = new TrackMetric();
        trackMetric1.setId(1L);
        TrackMetric trackMetric2 = new TrackMetric();
        trackMetric2.setId(trackMetric1.getId());
        assertThat(trackMetric1).isEqualTo(trackMetric2);
        trackMetric2.setId(2L);
        assertThat(trackMetric1).isNotEqualTo(trackMetric2);
        trackMetric1.setId(null);
        assertThat(trackMetric1).isNotEqualTo(trackMetric2);
    }
}
