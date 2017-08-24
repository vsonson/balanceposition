package com.balpos.app.service;

import com.balpos.app.domain.PathHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing PathHistory.
 */
public interface PathHistoryService {

    /**
     * Save a pathHistory.
     *
     * @param pathHistory the entity to save
     * @return the persisted entity
     */
    PathHistory save(PathHistory pathHistory);

    /**
     *  Get all the pathHistories.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PathHistory> findAll(Pageable pageable);

    /**
     *  Get the "id" pathHistory.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PathHistory findOne(Long id);

    /**
     *  Delete the "id" pathHistory.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
