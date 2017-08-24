package com.balpos.app.service.impl;

import com.balpos.app.service.KeyPairService;
import com.balpos.app.domain.KeyPair;
import com.balpos.app.repository.KeyPairRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing KeyPair.
 */
@Service
@Transactional
public class KeyPairServiceImpl implements KeyPairService{

    private final Logger log = LoggerFactory.getLogger(KeyPairServiceImpl.class);

    private final KeyPairRepository keyPairRepository;
    public KeyPairServiceImpl(KeyPairRepository keyPairRepository) {
        this.keyPairRepository = keyPairRepository;
    }

    /**
     * Save a keyPair.
     *
     * @param keyPair the entity to save
     * @return the persisted entity
     */
    @Override
    public KeyPair save(KeyPair keyPair) {
        log.debug("Request to save KeyPair : {}", keyPair);
        return keyPairRepository.save(keyPair);
    }

    /**
     *  Get all the keyPairs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<KeyPair> findAll(Pageable pageable) {
        log.debug("Request to get all KeyPairs");
        return keyPairRepository.findAll(pageable);
    }

    /**
     *  Get one keyPair by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public KeyPair findOne(Long id) {
        log.debug("Request to get KeyPair : {}", id);
        return keyPairRepository.findOne(id);
    }

    /**
     *  Delete the  keyPair by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete KeyPair : {}", id);
        keyPairRepository.delete(id);
    }
}
