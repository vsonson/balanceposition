package com.balpos.app.repository;

import com.balpos.app.domain.TrackMetricQuestion;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TrackMetricQuestion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrackMetricQuestionRepository extends JpaRepository<TrackMetricQuestion, Long> {

}
