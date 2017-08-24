package com.balpos.app.service.impl;

import com.balpos.app.service.TrackMetricQuestionService;
import com.balpos.app.domain.TrackMetricQuestion;
import com.balpos.app.repository.TrackMetricQuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing TrackMetricQuestion.
 */
@Service
@Transactional
public class TrackMetricQuestionServiceImpl implements TrackMetricQuestionService{

    private final Logger log = LoggerFactory.getLogger(TrackMetricQuestionServiceImpl.class);

    private final TrackMetricQuestionRepository trackMetricQuestionRepository;
    public TrackMetricQuestionServiceImpl(TrackMetricQuestionRepository trackMetricQuestionRepository) {
        this.trackMetricQuestionRepository = trackMetricQuestionRepository;
    }

    /**
     * Save a trackMetricQuestion.
     *
     * @param trackMetricQuestion the entity to save
     * @return the persisted entity
     */
    @Override
    public TrackMetricQuestion save(TrackMetricQuestion trackMetricQuestion) {
        log.debug("Request to save TrackMetricQuestion : {}", trackMetricQuestion);
        return trackMetricQuestionRepository.save(trackMetricQuestion);
    }

    /**
     *  Get all the trackMetricQuestions.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TrackMetricQuestion> findAll(Pageable pageable) {
        log.debug("Request to get all TrackMetricQuestions");
        return trackMetricQuestionRepository.findAll(pageable);
    }

    /**
     *  Get one trackMetricQuestion by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TrackMetricQuestion findOne(Long id) {
        log.debug("Request to get TrackMetricQuestion : {}", id);
        return trackMetricQuestionRepository.findOne(id);
    }

    /**
     *  Delete the  trackMetricQuestion by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TrackMetricQuestion : {}", id);
        trackMetricQuestionRepository.delete(id);
    }
}
