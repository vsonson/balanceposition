package com.balpos.app.service;

import com.balpos.app.domain.Resources;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Resources.
 */
public interface ResourcesService {

    /**
     * Save a resources.
     *
     * @param resources the entity to save
     * @return the persisted entity
     */
    Resources save(Resources resources);

    /**
     *  Get all the resources.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Resources> findAll(Pageable pageable);

    /**
     *  Get the "id" resources.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Resources findOne(Long id);

    /**
     *  Delete the "id" resources.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
