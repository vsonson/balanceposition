package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.ProgramLevel;
import com.balpos.app.repository.ProgramLevelRepository;
import com.balpos.app.service.ProgramLevelService;
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
 * Test class for the ProgramLevelResource REST controller.
 *
 * @see ProgramLevelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class ProgramLevelResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_LOCKED = false;
    private static final Boolean UPDATED_IS_LOCKED = true;

    private static final Boolean DEFAULT_IS_PAID = false;
    private static final Boolean UPDATED_IS_PAID = true;

    @Autowired
    private ProgramLevelRepository programLevelRepository;

    @Autowired
    private ProgramLevelService programLevelService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProgramLevelMockMvc;

    private ProgramLevel programLevel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProgramLevelResource programLevelResource = new ProgramLevelResource(programLevelService);
        this.restProgramLevelMockMvc = MockMvcBuilders.standaloneSetup(programLevelResource)
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
    public static ProgramLevel createEntity(EntityManager em) {
        ProgramLevel programLevel = new ProgramLevel()
            .name(DEFAULT_NAME)
            .isLocked(DEFAULT_IS_LOCKED)
            .isPaid(DEFAULT_IS_PAID);
        return programLevel;
    }

    @Before
    public void initTest() {
        programLevel = createEntity(em);
    }

    @Test
    @Transactional
    public void createProgramLevel() throws Exception {
        int databaseSizeBeforeCreate = programLevelRepository.findAll().size();

        // Create the ProgramLevel
        restProgramLevelMockMvc.perform(post("/api/program-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(programLevel)))
            .andExpect(status().isCreated());

        // Validate the ProgramLevel in the database
        List<ProgramLevel> programLevelList = programLevelRepository.findAll();
        assertThat(programLevelList).hasSize(databaseSizeBeforeCreate + 1);
        ProgramLevel testProgramLevel = programLevelList.get(programLevelList.size() - 1);
        assertThat(testProgramLevel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProgramLevel.isIsLocked()).isEqualTo(DEFAULT_IS_LOCKED);
        assertThat(testProgramLevel.isIsPaid()).isEqualTo(DEFAULT_IS_PAID);
    }

    @Test
    @Transactional
    public void createProgramLevelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = programLevelRepository.findAll().size();

        // Create the ProgramLevel with an existing ID
        programLevel.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgramLevelMockMvc.perform(post("/api/program-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(programLevel)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ProgramLevel> programLevelList = programLevelRepository.findAll();
        assertThat(programLevelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = programLevelRepository.findAll().size();
        // set the field null
        programLevel.setName(null);

        // Create the ProgramLevel, which fails.

        restProgramLevelMockMvc.perform(post("/api/program-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(programLevel)))
            .andExpect(status().isBadRequest());

        List<ProgramLevel> programLevelList = programLevelRepository.findAll();
        assertThat(programLevelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsLockedIsRequired() throws Exception {
        int databaseSizeBeforeTest = programLevelRepository.findAll().size();
        // set the field null
        programLevel.setIsLocked(null);

        // Create the ProgramLevel, which fails.

        restProgramLevelMockMvc.perform(post("/api/program-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(programLevel)))
            .andExpect(status().isBadRequest());

        List<ProgramLevel> programLevelList = programLevelRepository.findAll();
        assertThat(programLevelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProgramLevels() throws Exception {
        // Initialize the database
        programLevelRepository.saveAndFlush(programLevel);

        // Get all the programLevelList
        restProgramLevelMockMvc.perform(get("/api/program-levels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(programLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].isLocked").value(hasItem(DEFAULT_IS_LOCKED.booleanValue())))
            .andExpect(jsonPath("$.[*].isPaid").value(hasItem(DEFAULT_IS_PAID.booleanValue())));
    }

    @Test
    @Transactional
    public void getProgramLevel() throws Exception {
        // Initialize the database
        programLevelRepository.saveAndFlush(programLevel);

        // Get the programLevel
        restProgramLevelMockMvc.perform(get("/api/program-levels/{id}", programLevel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(programLevel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.isLocked").value(DEFAULT_IS_LOCKED.booleanValue()))
            .andExpect(jsonPath("$.isPaid").value(DEFAULT_IS_PAID.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProgramLevel() throws Exception {
        // Get the programLevel
        restProgramLevelMockMvc.perform(get("/api/program-levels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProgramLevel() throws Exception {
        // Initialize the database
        programLevelService.save(programLevel);

        int databaseSizeBeforeUpdate = programLevelRepository.findAll().size();

        // Update the programLevel
        ProgramLevel updatedProgramLevel = programLevelRepository.findOne(programLevel.getId());
        updatedProgramLevel
            .name(UPDATED_NAME)
            .isLocked(UPDATED_IS_LOCKED)
            .isPaid(UPDATED_IS_PAID);

        restProgramLevelMockMvc.perform(put("/api/program-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProgramLevel)))
            .andExpect(status().isOk());

        // Validate the ProgramLevel in the database
        List<ProgramLevel> programLevelList = programLevelRepository.findAll();
        assertThat(programLevelList).hasSize(databaseSizeBeforeUpdate);
        ProgramLevel testProgramLevel = programLevelList.get(programLevelList.size() - 1);
        assertThat(testProgramLevel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProgramLevel.isIsLocked()).isEqualTo(UPDATED_IS_LOCKED);
        assertThat(testProgramLevel.isIsPaid()).isEqualTo(UPDATED_IS_PAID);
    }

    @Test
    @Transactional
    public void updateNonExistingProgramLevel() throws Exception {
        int databaseSizeBeforeUpdate = programLevelRepository.findAll().size();

        // Create the ProgramLevel

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProgramLevelMockMvc.perform(put("/api/program-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(programLevel)))
            .andExpect(status().isCreated());

        // Validate the ProgramLevel in the database
        List<ProgramLevel> programLevelList = programLevelRepository.findAll();
        assertThat(programLevelList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProgramLevel() throws Exception {
        // Initialize the database
        programLevelService.save(programLevel);

        int databaseSizeBeforeDelete = programLevelRepository.findAll().size();

        // Get the programLevel
        restProgramLevelMockMvc.perform(delete("/api/program-levels/{id}", programLevel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProgramLevel> programLevelList = programLevelRepository.findAll();
        assertThat(programLevelList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProgramLevel.class);
        ProgramLevel programLevel1 = new ProgramLevel();
        programLevel1.setId(1L);
        ProgramLevel programLevel2 = new ProgramLevel();
        programLevel2.setId(programLevel1.getId());
        assertThat(programLevel1).isEqualTo(programLevel2);
        programLevel2.setId(2L);
        assertThat(programLevel1).isNotEqualTo(programLevel2);
        programLevel1.setId(null);
        assertThat(programLevel1).isNotEqualTo(programLevel2);
    }
}
