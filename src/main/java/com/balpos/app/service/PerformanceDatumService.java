package com.balpos.app.service;

import com.balpos.app.domain.User;
import com.balpos.app.service.dto.PerformanceDatumDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing PerformanceDatum.
 */
public interface PerformanceDatumService {

    /**
     * Save a performanceDatum.
     *
     * @param performanceDatumDTO the entity to save
     * @return the persisted entity
     */
    PerformanceDatumDTO save(PerformanceDatumDTO performanceDatumDTO, User user);

    /**
     *  Get all the performanceData.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PerformanceDatumDTO> findAll(Pageable pageable);

}
