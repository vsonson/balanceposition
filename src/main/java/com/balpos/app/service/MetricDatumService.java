package com.balpos.app.service;

import com.balpos.app.domain.MetricDatum;
import com.balpos.app.domain.User;
import com.balpos.app.service.dto.MetricDatumDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing MetricDatum.
 */
public interface MetricDatumService {

    /**
     * Save a metricDatum.
     *
     * @param metricDatumDTO the entity to save
     * @return the persisted entity
     */
    <T extends MetricDatumDTO, S extends MetricDatum> T save(T metricDatumDTO, User user);

    /**
     * Get all the metricData.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<? extends MetricDatumDTO> findAll(Pageable pageable);

}
