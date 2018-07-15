package com.balpos.app.repository;

import com.balpos.app.domain.DataPoint;
import com.balpos.app.domain.MetricDatum;
import com.balpos.app.domain.PerformanceDatum;
import com.balpos.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the MetricDatum entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetricDatumRepository extends JpaRepository<MetricDatum, Long>, JpaSpecificationExecutor<MetricDatum> {

    @Query("select metric_data from MetricDatum metric_data where metric_data.user.login = ?#{principal.username}")
    List<? extends MetricDatum> findByUserIsCurrentUser();

    List<? extends MetricDatum> findByUserAndDataPointAndTimestampBetween(User user, DataPoint dataPoint, LocalDateTime start, LocalDateTime end);
}
