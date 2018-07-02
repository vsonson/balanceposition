package com.balpos.app.service;

import com.balpos.app.domain.User;
import com.balpos.app.service.dto.MoodDatumDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing MoodDatum.
 */
public interface MoodDatumService {

    /**
     * Save a moodDatum.
     *
     * @param moodDatumDTO the entity to save
     * @return the persisted entity
     */
    MoodDatumDTO save(MoodDatumDTO moodDatumDTO, User user);

    /**
     * Get all the moodData.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MoodDatumDTO> findAll(Pageable pageable);

    /**
     * Get the "id" moodDatum.
     *
     * @param id the id of the entity
     * @return the entity
     */
    MoodDatumDTO findOne(Long id);

}
