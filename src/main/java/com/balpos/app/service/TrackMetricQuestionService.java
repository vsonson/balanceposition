package com.balpos.app.service;

import com.balpos.app.domain.TrackMetricQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing TrackMetricQuestion.
 */
public interface TrackMetricQuestionService {

    /**
     * Save a trackMetricQuestion.
     *
     * @param trackMetricQuestion the entity to save
     * @return the persisted entity
     */
    TrackMetricQuestion save(TrackMetricQuestion trackMetricQuestion);

    /**
     *  Get all the trackMetricQuestions.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TrackMetricQuestion> findAll(Pageable pageable);

    /**
     *  Get the "id" trackMetricQuestion.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TrackMetricQuestion findOne(Long id);

    /**
     *  Delete the "id" trackMetricQuestion.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
