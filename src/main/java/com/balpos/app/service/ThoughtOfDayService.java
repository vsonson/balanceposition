package com.balpos.app.service;

import com.balpos.app.domain.ThoughtOfDay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ThoughtOfDay.
 */
public interface ThoughtOfDayService {

    /**
     * Save a thoughtOfDay.
     *
     * @param thoughtOfDay the entity to save
     * @return the persisted entity
     */
    ThoughtOfDay save(ThoughtOfDay thoughtOfDay);

    /**
     *  Get all the thoughtOfDays.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ThoughtOfDay> findAll(Pageable pageable);

    /**
     *  Get the "id" thoughtOfDay.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ThoughtOfDay findOne(Long id);

    /**
     *  Delete the "id" thoughtOfDay.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
