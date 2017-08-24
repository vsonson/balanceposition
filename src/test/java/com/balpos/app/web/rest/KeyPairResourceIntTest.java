package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.KeyPair;
import com.balpos.app.repository.KeyPairRepository;
import com.balpos.app.service.KeyPairService;
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
 * Test class for the KeyPairResource REST controller.
 *
 * @see KeyPairResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class KeyPairResourceIntTest {

    private static final String DEFAULT_PAIR_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PAIR_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_KEY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_KEY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_KEY_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_KEY_VALUE = "BBBBBBBBBB";

    @Autowired
    private KeyPairRepository keyPairRepository;

    @Autowired
    private KeyPairService keyPairService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restKeyPairMockMvc;

    private KeyPair keyPair;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final KeyPairResource keyPairResource = new KeyPairResource(keyPairService);
        this.restKeyPairMockMvc = MockMvcBuilders.standaloneSetup(keyPairResource)
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
    public static KeyPair createEntity(EntityManager em) {
        KeyPair keyPair = new KeyPair()
            .pairType(DEFAULT_PAIR_TYPE)
            .keyName(DEFAULT_KEY_NAME)
            .keyValue(DEFAULT_KEY_VALUE);
        return keyPair;
    }

    @Before
    public void initTest() {
        keyPair = createEntity(em);
    }

    @Test
    @Transactional
    public void createKeyPair() throws Exception {
        int databaseSizeBeforeCreate = keyPairRepository.findAll().size();

        // Create the KeyPair
        restKeyPairMockMvc.perform(post("/api/key-pairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(keyPair)))
            .andExpect(status().isCreated());

        // Validate the KeyPair in the database
        List<KeyPair> keyPairList = keyPairRepository.findAll();
        assertThat(keyPairList).hasSize(databaseSizeBeforeCreate + 1);
        KeyPair testKeyPair = keyPairList.get(keyPairList.size() - 1);
        assertThat(testKeyPair.getPairType()).isEqualTo(DEFAULT_PAIR_TYPE);
        assertThat(testKeyPair.getKeyName()).isEqualTo(DEFAULT_KEY_NAME);
        assertThat(testKeyPair.getKeyValue()).isEqualTo(DEFAULT_KEY_VALUE);
    }

    @Test
    @Transactional
    public void createKeyPairWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = keyPairRepository.findAll().size();

        // Create the KeyPair with an existing ID
        keyPair.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restKeyPairMockMvc.perform(post("/api/key-pairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(keyPair)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<KeyPair> keyPairList = keyPairRepository.findAll();
        assertThat(keyPairList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPairTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = keyPairRepository.findAll().size();
        // set the field null
        keyPair.setPairType(null);

        // Create the KeyPair, which fails.

        restKeyPairMockMvc.perform(post("/api/key-pairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(keyPair)))
            .andExpect(status().isBadRequest());

        List<KeyPair> keyPairList = keyPairRepository.findAll();
        assertThat(keyPairList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkKeyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = keyPairRepository.findAll().size();
        // set the field null
        keyPair.setKeyName(null);

        // Create the KeyPair, which fails.

        restKeyPairMockMvc.perform(post("/api/key-pairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(keyPair)))
            .andExpect(status().isBadRequest());

        List<KeyPair> keyPairList = keyPairRepository.findAll();
        assertThat(keyPairList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkKeyValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = keyPairRepository.findAll().size();
        // set the field null
        keyPair.setKeyValue(null);

        // Create the KeyPair, which fails.

        restKeyPairMockMvc.perform(post("/api/key-pairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(keyPair)))
            .andExpect(status().isBadRequest());

        List<KeyPair> keyPairList = keyPairRepository.findAll();
        assertThat(keyPairList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllKeyPairs() throws Exception {
        // Initialize the database
        keyPairRepository.saveAndFlush(keyPair);

        // Get all the keyPairList
        restKeyPairMockMvc.perform(get("/api/key-pairs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(keyPair.getId().intValue())))
            .andExpect(jsonPath("$.[*].pairType").value(hasItem(DEFAULT_PAIR_TYPE.toString())))
            .andExpect(jsonPath("$.[*].keyName").value(hasItem(DEFAULT_KEY_NAME.toString())))
            .andExpect(jsonPath("$.[*].keyValue").value(hasItem(DEFAULT_KEY_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getKeyPair() throws Exception {
        // Initialize the database
        keyPairRepository.saveAndFlush(keyPair);

        // Get the keyPair
        restKeyPairMockMvc.perform(get("/api/key-pairs/{id}", keyPair.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(keyPair.getId().intValue()))
            .andExpect(jsonPath("$.pairType").value(DEFAULT_PAIR_TYPE.toString()))
            .andExpect(jsonPath("$.keyName").value(DEFAULT_KEY_NAME.toString()))
            .andExpect(jsonPath("$.keyValue").value(DEFAULT_KEY_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingKeyPair() throws Exception {
        // Get the keyPair
        restKeyPairMockMvc.perform(get("/api/key-pairs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateKeyPair() throws Exception {
        // Initialize the database
        keyPairService.save(keyPair);

        int databaseSizeBeforeUpdate = keyPairRepository.findAll().size();

        // Update the keyPair
        KeyPair updatedKeyPair = keyPairRepository.findOne(keyPair.getId());
        updatedKeyPair
            .pairType(UPDATED_PAIR_TYPE)
            .keyName(UPDATED_KEY_NAME)
            .keyValue(UPDATED_KEY_VALUE);

        restKeyPairMockMvc.perform(put("/api/key-pairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedKeyPair)))
            .andExpect(status().isOk());

        // Validate the KeyPair in the database
        List<KeyPair> keyPairList = keyPairRepository.findAll();
        assertThat(keyPairList).hasSize(databaseSizeBeforeUpdate);
        KeyPair testKeyPair = keyPairList.get(keyPairList.size() - 1);
        assertThat(testKeyPair.getPairType()).isEqualTo(UPDATED_PAIR_TYPE);
        assertThat(testKeyPair.getKeyName()).isEqualTo(UPDATED_KEY_NAME);
        assertThat(testKeyPair.getKeyValue()).isEqualTo(UPDATED_KEY_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingKeyPair() throws Exception {
        int databaseSizeBeforeUpdate = keyPairRepository.findAll().size();

        // Create the KeyPair

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restKeyPairMockMvc.perform(put("/api/key-pairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(keyPair)))
            .andExpect(status().isCreated());

        // Validate the KeyPair in the database
        List<KeyPair> keyPairList = keyPairRepository.findAll();
        assertThat(keyPairList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteKeyPair() throws Exception {
        // Initialize the database
        keyPairService.save(keyPair);

        int databaseSizeBeforeDelete = keyPairRepository.findAll().size();

        // Get the keyPair
        restKeyPairMockMvc.perform(delete("/api/key-pairs/{id}", keyPair.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<KeyPair> keyPairList = keyPairRepository.findAll();
        assertThat(keyPairList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(KeyPair.class);
        KeyPair keyPair1 = new KeyPair();
        keyPair1.setId(1L);
        KeyPair keyPair2 = new KeyPair();
        keyPair2.setId(keyPair1.getId());
        assertThat(keyPair1).isEqualTo(keyPair2);
        keyPair2.setId(2L);
        assertThat(keyPair1).isNotEqualTo(keyPair2);
        keyPair1.setId(null);
        assertThat(keyPair1).isNotEqualTo(keyPair2);
    }
}
