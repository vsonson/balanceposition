package com.balpos.app.repository;

import com.balpos.app.domain.MetricDatum;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the MetricDatum entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetricDatumRepository extends JpaRepository<MetricDatum, Long>, JpaSpecificationExecutor<MetricDatum> {

    @Query("select metric_data from MetricDatum metric_data where metric_data.user.login = ?#{principal.username}")
    List<MetricDatum> findByUserIsCurrentUser();

}
