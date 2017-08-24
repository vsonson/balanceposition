package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;

import com.balpos.app.domain.UserNotification;
import com.balpos.app.repository.UserNotificationRepository;
import com.balpos.app.service.UserNotificationService;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.balpos.app.domain.enumeration.NotificationType;
/**
 * Test class for the UserNotificationResource REST controller.
 *
 * @see UserNotificationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class UserNotificationResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_VISABLE = false;
    private static final Boolean UPDATED_IS_VISABLE = true;

    private static final Boolean DEFAULT_IS_READ = false;
    private static final Boolean UPDATED_IS_READ = true;

    private static final Boolean DEFAULT_IS_COMPLETED = false;
    private static final Boolean UPDATED_IS_COMPLETED = true;

    private static final NotificationType DEFAULT_NOTIFICATION_TYPE = NotificationType.SMS;
    private static final NotificationType UPDATED_NOTIFICATION_TYPE = NotificationType.EMAIL;

    @Autowired
    private UserNotificationRepository userNotificationRepository;

    @Autowired
    private UserNotificationService userNotificationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserNotificationMockMvc;

    private UserNotification userNotification;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserNotificationResource userNotificationResource = new UserNotificationResource(userNotificationService);
        this.restUserNotificationMockMvc = MockMvcBuilders.standaloneSetup(userNotificationResource)
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
    public static UserNotification createEntity(EntityManager em) {
        UserNotification userNotification = new UserNotification()
            .date(DEFAULT_DATE)
            .isVisable(DEFAULT_IS_VISABLE)
            .isRead(DEFAULT_IS_READ)
            .isCompleted(DEFAULT_IS_COMPLETED)
            .notificationType(DEFAULT_NOTIFICATION_TYPE);
        return userNotification;
    }

    @Before
    public void initTest() {
        userNotification = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserNotification() throws Exception {
        int databaseSizeBeforeCreate = userNotificationRepository.findAll().size();

        // Create the UserNotification
        restUserNotificationMockMvc.perform(post("/api/user-notifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userNotification)))
            .andExpect(status().isCreated());

        // Validate the UserNotification in the database
        List<UserNotification> userNotificationList = userNotificationRepository.findAll();
        assertThat(userNotificationList).hasSize(databaseSizeBeforeCreate + 1);
        UserNotification testUserNotification = userNotificationList.get(userNotificationList.size() - 1);
        assertThat(testUserNotification.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testUserNotification.isIsVisable()).isEqualTo(DEFAULT_IS_VISABLE);
        assertThat(testUserNotification.isIsRead()).isEqualTo(DEFAULT_IS_READ);
        assertThat(testUserNotification.isIsCompleted()).isEqualTo(DEFAULT_IS_COMPLETED);
        assertThat(testUserNotification.getNotificationType()).isEqualTo(DEFAULT_NOTIFICATION_TYPE);
    }

    @Test
    @Transactional
    public void createUserNotificationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userNotificationRepository.findAll().size();

        // Create the UserNotification with an existing ID
        userNotification.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserNotificationMockMvc.perform(post("/api/user-notifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userNotification)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<UserNotification> userNotificationList = userNotificationRepository.findAll();
        assertThat(userNotificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUserNotifications() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        // Get all the userNotificationList
        restUserNotificationMockMvc.perform(get("/api/user-notifications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userNotification.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].isVisable").value(hasItem(DEFAULT_IS_VISABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].isRead").value(hasItem(DEFAULT_IS_READ.booleanValue())))
            .andExpect(jsonPath("$.[*].isCompleted").value(hasItem(DEFAULT_IS_COMPLETED.booleanValue())))
            .andExpect(jsonPath("$.[*].notificationType").value(hasItem(DEFAULT_NOTIFICATION_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getUserNotification() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        // Get the userNotification
        restUserNotificationMockMvc.perform(get("/api/user-notifications/{id}", userNotification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userNotification.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.isVisable").value(DEFAULT_IS_VISABLE.booleanValue()))
            .andExpect(jsonPath("$.isRead").value(DEFAULT_IS_READ.booleanValue()))
            .andExpect(jsonPath("$.isCompleted").value(DEFAULT_IS_COMPLETED.booleanValue()))
            .andExpect(jsonPath("$.notificationType").value(DEFAULT_NOTIFICATION_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserNotification() throws Exception {
        // Get the userNotification
        restUserNotificationMockMvc.perform(get("/api/user-notifications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserNotification() throws Exception {
        // Initialize the database
        userNotificationService.save(userNotification);

        int databaseSizeBeforeUpdate = userNotificationRepository.findAll().size();

        // Update the userNotification
        UserNotification updatedUserNotification = userNotificationRepository.findOne(userNotification.getId());
        updatedUserNotification
            .date(UPDATED_DATE)
            .isVisable(UPDATED_IS_VISABLE)
            .isRead(UPDATED_IS_READ)
            .isCompleted(UPDATED_IS_COMPLETED)
            .notificationType(UPDATED_NOTIFICATION_TYPE);

        restUserNotificationMockMvc.perform(put("/api/user-notifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserNotification)))
            .andExpect(status().isOk());

        // Validate the UserNotification in the database
        List<UserNotification> userNotificationList = userNotificationRepository.findAll();
        assertThat(userNotificationList).hasSize(databaseSizeBeforeUpdate);
        UserNotification testUserNotification = userNotificationList.get(userNotificationList.size() - 1);
        assertThat(testUserNotification.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testUserNotification.isIsVisable()).isEqualTo(UPDATED_IS_VISABLE);
        assertThat(testUserNotification.isIsRead()).isEqualTo(UPDATED_IS_READ);
        assertThat(testUserNotification.isIsCompleted()).isEqualTo(UPDATED_IS_COMPLETED);
        assertThat(testUserNotification.getNotificationType()).isEqualTo(UPDATED_NOTIFICATION_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingUserNotification() throws Exception {
        int databaseSizeBeforeUpdate = userNotificationRepository.findAll().size();

        // Create the UserNotification

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserNotificationMockMvc.perform(put("/api/user-notifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userNotification)))
            .andExpect(status().isCreated());

        // Validate the UserNotification in the database
        List<UserNotification> userNotificationList = userNotificationRepository.findAll();
        assertThat(userNotificationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserNotification() throws Exception {
        // Initialize the database
        userNotificationService.save(userNotification);

        int databaseSizeBeforeDelete = userNotificationRepository.findAll().size();

        // Get the userNotification
        restUserNotificationMockMvc.perform(delete("/api/user-notifications/{id}", userNotification.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserNotification> userNotificationList = userNotificationRepository.findAll();
        assertThat(userNotificationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserNotification.class);
        UserNotification userNotification1 = new UserNotification();
        userNotification1.setId(1L);
        UserNotification userNotification2 = new UserNotification();
        userNotification2.setId(userNotification1.getId());
        assertThat(userNotification1).isEqualTo(userNotification2);
        userNotification2.setId(2L);
        assertThat(userNotification1).isNotEqualTo(userNotification2);
        userNotification1.setId(null);
        assertThat(userNotification1).isNotEqualTo(userNotification2);
    }
}
