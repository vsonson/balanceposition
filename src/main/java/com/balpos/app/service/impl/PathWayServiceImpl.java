package com.balpos.app.service.impl;

import com.balpos.app.service.PathWayService;
import com.balpos.app.domain.PathWay;
import com.balpos.app.repository.PathWayRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing PathWay.
 */
@Service
@Transactional
public class PathWayServiceImpl implements PathWayService{

    private final Logger log = LoggerFactory.getLogger(PathWayServiceImpl.class);

    private final PathWayRepository pathWayRepository;
    public PathWayServiceImpl(PathWayRepository pathWayRepository) {
        this.pathWayRepository = pathWayRepository;
    }

    /**
     * Save a pathWay.
     *
     * @param pathWay the entity to save
     * @return the persisted entity
     */
    @Override
    public PathWay save(PathWay pathWay) {
        log.debug("Request to save PathWay : {}", pathWay);
        return pathWayRepository.save(pathWay);
    }

    /**
     *  Get all the pathWays.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PathWay> findAll(Pageable pageable) {
        log.debug("Request to get all PathWays");
        return pathWayRepository.findAll(pageable);
    }

    /**
     *  Get one pathWay by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PathWay findOne(Long id) {
        log.debug("Request to get PathWay : {}", id);
        return pathWayRepository.findOne(id);
    }

    /**
     *  Delete the  pathWay by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PathWay : {}", id);
        pathWayRepository.delete(id);
    }
}
