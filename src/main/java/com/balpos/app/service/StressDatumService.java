package com.balpos.app.service;

import com.balpos.app.domain.User;
import com.balpos.app.service.dto.StressDatumDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing StressDatum.
 */
public interface StressDatumService {

    /**
     * Save a stressDatum.
     *
     * @param stressDatumDTO the entity to save
     * @param user           user who owns this stress datum
     * @return the persisted entity
     */
    StressDatumDTO save(StressDatumDTO stressDatumDTO, User user);

    /**
     * Get all the stressData.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StressDatumDTO> findAll(Pageable pageable);

    /**
     * Get the "id" stressDatum.
     *
     * @param id the id of the entity
     * @return the entity
     */
    StressDatumDTO findOne(Long id);

    /**
     * Delete the "id" stressDatum.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
