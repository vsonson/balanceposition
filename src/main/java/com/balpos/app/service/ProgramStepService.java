package com.balpos.app.service;

import com.balpos.app.domain.ProgramStep;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ProgramStep.
 */
public interface ProgramStepService {

    /**
     * Save a programStep.
     *
     * @param programStep the entity to save
     * @return the persisted entity
     */
    ProgramStep save(ProgramStep programStep);

    /**
     *  Get all the programSteps.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ProgramStep> findAll(Pageable pageable);

    /**
     *  Get the "id" programStep.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ProgramStep findOne(Long id);

    /**
     *  Delete the "id" programStep.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
