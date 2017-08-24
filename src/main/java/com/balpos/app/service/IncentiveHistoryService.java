package com.balpos.app.service;

import com.balpos.app.domain.IncentiveHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing IncentiveHistory.
 */
public interface IncentiveHistoryService {

    /**
     * Save a incentiveHistory.
     *
     * @param incentiveHistory the entity to save
     * @return the persisted entity
     */
    IncentiveHistory save(IncentiveHistory incentiveHistory);

    /**
     *  Get all the incentiveHistories.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<IncentiveHistory> findAll(Pageable pageable);

    /**
     *  Get the "id" incentiveHistory.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    IncentiveHistory findOne(Long id);

    /**
     *  Delete the "id" incentiveHistory.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
