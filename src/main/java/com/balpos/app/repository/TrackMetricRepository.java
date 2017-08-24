package com.balpos.app.repository;

import com.balpos.app.domain.TrackMetric;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TrackMetric entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrackMetricRepository extends JpaRepository<TrackMetric, Long> {

}
