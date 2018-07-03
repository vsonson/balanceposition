package com.balpos.app.service;

import com.balpos.app.domain.User;
import com.balpos.app.service.dto.SleepDatumDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing SleepDatum.
 */
public interface SleepDatumService {

    /**
     * Save a sleepDatum.
     *
     * @param sleepDatumDTO the entity to save
     * @return the persisted entity
     */
    SleepDatumDTO save(SleepDatumDTO sleepDatumDTO, User user);

    /**
     *  Get all the sleepData.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SleepDatumDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" sleepDatum.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    SleepDatumDTO findOne(Long id);
}
