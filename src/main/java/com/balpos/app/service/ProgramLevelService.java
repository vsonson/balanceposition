package com.balpos.app.service;

import com.balpos.app.domain.ProgramLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ProgramLevel.
 */
public interface ProgramLevelService {

    /**
     * Save a programLevel.
     *
     * @param programLevel the entity to save
     * @return the persisted entity
     */
    ProgramLevel save(ProgramLevel programLevel);

    /**
     *  Get all the programLevels.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ProgramLevel> findAll(Pageable pageable);

    /**
     *  Get the "id" programLevel.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ProgramLevel findOne(Long id);

    /**
     *  Delete the "id" programLevel.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
