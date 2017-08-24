package com.balpos.app.service.impl;

import com.balpos.app.service.IncentiveService;
import com.balpos.app.domain.Incentive;
import com.balpos.app.repository.IncentiveRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Incentive.
 */
@Service
@Transactional
public class IncentiveServiceImpl implements IncentiveService{

    private final Logger log = LoggerFactory.getLogger(IncentiveServiceImpl.class);

    private final IncentiveRepository incentiveRepository;
    public IncentiveServiceImpl(IncentiveRepository incentiveRepository) {
        this.incentiveRepository = incentiveRepository;
    }

    /**
     * Save a incentive.
     *
     * @param incentive the entity to save
     * @return the persisted entity
     */
    @Override
    public Incentive save(Incentive incentive) {
        log.debug("Request to save Incentive : {}", incentive);
        return incentiveRepository.save(incentive);
    }

    /**
     *  Get all the incentives.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Incentive> findAll(Pageable pageable) {
        log.debug("Request to get all Incentives");
        return incentiveRepository.findAll(pageable);
    }

    /**
     *  Get one incentive by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Incentive findOne(Long id) {
        log.debug("Request to get Incentive : {}", id);
        return incentiveRepository.findOne(id);
    }

    /**
     *  Delete the  incentive by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Incentive : {}", id);
        incentiveRepository.delete(id);
    }
}
