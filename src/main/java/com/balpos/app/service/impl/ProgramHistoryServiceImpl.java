package com.balpos.app.service.impl;

import com.balpos.app.service.ProgramHistoryService;
import com.balpos.app.domain.ProgramHistory;
import com.balpos.app.repository.ProgramHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ProgramHistory.
 */
@Service
@Transactional
public class ProgramHistoryServiceImpl implements ProgramHistoryService{

    private final Logger log = LoggerFactory.getLogger(ProgramHistoryServiceImpl.class);

    private final ProgramHistoryRepository programHistoryRepository;
    public ProgramHistoryServiceImpl(ProgramHistoryRepository programHistoryRepository) {
        this.programHistoryRepository = programHistoryRepository;
    }

    /**
     * Save a programHistory.
     *
     * @param programHistory the entity to save
     * @return the persisted entity
     */
    @Override
    public ProgramHistory save(ProgramHistory programHistory) {
        log.debug("Request to save ProgramHistory : {}", programHistory);
        return programHistoryRepository.save(programHistory);
    }

    /**
     *  Get all the programHistories.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProgramHistory> findAll(Pageable pageable) {
        log.debug("Request to get all ProgramHistories");
        return programHistoryRepository.findAll(pageable);
    }

    /**
     *  Get one programHistory by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ProgramHistory findOne(Long id) {
        log.debug("Request to get ProgramHistory : {}", id);
        return programHistoryRepository.findOne(id);
    }

    /**
     *  Delete the  programHistory by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProgramHistory : {}", id);
        programHistoryRepository.delete(id);
    }
}
