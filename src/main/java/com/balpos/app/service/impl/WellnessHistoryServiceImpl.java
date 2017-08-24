package com.balpos.app.service.impl;

import com.balpos.app.service.WellnessHistoryService;
import com.balpos.app.domain.WellnessHistory;
import com.balpos.app.repository.WellnessHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing WellnessHistory.
 */
@Service
@Transactional
public class WellnessHistoryServiceImpl implements WellnessHistoryService{

    private final Logger log = LoggerFactory.getLogger(WellnessHistoryServiceImpl.class);

    private final WellnessHistoryRepository wellnessHistoryRepository;
    public WellnessHistoryServiceImpl(WellnessHistoryRepository wellnessHistoryRepository) {
        this.wellnessHistoryRepository = wellnessHistoryRepository;
    }

    /**
     * Save a wellnessHistory.
     *
     * @param wellnessHistory the entity to save
     * @return the persisted entity
     */
    @Override
    public WellnessHistory save(WellnessHistory wellnessHistory) {
        log.debug("Request to save WellnessHistory : {}", wellnessHistory);
        return wellnessHistoryRepository.save(wellnessHistory);
    }

    /**
     *  Get all the wellnessHistories.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WellnessHistory> findAll(Pageable pageable) {
        log.debug("Request to get all WellnessHistories");
        return wellnessHistoryRepository.findAll(pageable);
    }

    /**
     *  Get one wellnessHistory by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public WellnessHistory findOne(Long id) {
        log.debug("Request to get WellnessHistory : {}", id);
        return wellnessHistoryRepository.findOne(id);
    }

    /**
     *  Delete the  wellnessHistory by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WellnessHistory : {}", id);
        wellnessHistoryRepository.delete(id);
    }
}
