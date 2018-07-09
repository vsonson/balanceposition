package com.balpos.app.service;

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
    MetricDatumDTO save(MetricDatumDTO metricDatumDTO);

    /**
     *  Get all the metricData.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<MetricDatumDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" metricDatum.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    MetricDatumDTO findOne(Long id);

    /**
     *  Delete the "id" metricDatum.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
