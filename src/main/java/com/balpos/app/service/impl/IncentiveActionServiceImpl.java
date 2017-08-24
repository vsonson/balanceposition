package com.balpos.app.service.impl;

import com.balpos.app.service.IncentiveActionService;
import com.balpos.app.domain.IncentiveAction;
import com.balpos.app.repository.IncentiveActionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing IncentiveAction.
 */
@Service
@Transactional
public class IncentiveActionServiceImpl implements IncentiveActionService{

    private final Logger log = LoggerFactory.getLogger(IncentiveActionServiceImpl.class);

    private final IncentiveActionRepository incentiveActionRepository;
    public IncentiveActionServiceImpl(IncentiveActionRepository incentiveActionRepository) {
        this.incentiveActionRepository = incentiveActionRepository;
    }

    /**
     * Save a incentiveAction.
     *
     * @param incentiveAction the entity to save
     * @return the persisted entity
     */
    @Override
    public IncentiveAction save(IncentiveAction incentiveAction) {
        log.debug("Request to save IncentiveAction : {}", incentiveAction);
        return incentiveActionRepository.save(incentiveAction);
    }

    /**
     *  Get all the incentiveActions.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<IncentiveAction> findAll(Pageable pageable) {
        log.debug("Request to get all IncentiveActions");
        return incentiveActionRepository.findAll(pageable);
    }

    /**
     *  Get one incentiveAction by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public IncentiveAction findOne(Long id) {
        log.debug("Request to get IncentiveAction : {}", id);
        return incentiveActionRepository.findOne(id);
    }

    /**
     *  Delete the  incentiveAction by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete IncentiveAction : {}", id);
        incentiveActionRepository.delete(id);
    }
}
