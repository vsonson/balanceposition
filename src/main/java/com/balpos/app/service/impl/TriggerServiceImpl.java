package com.balpos.app.service.impl;

import com.balpos.app.service.TriggerService;
import com.balpos.app.domain.Trigger;
import com.balpos.app.repository.TriggerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Trigger.
 */
@Service
@Transactional
public class TriggerServiceImpl implements TriggerService{

    private final Logger log = LoggerFactory.getLogger(TriggerServiceImpl.class);

    private final TriggerRepository triggerRepository;
    public TriggerServiceImpl(TriggerRepository triggerRepository) {
        this.triggerRepository = triggerRepository;
    }

    /**
     * Save a trigger.
     *
     * @param trigger the entity to save
     * @return the persisted entity
     */
    @Override
    public Trigger save(Trigger trigger) {
        log.debug("Request to save Trigger : {}", trigger);
        return triggerRepository.save(trigger);
    }

    /**
     *  Get all the triggers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Trigger> findAll(Pageable pageable) {
        log.debug("Request to get all Triggers");
        return triggerRepository.findAll(pageable);
    }

    /**
     *  Get one trigger by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Trigger findOne(Long id) {
        log.debug("Request to get Trigger : {}", id);
        return triggerRepository.findOne(id);
    }

    /**
     *  Delete the  trigger by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Trigger : {}", id);
        triggerRepository.delete(id);
    }
}
