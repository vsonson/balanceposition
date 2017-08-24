package com.balpos.app.service;

import com.balpos.app.domain.PathStep;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing PathStep.
 */
public interface PathStepService {

    /**
     * Save a pathStep.
     *
     * @param pathStep the entity to save
     * @return the persisted entity
     */
    PathStep save(PathStep pathStep);

    /**
     *  Get all the pathSteps.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PathStep> findAll(Pageable pageable);

    /**
     *  Get the "id" pathStep.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PathStep findOne(Long id);

    /**
     *  Delete the "id" pathStep.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
