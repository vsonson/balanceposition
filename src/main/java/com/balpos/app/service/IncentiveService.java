package com.balpos.app.service;

import com.balpos.app.domain.Incentive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Incentive.
 */
public interface IncentiveService {

    /**
     * Save a incentive.
     *
     * @param incentive the entity to save
     * @return the persisted entity
     */
    Incentive save(Incentive incentive);

    /**
     *  Get all the incentives.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Incentive> findAll(Pageable pageable);

    /**
     *  Get the "id" incentive.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Incentive findOne(Long id);

    /**
     *  Delete the "id" incentive.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
