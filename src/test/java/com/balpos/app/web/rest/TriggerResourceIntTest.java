package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.Trigger;
import com.balpos.app.repository.TriggerRepository;
import com.balpos.app.service.TriggerService;
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
 * Test class for the TriggerResource REST controller.
 *
 * @see TriggerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class TriggerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    private static final String DEFAULT_TRIGGER_METRIC = "AAAAAAAAAA";
    private static final String UPDATED_TRIGGER_METRIC = "BBBBBBBBBB";

    @Autowired
    private TriggerRepository triggerRepository;

    @Autowired
    private TriggerService triggerService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTriggerMockMvc;

    private Trigger trigger;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TriggerResource triggerResource = new TriggerResource(triggerService);
        this.restTriggerMockMvc = MockMvcBuilders.standaloneSetup(triggerResource)
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
    public static Trigger createEntity(EntityManager em) {
        Trigger trigger = new Trigger()
            .name(DEFAULT_NAME)
            .desc(DEFAULT_DESC)
            .triggerMetric(DEFAULT_TRIGGER_METRIC);
        return trigger;
    }

    @Before
    public void initTest() {
        trigger = createEntity(em);
    }

    @Test
    @Transactional
    public void createTrigger() throws Exception {
        int databaseSizeBeforeCreate = triggerRepository.findAll().size();

        // Create the Trigger
        restTriggerMockMvc.perform(post("/api/triggers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trigger)))
            .andExpect(status().isCreated());

        // Validate the Trigger in the database
        List<Trigger> triggerList = triggerRepository.findAll();
        assertThat(triggerList).hasSize(databaseSizeBeforeCreate + 1);
        Trigger testTrigger = triggerList.get(triggerList.size() - 1);
        assertThat(testTrigger.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTrigger.getDesc()).isEqualTo(DEFAULT_DESC);
        assertThat(testTrigger.getTriggerMetric()).isEqualTo(DEFAULT_TRIGGER_METRIC);
    }

    @Test
    @Transactional
    public void createTriggerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = triggerRepository.findAll().size();

        // Create the Trigger with an existing ID
        trigger.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTriggerMockMvc.perform(post("/api/triggers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trigger)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Trigger> triggerList = triggerRepository.findAll();
        assertThat(triggerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = triggerRepository.findAll().size();
        // set the field null
        trigger.setName(null);

        // Create the Trigger, which fails.

        restTriggerMockMvc.perform(post("/api/triggers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trigger)))
            .andExpect(status().isBadRequest());

        List<Trigger> triggerList = triggerRepository.findAll();
        assertThat(triggerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTriggers() throws Exception {
        // Initialize the database
        triggerRepository.saveAndFlush(trigger);

        // Get all the triggerList
        restTriggerMockMvc.perform(get("/api/triggers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trigger.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())))
            .andExpect(jsonPath("$.[*].triggerMetric").value(hasItem(DEFAULT_TRIGGER_METRIC.toString())));
    }

    @Test
    @Transactional
    public void getTrigger() throws Exception {
        // Initialize the database
        triggerRepository.saveAndFlush(trigger);

        // Get the trigger
        restTriggerMockMvc.perform(get("/api/triggers/{id}", trigger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(trigger.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC.toString()))
            .andExpect(jsonPath("$.triggerMetric").value(DEFAULT_TRIGGER_METRIC.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTrigger() throws Exception {
        // Get the trigger
        restTriggerMockMvc.perform(get("/api/triggers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrigger() throws Exception {
        // Initialize the database
        triggerService.save(trigger);

        int databaseSizeBeforeUpdate = triggerRepository.findAll().size();

        // Update the trigger
        Trigger updatedTrigger = triggerRepository.findOne(trigger.getId());
        updatedTrigger
            .name(UPDATED_NAME)
            .desc(UPDATED_DESC)
            .triggerMetric(UPDATED_TRIGGER_METRIC);

        restTriggerMockMvc.perform(put("/api/triggers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTrigger)))
            .andExpect(status().isOk());

        // Validate the Trigger in the database
        List<Trigger> triggerList = triggerRepository.findAll();
        assertThat(triggerList).hasSize(databaseSizeBeforeUpdate);
        Trigger testTrigger = triggerList.get(triggerList.size() - 1);
        assertThat(testTrigger.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTrigger.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testTrigger.getTriggerMetric()).isEqualTo(UPDATED_TRIGGER_METRIC);
    }

    @Test
    @Transactional
    public void updateNonExistingTrigger() throws Exception {
        int databaseSizeBeforeUpdate = triggerRepository.findAll().size();

        // Create the Trigger

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTriggerMockMvc.perform(put("/api/triggers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trigger)))
            .andExpect(status().isCreated());

        // Validate the Trigger in the database
        List<Trigger> triggerList = triggerRepository.findAll();
        assertThat(triggerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTrigger() throws Exception {
        // Initialize the database
        triggerService.save(trigger);

        int databaseSizeBeforeDelete = triggerRepository.findAll().size();

        // Get the trigger
        restTriggerMockMvc.perform(delete("/api/triggers/{id}", trigger.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Trigger> triggerList = triggerRepository.findAll();
        assertThat(triggerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Trigger.class);
        Trigger trigger1 = new Trigger();
        trigger1.setId(1L);
        Trigger trigger2 = new Trigger();
        trigger2.setId(trigger1.getId());
        assertThat(trigger1).isEqualTo(trigger2);
        trigger2.setId(2L);
        assertThat(trigger1).isNotEqualTo(trigger2);
        trigger1.setId(null);
        assertThat(trigger1).isNotEqualTo(trigger2);
    }
}
