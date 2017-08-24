package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.WellnessItem;
import com.balpos.app.repository.WellnessItemRepository;
import com.balpos.app.service.WellnessItemService;
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
 * Test class for the WellnessItemResource REST controller.
 *
 * @see WellnessItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class WellnessItemResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    private static final Integer DEFAULT_WELLNESSVALUE = 1;
    private static final Integer UPDATED_WELLNESSVALUE = 2;

    @Autowired
    private WellnessItemRepository wellnessItemRepository;

    @Autowired
    private WellnessItemService wellnessItemService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWellnessItemMockMvc;

    private WellnessItem wellnessItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WellnessItemResource wellnessItemResource = new WellnessItemResource(wellnessItemService);
        this.restWellnessItemMockMvc = MockMvcBuilders.standaloneSetup(wellnessItemResource)
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
    public static WellnessItem createEntity(EntityManager em) {
        WellnessItem wellnessItem = new WellnessItem()
            .name(DEFAULT_NAME)
            .desc(DEFAULT_DESC)
            .wellnessvalue(DEFAULT_WELLNESSVALUE);
        return wellnessItem;
    }

    @Before
    public void initTest() {
        wellnessItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createWellnessItem() throws Exception {
        int databaseSizeBeforeCreate = wellnessItemRepository.findAll().size();

        // Create the WellnessItem
        restWellnessItemMockMvc.perform(post("/api/wellness-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wellnessItem)))
            .andExpect(status().isCreated());

        // Validate the WellnessItem in the database
        List<WellnessItem> wellnessItemList = wellnessItemRepository.findAll();
        assertThat(wellnessItemList).hasSize(databaseSizeBeforeCreate + 1);
        WellnessItem testWellnessItem = wellnessItemList.get(wellnessItemList.size() - 1);
        assertThat(testWellnessItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWellnessItem.getDesc()).isEqualTo(DEFAULT_DESC);
        assertThat(testWellnessItem.getWellnessvalue()).isEqualTo(DEFAULT_WELLNESSVALUE);
    }

    @Test
    @Transactional
    public void createWellnessItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wellnessItemRepository.findAll().size();

        // Create the WellnessItem with an existing ID
        wellnessItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWellnessItemMockMvc.perform(post("/api/wellness-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wellnessItem)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<WellnessItem> wellnessItemList = wellnessItemRepository.findAll();
        assertThat(wellnessItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWellnessItems() throws Exception {
        // Initialize the database
        wellnessItemRepository.saveAndFlush(wellnessItem);

        // Get all the wellnessItemList
        restWellnessItemMockMvc.perform(get("/api/wellness-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wellnessItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())))
            .andExpect(jsonPath("$.[*].wellnessvalue").value(hasItem(DEFAULT_WELLNESSVALUE)));
    }

    @Test
    @Transactional
    public void getWellnessItem() throws Exception {
        // Initialize the database
        wellnessItemRepository.saveAndFlush(wellnessItem);

        // Get the wellnessItem
        restWellnessItemMockMvc.perform(get("/api/wellness-items/{id}", wellnessItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wellnessItem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC.toString()))
            .andExpect(jsonPath("$.wellnessvalue").value(DEFAULT_WELLNESSVALUE));
    }

    @Test
    @Transactional
    public void getNonExistingWellnessItem() throws Exception {
        // Get the wellnessItem
        restWellnessItemMockMvc.perform(get("/api/wellness-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWellnessItem() throws Exception {
        // Initialize the database
        wellnessItemService.save(wellnessItem);

        int databaseSizeBeforeUpdate = wellnessItemRepository.findAll().size();

        // Update the wellnessItem
        WellnessItem updatedWellnessItem = wellnessItemRepository.findOne(wellnessItem.getId());
        updatedWellnessItem
            .name(UPDATED_NAME)
            .desc(UPDATED_DESC)
            .wellnessvalue(UPDATED_WELLNESSVALUE);

        restWellnessItemMockMvc.perform(put("/api/wellness-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWellnessItem)))
            .andExpect(status().isOk());

        // Validate the WellnessItem in the database
        List<WellnessItem> wellnessItemList = wellnessItemRepository.findAll();
        assertThat(wellnessItemList).hasSize(databaseSizeBeforeUpdate);
        WellnessItem testWellnessItem = wellnessItemList.get(wellnessItemList.size() - 1);
        assertThat(testWellnessItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWellnessItem.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testWellnessItem.getWellnessvalue()).isEqualTo(UPDATED_WELLNESSVALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingWellnessItem() throws Exception {
        int databaseSizeBeforeUpdate = wellnessItemRepository.findAll().size();

        // Create the WellnessItem

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWellnessItemMockMvc.perform(put("/api/wellness-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wellnessItem)))
            .andExpect(status().isCreated());

        // Validate the WellnessItem in the database
        List<WellnessItem> wellnessItemList = wellnessItemRepository.findAll();
        assertThat(wellnessItemList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWellnessItem() throws Exception {
        // Initialize the database
        wellnessItemService.save(wellnessItem);

        int databaseSizeBeforeDelete = wellnessItemRepository.findAll().size();

        // Get the wellnessItem
        restWellnessItemMockMvc.perform(delete("/api/wellness-items/{id}", wellnessItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WellnessItem> wellnessItemList = wellnessItemRepository.findAll();
        assertThat(wellnessItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WellnessItem.class);
        WellnessItem wellnessItem1 = new WellnessItem();
        wellnessItem1.setId(1L);
        WellnessItem wellnessItem2 = new WellnessItem();
        wellnessItem2.setId(wellnessItem1.getId());
        assertThat(wellnessItem1).isEqualTo(wellnessItem2);
        wellnessItem2.setId(2L);
        assertThat(wellnessItem1).isNotEqualTo(wellnessItem2);
        wellnessItem1.setId(null);
        assertThat(wellnessItem1).isNotEqualTo(wellnessItem2);
    }
}
