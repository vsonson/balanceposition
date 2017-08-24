package com.balpos.app.service.impl;

import com.balpos.app.service.TrackMetricService;
import com.balpos.app.domain.TrackMetric;
import com.balpos.app.repository.TrackMetricRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing TrackMetric.
 */
@Service
@Transactional
public class TrackMetricServiceImpl implements TrackMetricService{

    private final Logger log = LoggerFactory.getLogger(TrackMetricServiceImpl.class);

    private final TrackMetricRepository trackMetricRepository;
    public TrackMetricServiceImpl(TrackMetricRepository trackMetricRepository) {
        this.trackMetricRepository = trackMetricRepository;
    }

    /**
     * Save a trackMetric.
     *
     * @param trackMetric the entity to save
     * @return the persisted entity
     */
    @Override
    public TrackMetric save(TrackMetric trackMetric) {
        log.debug("Request to save TrackMetric : {}", trackMetric);
        return trackMetricRepository.save(trackMetric);
    }

    /**
     *  Get all the trackMetrics.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TrackMetric> findAll(Pageable pageable) {
        log.debug("Request to get all TrackMetrics");
        return trackMetricRepository.findAll(pageable);
    }

    /**
     *  Get one trackMetric by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TrackMetric findOne(Long id) {
        log.debug("Request to get TrackMetric : {}", id);
        return trackMetricRepository.findOne(id);
    }

    /**
     *  Delete the  trackMetric by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TrackMetric : {}", id);
        trackMetricRepository.delete(id);
    }
}
