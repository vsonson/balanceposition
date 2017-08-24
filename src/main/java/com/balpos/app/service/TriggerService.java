package com.balpos.app.service;

import com.balpos.app.domain.Trigger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Trigger.
 */
public interface TriggerService {

    /**
     * Save a trigger.
     *
     * @param trigger the entity to save
     * @return the persisted entity
     */
    Trigger save(Trigger trigger);

    /**
     *  Get all the triggers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Trigger> findAll(Pageable pageable);

    /**
     *  Get the "id" trigger.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Trigger findOne(Long id);

    /**
     *  Delete the "id" trigger.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
