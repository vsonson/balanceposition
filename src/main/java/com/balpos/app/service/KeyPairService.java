package com.balpos.app.service;

import com.balpos.app.domain.KeyPair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing KeyPair.
 */
public interface KeyPairService {

    /**
     * Save a keyPair.
     *
     * @param keyPair the entity to save
     * @return the persisted entity
     */
    KeyPair save(KeyPair keyPair);

    /**
     *  Get all the keyPairs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<KeyPair> findAll(Pageable pageable);

    /**
     *  Get the "id" keyPair.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    KeyPair findOne(Long id);

    /**
     *  Delete the "id" keyPair.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
