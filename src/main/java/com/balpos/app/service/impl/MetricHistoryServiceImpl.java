package com.balpos.app.service.impl;

import com.balpos.app.service.MetricHistoryService;
import com.balpos.app.domain.MetricHistory;
import com.balpos.app.repository.MetricHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing MetricHistory.
 */
@Service
@Transactional
public class MetricHistoryServiceImpl implements MetricHistoryService{

    private final Logger log = LoggerFactory.getLogger(MetricHistoryServiceImpl.class);

    private final MetricHistoryRepository metricHistoryRepository;
    public MetricHistoryServiceImpl(MetricHistoryRepository metricHistoryRepository) {
        this.metricHistoryRepository = metricHistoryRepository;
    }

    /**
     * Save a metricHistory.
     *
     * @param metricHistory the entity to save
     * @return the persisted entity
     */
    @Override
    public MetricHistory save(MetricHistory metricHistory) {
        log.debug("Request to save MetricHistory : {}", metricHistory);
        return metricHistoryRepository.save(metricHistory);
    }

    /**
     *  Get all the metricHistories.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MetricHistory> findAll(Pageable pageable) {
        log.debug("Request to get all MetricHistories");
        return metricHistoryRepository.findAll(pageable);
    }

    /**
     *  Get one metricHistory by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MetricHistory findOne(Long id) {
        log.debug("Request to get MetricHistory : {}", id);
        return metricHistoryRepository.findOne(id);
    }

    /**
     *  Delete the  metricHistory by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MetricHistory : {}", id);
        metricHistoryRepository.delete(id);
    }
}
