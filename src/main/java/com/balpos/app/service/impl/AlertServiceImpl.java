package com.balpos.app.service.impl;

import com.balpos.app.service.AlertService;
import com.balpos.app.domain.Alert;
import com.balpos.app.repository.AlertRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Alert.
 */
@Service
@Transactional
public class AlertServiceImpl implements AlertService{

    private final Logger log = LoggerFactory.getLogger(AlertServiceImpl.class);

    private final AlertRepository alertRepository;
    public AlertServiceImpl(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    /**
     * Save a alert.
     *
     * @param alert the entity to save
     * @return the persisted entity
     */
    @Override
    public Alert save(Alert alert) {
        log.debug("Request to save Alert : {}", alert);
        return alertRepository.save(alert);
    }

    /**
     *  Get all the alerts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Alert> findAll(Pageable pageable) {
        log.debug("Request to get all Alerts");
        return alertRepository.findAll(pageable);
    }

    /**
     *  Get one alert by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Alert findOne(Long id) {
        log.debug("Request to get Alert : {}", id);
        return alertRepository.findOne(id);
    }

    /**
     *  Delete the  alert by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Alert : {}", id);
        alertRepository.delete(id);
    }
}
