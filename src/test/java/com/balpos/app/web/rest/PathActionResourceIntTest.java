package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.PathAction;
import com.balpos.app.repository.PathActionRepository;
import com.balpos.app.service.PathActionService;
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

import com.balpos.app.domain.enumeration.ActionType;
/**
 * Test class for the PathActionResource REST controller.
 *
 * @see PathActionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class PathActionResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ACTION_URL = "AAAAAAAAAA";
    private static final String UPDATED_ACTION_URL = "BBBBBBBBBB";

    private static final ActionType DEFAULT_ACTION_TYPE = ActionType.TRACKMETRIC;
    private static final ActionType UPDATED_ACTION_TYPE = ActionType.NOTE;

    @Autowired
    private PathActionRepository pathActionRepository;

    @Autowired
    private PathActionService pathActionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPathActionMockMvc;

    private PathAction pathAction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PathActionResource pathActionResource = new PathActionResource(pathActionService);
        this.restPathActionMockMvc = MockMvcBuilders.standaloneSetup(pathActionResource)
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
    public static PathAction createEntity(EntityManager em) {
        PathAction pathAction = new PathAction()
            .name(DEFAULT_NAME)
            .actionUrl(DEFAULT_ACTION_URL)
            .actionType(DEFAULT_ACTION_TYPE);
        return pathAction;
    }

    @Before
    public void initTest() {
        pathAction = createEntity(em);
    }

    @Test
    @Transactional
    public void createPathAction() throws Exception {
        int databaseSizeBeforeCreate = pathActionRepository.findAll().size();

        // Create the PathAction
        restPathActionMockMvc.perform(post("/api/path-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pathAction)))
            .andExpect(status().isCreated());

        // Validate the PathAction in the database
        List<PathAction> pathActionList = pathActionRepository.findAll();
        assertThat(pathActionList).hasSize(databaseSizeBeforeCreate + 1);
        PathAction testPathAction = pathActionList.get(pathActionList.size() - 1);
        assertThat(testPathAction.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPathAction.getActionUrl()).isEqualTo(DEFAULT_ACTION_URL);
        assertThat(testPathAction.getActionType()).isEqualTo(DEFAULT_ACTION_TYPE);
    }

    @Test
    @Transactional
    public void createPathActionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pathActionRepository.findAll().size();

        // Create the PathAction with an existing ID
        pathAction.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPathActionMockMvc.perform(post("/api/path-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pathAction)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PathAction> pathActionList = pathActionRepository.findAll();
        assertThat(pathActionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pathActionRepository.findAll().size();
        // set the field null
        pathAction.setName(null);

        // Create the PathAction, which fails.

        restPathActionMockMvc.perform(post("/api/path-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pathAction)))
            .andExpect(status().isBadRequest());

        List<PathAction> pathActionList = pathActionRepository.findAll();
        assertThat(pathActionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPathActions() throws Exception {
        // Initialize the database
        pathActionRepository.saveAndFlush(pathAction);

        // Get all the pathActionList
        restPathActionMockMvc.perform(get("/api/path-actions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pathAction.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].actionUrl").value(hasItem(DEFAULT_ACTION_URL.toString())))
            .andExpect(jsonPath("$.[*].actionType").value(hasItem(DEFAULT_ACTION_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getPathAction() throws Exception {
        // Initialize the database
        pathActionRepository.saveAndFlush(pathAction);

        // Get the pathAction
        restPathActionMockMvc.perform(get("/api/path-actions/{id}", pathAction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pathAction.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.actionUrl").value(DEFAULT_ACTION_URL.toString()))
            .andExpect(jsonPath("$.actionType").value(DEFAULT_ACTION_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPathAction() throws Exception {
        // Get the pathAction
        restPathActionMockMvc.perform(get("/api/path-actions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePathAction() throws Exception {
        // Initialize the database
        pathActionService.save(pathAction);

        int databaseSizeBeforeUpdate = pathActionRepository.findAll().size();

        // Update the pathAction
        PathAction updatedPathAction = pathActionRepository.findOne(pathAction.getId());
        updatedPathAction
            .name(UPDATED_NAME)
            .actionUrl(UPDATED_ACTION_URL)
            .actionType(UPDATED_ACTION_TYPE);

        restPathActionMockMvc.perform(put("/api/path-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPathAction)))
            .andExpect(status().isOk());

        // Validate the PathAction in the database
        List<PathAction> pathActionList = pathActionRepository.findAll();
        assertThat(pathActionList).hasSize(databaseSizeBeforeUpdate);
        PathAction testPathAction = pathActionList.get(pathActionList.size() - 1);
        assertThat(testPathAction.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPathAction.getActionUrl()).isEqualTo(UPDATED_ACTION_URL);
        assertThat(testPathAction.getActionType()).isEqualTo(UPDATED_ACTION_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingPathAction() throws Exception {
        int databaseSizeBeforeUpdate = pathActionRepository.findAll().size();

        // Create the PathAction

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPathActionMockMvc.perform(put("/api/path-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pathAction)))
            .andExpect(status().isCreated());

        // Validate the PathAction in the database
        List<PathAction> pathActionList = pathActionRepository.findAll();
        assertThat(pathActionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePathAction() throws Exception {
        // Initialize the database
        pathActionService.save(pathAction);

        int databaseSizeBeforeDelete = pathActionRepository.findAll().size();

        // Get the pathAction
        restPathActionMockMvc.perform(delete("/api/path-actions/{id}", pathAction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PathAction> pathActionList = pathActionRepository.findAll();
        assertThat(pathActionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PathAction.class);
        PathAction pathAction1 = new PathAction();
        pathAction1.setId(1L);
        PathAction pathAction2 = new PathAction();
        pathAction2.setId(pathAction1.getId());
        assertThat(pathAction1).isEqualTo(pathAction2);
        pathAction2.setId(2L);
        assertThat(pathAction1).isNotEqualTo(pathAction2);
        pathAction1.setId(null);
        assertThat(pathAction1).isNotEqualTo(pathAction2);
    }
}
