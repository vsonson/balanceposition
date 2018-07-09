package com.balpos.app.service;

import com.balpos.app.service.dto.DataPointDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing DataPoint.
 */
public interface DataPointService {

    /**
     *  Get all the dataPoints.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<DataPointDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" dataPoint.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    DataPointDTO findOne(Long id);

}
