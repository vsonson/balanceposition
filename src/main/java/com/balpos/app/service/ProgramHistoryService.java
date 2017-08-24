package com.balpos.app.service;

import com.balpos.app.domain.ProgramHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ProgramHistory.
 */
public interface ProgramHistoryService {

    /**
     * Save a programHistory.
     *
     * @param programHistory the entity to save
     * @return the persisted entity
     */
    ProgramHistory save(ProgramHistory programHistory);

    /**
     *  Get all the programHistories.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ProgramHistory> findAll(Pageable pageable);

    /**
     *  Get the "id" programHistory.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ProgramHistory findOne(Long id);

    /**
     *  Delete the "id" programHistory.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
