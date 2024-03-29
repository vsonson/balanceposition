package com.balpos.app.repository;

import com.balpos.app.domain.WellnessHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the WellnessHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WellnessHistoryRepository extends JpaRepository<WellnessHistory, Long> {

}
