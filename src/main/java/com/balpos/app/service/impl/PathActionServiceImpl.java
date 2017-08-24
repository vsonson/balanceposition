package com.balpos.app.service.impl;

import com.balpos.app.service.PathActionService;
import com.balpos.app.domain.PathAction;
import com.balpos.app.repository.PathActionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing PathAction.
 */
@Service
@Transactional
public class PathActionServiceImpl implements PathActionService{

    private final Logger log = LoggerFactory.getLogger(PathActionServiceImpl.class);

    private final PathActionRepository pathActionRepository;
    public PathActionServiceImpl(PathActionRepository pathActionRepository) {
        this.pathActionRepository = pathActionRepository;
    }

    /**
     * Save a pathAction.
     *
     * @param pathAction the entity to save
     * @return the persisted entity
     */
    @Override
    public PathAction save(PathAction pathAction) {
        log.debug("Request to save PathAction : {}", pathAction);
        return pathActionRepository.save(pathAction);
    }

    /**
     *  Get all the pathActions.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PathAction> findAll(Pageable pageable) {
        log.debug("Request to get all PathActions");
        return pathActionRepository.findAll(pageable);
    }

    /**
     *  Get one pathAction by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PathAction findOne(Long id) {
        log.debug("Request to get PathAction : {}", id);
        return pathActionRepository.findOne(id);
    }

    /**
     *  Delete the  pathAction by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PathAction : {}", id);
        pathActionRepository.delete(id);
    }
}
