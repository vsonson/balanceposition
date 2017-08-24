package com.balpos.app.service;

import com.balpos.app.domain.IncentiveAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing IncentiveAction.
 */
public interface IncentiveActionService {

    /**
     * Save a incentiveAction.
     *
     * @param incentiveAction the entity to save
     * @return the persisted entity
     */
    IncentiveAction save(IncentiveAction incentiveAction);

    /**
     *  Get all the incentiveActions.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<IncentiveAction> findAll(Pageable pageable);

    /**
     *  Get the "id" incentiveAction.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    IncentiveAction findOne(Long id);

    /**
     *  Delete the "id" incentiveAction.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
