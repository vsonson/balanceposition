package com.balpos.app.service;

import com.balpos.app.domain.MetricHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing MetricHistory.
 */
public interface MetricHistoryService {

    /**
     * Save a metricHistory.
     *
     * @param metricHistory the entity to save
     * @return the persisted entity
     */
    MetricHistory save(MetricHistory metricHistory);

    /**
     *  Get all the metricHistories.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<MetricHistory> findAll(Pageable pageable);

    /**
     *  Get the "id" metricHistory.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    MetricHistory findOne(Long id);

    /**
     *  Delete the "id" metricHistory.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
