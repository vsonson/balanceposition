package com.balpos.app.service;

import com.balpos.app.domain.User;
import com.balpos.app.service.dto.BodyDatumDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing BodyDatum.
 */
public interface BodyDatumService {

    /**
     * Save a bodyDatum.
     *
     * @param bodyDatumDTO the entity to save
     * @return the persisted entity
     */
    BodyDatumDTO save(BodyDatumDTO bodyDatumDTO, User user);

    /**
     *  Get all the bodyData.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BodyDatumDTO> findAll(Pageable pageable);
}
