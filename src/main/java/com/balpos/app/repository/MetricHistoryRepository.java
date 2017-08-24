package com.balpos.app.repository;

import com.balpos.app.domain.MetricHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MetricHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetricHistoryRepository extends JpaRepository<MetricHistory, Long> {

}
