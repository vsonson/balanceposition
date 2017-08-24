package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.ProgramStep;
import com.balpos.app.repository.ProgramStepRepository;
import com.balpos.app.service.ProgramStepService;
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
 * Test class for the ProgramStepResource REST controller.
 *
 * @see ProgramStepResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class ProgramStepResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_F = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_F = "BBBBBBBBBB";

    private static final String DEFAULT_MEDIA_URL = "AAAAAAAAAA";
    private static final String UPDATED_MEDIA_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_LOCKED = false;
    private static final Boolean UPDATED_IS_LOCKED = true;

    private static final Boolean DEFAULT_IS_PAID = false;
    private static final Boolean UPDATED_IS_PAID = true;

    @Autowired
    private ProgramStepRepository programStepRepository;

    @Autowired
    private ProgramStepService programStepService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProgramStepMockMvc;

    private ProgramStep programStep;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProgramStepResource programStepResource = new ProgramStepResource(programStepService);
        this.restProgramStepMockMvc = MockMvcBuilders.standaloneSetup(programStepResource)
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
    public static ProgramStep createEntity(EntityManager em) {
        ProgramStep programStep = new ProgramStep()
            .name(DEFAULT_NAME)
            .descriptionF(DEFAULT_DESCRIPTION_F)
            .mediaUrl(DEFAULT_MEDIA_URL)
            .isLocked(DEFAULT_IS_LOCKED)
            .isPaid(DEFAULT_IS_PAID);
        return programStep;
    }

    @Before
    public void initTest() {
        programStep = createEntity(em);
    }

    @Test
    @Transactional
    public void createProgramStep() throws Exception {
        int databaseSizeBeforeCreate = programStepRepository.findAll().size();

        // Create the ProgramStep
        restProgramStepMockMvc.perform(post("/api/program-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(programStep)))
            .andExpect(status().isCreated());

        // Validate the ProgramStep in the database
        List<ProgramStep> programStepList = programStepRepository.findAll();
        assertThat(programStepList).hasSize(databaseSizeBeforeCreate + 1);
        ProgramStep testProgramStep = programStepList.get(programStepList.size() - 1);
        assertThat(testProgramStep.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProgramStep.getDescriptionF()).isEqualTo(DEFAULT_DESCRIPTION_F);
        assertThat(testProgramStep.getMediaUrl()).isEqualTo(DEFAULT_MEDIA_URL);
        assertThat(testProgramStep.isIsLocked()).isEqualTo(DEFAULT_IS_LOCKED);
        assertThat(testProgramStep.isIsPaid()).isEqualTo(DEFAULT_IS_PAID);
    }

    @Test
    @Transactional
    public void createProgramStepWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = programStepRepository.findAll().size();

        // Create the ProgramStep with an existing ID
        programStep.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgramStepMockMvc.perform(post("/api/program-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(programStep)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ProgramStep> programStepList = programStepRepository.findAll();
        assertThat(programStepList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = programStepRepository.findAll().size();
        // set the field null
        programStep.setName(null);

        // Create the ProgramStep, which fails.

        restProgramStepMockMvc.perform(post("/api/program-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(programStep)))
            .andExpect(status().isBadRequest());

        List<ProgramStep> programStepList = programStepRepository.findAll();
        assertThat(programStepList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMediaUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = programStepRepository.findAll().size();
        // set the field null
        programStep.setMediaUrl(null);

        // Create the ProgramStep, which fails.

        restProgramStepMockMvc.perform(post("/api/program-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(programStep)))
            .andExpect(status().isBadRequest());

        List<ProgramStep> programStepList = programStepRepository.findAll();
        assertThat(programStepList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsLockedIsRequired() throws Exception {
        int databaseSizeBeforeTest = programStepRepository.findAll().size();
        // set the field null
        programStep.setIsLocked(null);

        // Create the ProgramStep, which fails.

        restProgramStepMockMvc.perform(post("/api/program-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(programStep)))
            .andExpect(status().isBadRequest());

        List<ProgramStep> programStepList = programStepRepository.findAll();
        assertThat(programStepList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProgramSteps() throws Exception {
        // Initialize the database
        programStepRepository.saveAndFlush(programStep);

        // Get all the programStepList
        restProgramStepMockMvc.perform(get("/api/program-steps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(programStep.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].descriptionF").value(hasItem(DEFAULT_DESCRIPTION_F.toString())))
            .andExpect(jsonPath("$.[*].mediaUrl").value(hasItem(DEFAULT_MEDIA_URL.toString())))
            .andExpect(jsonPath("$.[*].isLocked").value(hasItem(DEFAULT_IS_LOCKED.booleanValue())))
            .andExpect(jsonPath("$.[*].isPaid").value(hasItem(DEFAULT_IS_PAID.booleanValue())));
    }

    @Test
    @Transactional
    public void getProgramStep() throws Exception {
        // Initialize the database
        programStepRepository.saveAndFlush(programStep);

        // Get the programStep
        restProgramStepMockMvc.perform(get("/api/program-steps/{id}", programStep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(programStep.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.descriptionF").value(DEFAULT_DESCRIPTION_F.toString()))
            .andExpect(jsonPath("$.mediaUrl").value(DEFAULT_MEDIA_URL.toString()))
            .andExpect(jsonPath("$.isLocked").value(DEFAULT_IS_LOCKED.booleanValue()))
            .andExpect(jsonPath("$.isPaid").value(DEFAULT_IS_PAID.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProgramStep() throws Exception {
        // Get the programStep
        restProgramStepMockMvc.perform(get("/api/program-steps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProgramStep() throws Exception {
        // Initialize the database
        programStepService.save(programStep);

        int databaseSizeBeforeUpdate = programStepRepository.findAll().size();

        // Update the programStep
        ProgramStep updatedProgramStep = programStepRepository.findOne(programStep.getId());
        updatedProgramStep
            .name(UPDATED_NAME)
            .descriptionF(UPDATED_DESCRIPTION_F)
            .mediaUrl(UPDATED_MEDIA_URL)
            .isLocked(UPDATED_IS_LOCKED)
            .isPaid(UPDATED_IS_PAID);

        restProgramStepMockMvc.perform(put("/api/program-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProgramStep)))
            .andExpect(status().isOk());

        // Validate the ProgramStep in the database
        List<ProgramStep> programStepList = programStepRepository.findAll();
        assertThat(programStepList).hasSize(databaseSizeBeforeUpdate);
        ProgramStep testProgramStep = programStepList.get(programStepList.size() - 1);
        assertThat(testProgramStep.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProgramStep.getDescriptionF()).isEqualTo(UPDATED_DESCRIPTION_F);
        assertThat(testProgramStep.getMediaUrl()).isEqualTo(UPDATED_MEDIA_URL);
        assertThat(testProgramStep.isIsLocked()).isEqualTo(UPDATED_IS_LOCKED);
        assertThat(testProgramStep.isIsPaid()).isEqualTo(UPDATED_IS_PAID);
    }

    @Test
    @Transactional
    public void updateNonExistingProgramStep() throws Exception {
        int databaseSizeBeforeUpdate = programStepRepository.findAll().size();

        // Create the ProgramStep

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProgramStepMockMvc.perform(put("/api/program-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(programStep)))
            .andExpect(status().isCreated());

        // Validate the ProgramStep in the database
        List<ProgramStep> programStepList = programStepRepository.findAll();
        assertThat(programStepList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProgramStep() throws Exception {
        // Initialize the database
        programStepService.save(programStep);

        int databaseSizeBeforeDelete = programStepRepository.findAll().size();

        // Get the programStep
        restProgramStepMockMvc.perform(delete("/api/program-steps/{id}", programStep.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProgramStep> programStepList = programStepRepository.findAll();
        assertThat(programStepList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProgramStep.class);
        ProgramStep programStep1 = new ProgramStep();
        programStep1.setId(1L);
        ProgramStep programStep2 = new ProgramStep();
        programStep2.setId(programStep1.getId());
        assertThat(programStep1).isEqualTo(programStep2);
        programStep2.setId(2L);
        assertThat(programStep1).isNotEqualTo(programStep2);
        programStep1.setId(null);
        assertThat(programStep1).isNotEqualTo(programStep2);
    }
}
