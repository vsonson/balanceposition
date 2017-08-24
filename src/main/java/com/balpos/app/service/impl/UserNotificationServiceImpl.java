package com.balpos.app.service.impl;

import com.balpos.app.service.UserNotificationService;
import com.balpos.app.domain.UserNotification;
import com.balpos.app.repository.UserNotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing UserNotification.
 */
@Service
@Transactional
public class UserNotificationServiceImpl implements UserNotificationService{

    private final Logger log = LoggerFactory.getLogger(UserNotificationServiceImpl.class);

    private final UserNotificationRepository userNotificationRepository;
    public UserNotificationServiceImpl(UserNotificationRepository userNotificationRepository) {
        this.userNotificationRepository = userNotificationRepository;
    }

    /**
     * Save a userNotification.
     *
     * @param userNotification the entity to save
     * @return the persisted entity
     */
    @Override
    public UserNotification save(UserNotification userNotification) {
        log.debug("Request to save UserNotification : {}", userNotification);
        return userNotificationRepository.save(userNotification);
    }

    /**
     *  Get all the userNotifications.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserNotification> findAll(Pageable pageable) {
        log.debug("Request to get all UserNotifications");
        return userNotificationRepository.findAll(pageable);
    }

    /**
     *  Get one userNotification by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public UserNotification findOne(Long id) {
        log.debug("Request to get UserNotification : {}", id);
        return userNotificationRepository.findOne(id);
    }

    /**
     *  Delete the  userNotification by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserNotification : {}", id);
        userNotificationRepository.delete(id);
    }
}
