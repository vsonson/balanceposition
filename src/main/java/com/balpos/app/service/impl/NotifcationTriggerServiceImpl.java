package com.balpos.app.service.impl;

import com.balpos.app.service.NotifcationTriggerService;
import com.balpos.app.domain.NotifcationTrigger;
import com.balpos.app.repository.NotifcationTriggerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing NotifcationTrigger.
 */
@Service
@Transactional
public class NotifcationTriggerServiceImpl implements NotifcationTriggerService{

    private final Logger log = LoggerFactory.getLogger(NotifcationTriggerServiceImpl.class);

    private final NotifcationTriggerRepository notifcationTriggerRepository;
    public NotifcationTriggerServiceImpl(NotifcationTriggerRepository notifcationTriggerRepository) {
        this.notifcationTriggerRepository = notifcationTriggerRepository;
    }

    /**
     * Save a notifcationTrigger.
     *
     * @param notifcationTrigger the entity to save
     * @return the persisted entity
     */
    @Override
    public NotifcationTrigger save(NotifcationTrigger notifcationTrigger) {
        log.debug("Request to save NotifcationTrigger : {}", notifcationTrigger);
        return notifcationTriggerRepository.save(notifcationTrigger);
    }

    /**
     *  Get all the notifcationTriggers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<NotifcationTrigger> findAll(Pageable pageable) {
        log.debug("Request to get all NotifcationTriggers");
        return notifcationTriggerRepository.findAll(pageable);
    }

    /**
     *  Get one notifcationTrigger by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public NotifcationTrigger findOne(Long id) {
        log.debug("Request to get NotifcationTrigger : {}", id);
        return notifcationTriggerRepository.findOne(id);
    }

    /**
     *  Delete the  notifcationTrigger by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete NotifcationTrigger : {}", id);
        notifcationTriggerRepository.delete(id);
    }
}
