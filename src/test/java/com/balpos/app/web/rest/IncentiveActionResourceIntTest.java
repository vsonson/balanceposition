package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.IncentiveAction;
import com.balpos.app.repository.IncentiveActionRepository;
import com.balpos.app.service.IncentiveActionService;
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
 * Test class for the IncentiveActionResource REST controller.
 *
 * @see IncentiveActionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class IncentiveActionResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private IncentiveActionRepository incentiveActionRepository;

    @Autowired
    private IncentiveActionService incentiveActionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIncentiveActionMockMvc;

    private IncentiveAction incentiveAction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IncentiveActionResource incentiveActionResource = new IncentiveActionResource(incentiveActionService);
        this.restIncentiveActionMockMvc = MockMvcBuilders.standaloneSetup(incentiveActionResource)
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
    public static IncentiveAction createEntity(EntityManager em) {
        IncentiveAction incentiveAction = new IncentiveAction()
            .name(DEFAULT_NAME);
        return incentiveAction;
    }

    @Before
    public void initTest() {
        incentiveAction = createEntity(em);
    }

    @Test
    @Transactional
    public void createIncentiveAction() throws Exception {
        int databaseSizeBeforeCreate = incentiveActionRepository.findAll().size();

        // Create the IncentiveAction
        restIncentiveActionMockMvc.perform(post("/api/incentive-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(incentiveAction)))
            .andExpect(status().isCreated());

        // Validate the IncentiveAction in the database
        List<IncentiveAction> incentiveActionList = incentiveActionRepository.findAll();
        assertThat(incentiveActionList).hasSize(databaseSizeBeforeCreate + 1);
        IncentiveAction testIncentiveAction = incentiveActionList.get(incentiveActionList.size() - 1);
        assertThat(testIncentiveAction.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createIncentiveActionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = incentiveActionRepository.findAll().size();

        // Create the IncentiveAction with an existing ID
        incentiveAction.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIncentiveActionMockMvc.perform(post("/api/incentive-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(incentiveAction)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<IncentiveAction> incentiveActionList = incentiveActionRepository.findAll();
        assertThat(incentiveActionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = incentiveActionRepository.findAll().size();
        // set the field null
        incentiveAction.setName(null);

        // Create the IncentiveAction, which fails.

        restIncentiveActionMockMvc.perform(post("/api/incentive-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(incentiveAction)))
            .andExpect(status().isBadRequest());

        List<IncentiveAction> incentiveActionList = incentiveActionRepository.findAll();
        assertThat(incentiveActionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIncentiveActions() throws Exception {
        // Initialize the database
        incentiveActionRepository.saveAndFlush(incentiveAction);

        // Get all the incentiveActionList
        restIncentiveActionMockMvc.perform(get("/api/incentive-actions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(incentiveAction.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getIncentiveAction() throws Exception {
        // Initialize the database
        incentiveActionRepository.saveAndFlush(incentiveAction);

        // Get the incentiveAction
        restIncentiveActionMockMvc.perform(get("/api/incentive-actions/{id}", incentiveAction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(incentiveAction.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIncentiveAction() throws Exception {
        // Get the incentiveAction
        restIncentiveActionMockMvc.perform(get("/api/incentive-actions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIncentiveAction() throws Exception {
        // Initialize the database
        incentiveActionService.save(incentiveAction);

        int databaseSizeBeforeUpdate = incentiveActionRepository.findAll().size();

        // Update the incentiveAction
        IncentiveAction updatedIncentiveAction = incentiveActionRepository.findOne(incentiveAction.getId());
        updatedIncentiveAction
            .name(UPDATED_NAME);

        restIncentiveActionMockMvc.perform(put("/api/incentive-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIncentiveAction)))
            .andExpect(status().isOk());

        // Validate the IncentiveAction in the database
        List<IncentiveAction> incentiveActionList = incentiveActionRepository.findAll();
        assertThat(incentiveActionList).hasSize(databaseSizeBeforeUpdate);
        IncentiveAction testIncentiveAction = incentiveActionList.get(incentiveActionList.size() - 1);
        assertThat(testIncentiveAction.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingIncentiveAction() throws Exception {
        int databaseSizeBeforeUpdate = incentiveActionRepository.findAll().size();

        // Create the IncentiveAction

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restIncentiveActionMockMvc.perform(put("/api/incentive-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(incentiveAction)))
            .andExpect(status().isCreated());

        // Validate the IncentiveAction in the database
        List<IncentiveAction> incentiveActionList = incentiveActionRepository.findAll();
        assertThat(incentiveActionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteIncentiveAction() throws Exception {
        // Initialize the database
        incentiveActionService.save(incentiveAction);

        int databaseSizeBeforeDelete = incentiveActionRepository.findAll().size();

        // Get the incentiveAction
        restIncentiveActionMockMvc.perform(delete("/api/incentive-actions/{id}", incentiveAction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<IncentiveAction> incentiveActionList = incentiveActionRepository.findAll();
        assertThat(incentiveActionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IncentiveAction.class);
        IncentiveAction incentiveAction1 = new IncentiveAction();
        incentiveAction1.setId(1L);
        IncentiveAction incentiveAction2 = new IncentiveAction();
        incentiveAction2.setId(incentiveAction1.getId());
        assertThat(incentiveAction1).isEqualTo(incentiveAction2);
        incentiveAction2.setId(2L);
        assertThat(incentiveAction1).isNotEqualTo(incentiveAction2);
        incentiveAction1.setId(null);
        assertThat(incentiveAction1).isNotEqualTo(incentiveAction2);
    }
}
