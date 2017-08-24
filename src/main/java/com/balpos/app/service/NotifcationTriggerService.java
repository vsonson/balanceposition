package com.balpos.app.service;

import com.balpos.app.domain.NotifcationTrigger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing NotifcationTrigger.
 */
public interface NotifcationTriggerService {

    /**
     * Save a notifcationTrigger.
     *
     * @param notifcationTrigger the entity to save
     * @return the persisted entity
     */
    NotifcationTrigger save(NotifcationTrigger notifcationTrigger);

    /**
     *  Get all the notifcationTriggers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<NotifcationTrigger> findAll(Pageable pageable);

    /**
     *  Get the "id" notifcationTrigger.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    NotifcationTrigger findOne(Long id);

    /**
     *  Delete the "id" notifcationTrigger.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
