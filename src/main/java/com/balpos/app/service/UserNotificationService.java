package com.balpos.app.service;

import com.balpos.app.domain.UserNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing UserNotification.
 */
public interface UserNotificationService {

    /**
     * Save a userNotification.
     *
     * @param userNotification the entity to save
     * @return the persisted entity
     */
    UserNotification save(UserNotification userNotification);

    /**
     *  Get all the userNotifications.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<UserNotification> findAll(Pageable pageable);

    /**
     *  Get the "id" userNotification.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    UserNotification findOne(Long id);

    /**
     *  Delete the "id" userNotification.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
