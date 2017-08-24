package com.balpos.app.service.impl;

import com.balpos.app.service.PathHistoryService;
import com.balpos.app.domain.PathHistory;
import com.balpos.app.repository.PathHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing PathHistory.
 */
@Service
@Transactional
public class PathHistoryServiceImpl implements PathHistoryService{

    private final Logger log = LoggerFactory.getLogger(PathHistoryServiceImpl.class);

    private final PathHistoryRepository pathHistoryRepository;
    public PathHistoryServiceImpl(PathHistoryRepository pathHistoryRepository) {
        this.pathHistoryRepository = pathHistoryRepository;
    }

    /**
     * Save a pathHistory.
     *
     * @param pathHistory the entity to save
     * @return the persisted entity
     */
    @Override
    public PathHistory save(PathHistory pathHistory) {
        log.debug("Request to save PathHistory : {}", pathHistory);
        return pathHistoryRepository.save(pathHistory);
    }

    /**
     *  Get all the pathHistories.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PathHistory> findAll(Pageable pageable) {
        log.debug("Request to get all PathHistories");
        return pathHistoryRepository.findAll(pageable);
    }

    /**
     *  Get one pathHistory by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PathHistory findOne(Long id) {
        log.debug("Request to get PathHistory : {}", id);
        return pathHistoryRepository.findOne(id);
    }

    /**
     *  Delete the  pathHistory by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PathHistory : {}", id);
        pathHistoryRepository.delete(id);
    }
}
