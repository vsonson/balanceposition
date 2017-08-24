package com.balpos.app.service;

import com.balpos.app.domain.PathAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing PathAction.
 */
public interface PathActionService {

    /**
     * Save a pathAction.
     *
     * @param pathAction the entity to save
     * @return the persisted entity
     */
    PathAction save(PathAction pathAction);

    /**
     *  Get all the pathActions.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PathAction> findAll(Pageable pageable);

    /**
     *  Get the "id" pathAction.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PathAction findOne(Long id);

    /**
     *  Delete the "id" pathAction.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
