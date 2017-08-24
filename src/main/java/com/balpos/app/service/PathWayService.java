package com.balpos.app.service;

import com.balpos.app.domain.PathWay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing PathWay.
 */
public interface PathWayService {

    /**
     * Save a pathWay.
     *
     * @param pathWay the entity to save
     * @return the persisted entity
     */
    PathWay save(PathWay pathWay);

    /**
     *  Get all the pathWays.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PathWay> findAll(Pageable pageable);

    /**
     *  Get the "id" pathWay.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PathWay findOne(Long id);

    /**
     *  Delete the "id" pathWay.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
