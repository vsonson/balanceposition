package com.balpos.app.service;

import com.balpos.app.domain.User;
import com.balpos.app.service.dto.FocusDatumDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing FocusDatum.
 */
public interface FocusDatumService {

    /**
     * Save a focusDatum.
     *
     * @param focusDatumDTO the entity to save
     * @return the persisted entity
     */
    FocusDatumDTO save(FocusDatumDTO focusDatumDTO, User user);

    /**
     *  Get all the focusData.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<FocusDatumDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" focusDatum.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    FocusDatumDTO findOne(Long id);

}
