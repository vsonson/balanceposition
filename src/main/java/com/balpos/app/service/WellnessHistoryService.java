package com.balpos.app.service;

import com.balpos.app.domain.WellnessHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing WellnessHistory.
 */
public interface WellnessHistoryService {

    /**
     * Save a wellnessHistory.
     *
     * @param wellnessHistory the entity to save
     * @return the persisted entity
     */
    WellnessHistory save(WellnessHistory wellnessHistory);

    /**
     *  Get all the wellnessHistories.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<WellnessHistory> findAll(Pageable pageable);

    /**
     *  Get the "id" wellnessHistory.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    WellnessHistory findOne(Long id);

    /**
     *  Delete the "id" wellnessHistory.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
