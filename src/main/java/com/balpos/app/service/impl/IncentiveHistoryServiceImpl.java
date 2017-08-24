package com.balpos.app.service.impl;

import com.balpos.app.service.IncentiveHistoryService;
import com.balpos.app.domain.IncentiveHistory;
import com.balpos.app.repository.IncentiveHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing IncentiveHistory.
 */
@Service
@Transactional
public class IncentiveHistoryServiceImpl implements IncentiveHistoryService{

    private final Logger log = LoggerFactory.getLogger(IncentiveHistoryServiceImpl.class);

    private final IncentiveHistoryRepository incentiveHistoryRepository;
    public IncentiveHistoryServiceImpl(IncentiveHistoryRepository incentiveHistoryRepository) {
        this.incentiveHistoryRepository = incentiveHistoryRepository;
    }

    /**
     * Save a incentiveHistory.
     *
     * @param incentiveHistory the entity to save
     * @return the persisted entity
     */
    @Override
    public IncentiveHistory save(IncentiveHistory incentiveHistory) {
        log.debug("Request to save IncentiveHistory : {}", incentiveHistory);
        return incentiveHistoryRepository.save(incentiveHistory);
    }

    /**
     *  Get all the incentiveHistories.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<IncentiveHistory> findAll(Pageable pageable) {
        log.debug("Request to get all IncentiveHistories");
        return incentiveHistoryRepository.findAll(pageable);
    }

    /**
     *  Get one incentiveHistory by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public IncentiveHistory findOne(Long id) {
        log.debug("Request to get IncentiveHistory : {}", id);
        return incentiveHistoryRepository.findOne(id);
    }

    /**
     *  Delete the  incentiveHistory by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete IncentiveHistory : {}", id);
        incentiveHistoryRepository.delete(id);
    }
}
