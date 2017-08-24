package com.balpos.app.service;

import com.balpos.app.domain.TrackMetric;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing TrackMetric.
 */
public interface TrackMetricService {

    /**
     * Save a trackMetric.
     *
     * @param trackMetric the entity to save
     * @return the persisted entity
     */
    TrackMetric save(TrackMetric trackMetric);

    /**
     *  Get all the trackMetrics.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TrackMetric> findAll(Pageable pageable);

    /**
     *  Get the "id" trackMetric.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TrackMetric findOne(Long id);

    /**
     *  Delete the "id" trackMetric.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
