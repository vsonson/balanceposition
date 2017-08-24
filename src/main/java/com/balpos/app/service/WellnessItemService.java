package com.balpos.app.service;

import com.balpos.app.domain.WellnessItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing WellnessItem.
 */
public interface WellnessItemService {

    /**
     * Save a wellnessItem.
     *
     * @param wellnessItem the entity to save
     * @return the persisted entity
     */
    WellnessItem save(WellnessItem wellnessItem);

    /**
     *  Get all the wellnessItems.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<WellnessItem> findAll(Pageable pageable);

    /**
     *  Get the "id" wellnessItem.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    WellnessItem findOne(Long id);

    /**
     *  Delete the "id" wellnessItem.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
