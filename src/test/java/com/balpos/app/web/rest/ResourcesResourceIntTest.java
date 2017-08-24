package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.Resources;
import com.balpos.app.repository.ResourcesRepository;
import com.balpos.app.service.ResourcesService;
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

import com.balpos.app.domain.enumeration.ResourceType;
/**
 * Test class for the ResourcesResource REST controller.
 *
 * @see ResourcesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class ResourcesResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    private static final byte[] DEFAULT_ICON = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ICON = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_ICON_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ICON_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_RESOURCE_URL = "AAAAAAAAAA";
    private static final String UPDATED_RESOURCE_URL = "BBBBBBBBBB";

    private static final ResourceType DEFAULT_RESOURCE_TYPE = ResourceType.URL;
    private static final ResourceType UPDATED_RESOURCE_TYPE = ResourceType.CALL;

    @Autowired
    private ResourcesRepository resourcesRepository;

    @Autowired
    private ResourcesService resourcesService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restResourcesMockMvc;

    private Resources resources;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ResourcesResource resourcesResource = new ResourcesResource(resourcesService);
        this.restResourcesMockMvc = MockMvcBuilders.standaloneSetup(resourcesResource)
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
    public static Resources createEntity(EntityManager em) {
        Resources resources = new Resources()
            .name(DEFAULT_NAME)
            .desc(DEFAULT_DESC)
            .icon(DEFAULT_ICON)
            .iconContentType(DEFAULT_ICON_CONTENT_TYPE)
            .resourceUrl(DEFAULT_RESOURCE_URL)
            .resourceType(DEFAULT_RESOURCE_TYPE);
        return resources;
    }

    @Before
    public void initTest() {
        resources = createEntity(em);
    }

    @Test
    @Transactional
    public void createResources() throws Exception {
        int databaseSizeBeforeCreate = resourcesRepository.findAll().size();

        // Create the Resources
        restResourcesMockMvc.perform(post("/api/resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resources)))
            .andExpect(status().isCreated());

        // Validate the Resources in the database
        List<Resources> resourcesList = resourcesRepository.findAll();
        assertThat(resourcesList).hasSize(databaseSizeBeforeCreate + 1);
        Resources testResources = resourcesList.get(resourcesList.size() - 1);
        assertThat(testResources.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testResources.getDesc()).isEqualTo(DEFAULT_DESC);
        assertThat(testResources.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testResources.getIconContentType()).isEqualTo(DEFAULT_ICON_CONTENT_TYPE);
        assertThat(testResources.getResourceUrl()).isEqualTo(DEFAULT_RESOURCE_URL);
        assertThat(testResources.getResourceType()).isEqualTo(DEFAULT_RESOURCE_TYPE);
    }

    @Test
    @Transactional
    public void createResourcesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = resourcesRepository.findAll().size();

        // Create the Resources with an existing ID
        resources.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResourcesMockMvc.perform(post("/api/resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resources)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Resources> resourcesList = resourcesRepository.findAll();
        assertThat(resourcesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourcesRepository.findAll().size();
        // set the field null
        resources.setName(null);

        // Create the Resources, which fails.

        restResourcesMockMvc.perform(post("/api/resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resources)))
            .andExpect(status().isBadRequest());

        List<Resources> resourcesList = resourcesRepository.findAll();
        assertThat(resourcesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllResources() throws Exception {
        // Initialize the database
        resourcesRepository.saveAndFlush(resources);

        // Get all the resourcesList
        restResourcesMockMvc.perform(get("/api/resources?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resources.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())))
            .andExpect(jsonPath("$.[*].iconContentType").value(hasItem(DEFAULT_ICON_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(Base64Utils.encodeToString(DEFAULT_ICON))))
            .andExpect(jsonPath("$.[*].resourceUrl").value(hasItem(DEFAULT_RESOURCE_URL.toString())))
            .andExpect(jsonPath("$.[*].resourceType").value(hasItem(DEFAULT_RESOURCE_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getResources() throws Exception {
        // Initialize the database
        resourcesRepository.saveAndFlush(resources);

        // Get the resources
        restResourcesMockMvc.perform(get("/api/resources/{id}", resources.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(resources.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC.toString()))
            .andExpect(jsonPath("$.iconContentType").value(DEFAULT_ICON_CONTENT_TYPE))
            .andExpect(jsonPath("$.icon").value(Base64Utils.encodeToString(DEFAULT_ICON)))
            .andExpect(jsonPath("$.resourceUrl").value(DEFAULT_RESOURCE_URL.toString()))
            .andExpect(jsonPath("$.resourceType").value(DEFAULT_RESOURCE_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingResources() throws Exception {
        // Get the resources
        restResourcesMockMvc.perform(get("/api/resources/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResources() throws Exception {
        // Initialize the database
        resourcesService.save(resources);

        int databaseSizeBeforeUpdate = resourcesRepository.findAll().size();

        // Update the resources
        Resources updatedResources = resourcesRepository.findOne(resources.getId());
        updatedResources
            .name(UPDATED_NAME)
            .desc(UPDATED_DESC)
            .icon(UPDATED_ICON)
            .iconContentType(UPDATED_ICON_CONTENT_TYPE)
            .resourceUrl(UPDATED_RESOURCE_URL)
            .resourceType(UPDATED_RESOURCE_TYPE);

        restResourcesMockMvc.perform(put("/api/resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedResources)))
            .andExpect(status().isOk());

        // Validate the Resources in the database
        List<Resources> resourcesList = resourcesRepository.findAll();
        assertThat(resourcesList).hasSize(databaseSizeBeforeUpdate);
        Resources testResources = resourcesList.get(resourcesList.size() - 1);
        assertThat(testResources.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testResources.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testResources.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testResources.getIconContentType()).isEqualTo(UPDATED_ICON_CONTENT_TYPE);
        assertThat(testResources.getResourceUrl()).isEqualTo(UPDATED_RESOURCE_URL);
        assertThat(testResources.getResourceType()).isEqualTo(UPDATED_RESOURCE_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingResources() throws Exception {
        int databaseSizeBeforeUpdate = resourcesRepository.findAll().size();

        // Create the Resources

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restResourcesMockMvc.perform(put("/api/resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resources)))
            .andExpect(status().isCreated());

        // Validate the Resources in the database
        List<Resources> resourcesList = resourcesRepository.findAll();
        assertThat(resourcesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteResources() throws Exception {
        // Initialize the database
        resourcesService.save(resources);

        int databaseSizeBeforeDelete = resourcesRepository.findAll().size();

        // Get the resources
        restResourcesMockMvc.perform(delete("/api/resources/{id}", resources.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Resources> resourcesList = resourcesRepository.findAll();
        assertThat(resourcesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Resources.class);
        Resources resources1 = new Resources();
        resources1.setId(1L);
        Resources resources2 = new Resources();
        resources2.setId(resources1.getId());
        assertThat(resources1).isEqualTo(resources2);
        resources2.setId(2L);
        assertThat(resources1).isNotEqualTo(resources2);
        resources1.setId(null);
        assertThat(resources1).isNotEqualTo(resources2);
    }
}
