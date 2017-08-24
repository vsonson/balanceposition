package com.balpos.app.service.impl;

import com.balpos.app.service.PathStepService;
import com.balpos.app.domain.PathStep;
import com.balpos.app.repository.PathStepRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing PathStep.
 */
@Service
@Transactional
public class PathStepServiceImpl implements PathStepService{

    private final Logger log = LoggerFactory.getLogger(PathStepServiceImpl.class);

    private final PathStepRepository pathStepRepository;
    public PathStepServiceImpl(PathStepRepository pathStepRepository) {
        this.pathStepRepository = pathStepRepository;
    }

    /**
     * Save a pathStep.
     *
     * @param pathStep the entity to save
     * @return the persisted entity
     */
    @Override
    public PathStep save(PathStep pathStep) {
        log.debug("Request to save PathStep : {}", pathStep);
        return pathStepRepository.save(pathStep);
    }

    /**
     *  Get all the pathSteps.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PathStep> findAll(Pageable pageable) {
        log.debug("Request to get all PathSteps");
        return pathStepRepository.findAll(pageable);
    }

    /**
     *  Get one pathStep by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PathStep findOne(Long id) {
        log.debug("Request to get PathStep : {}", id);
        return pathStepRepository.findOne(id);
    }

    /**
     *  Delete the  pathStep by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PathStep : {}", id);
        pathStepRepository.delete(id);
    }
}
