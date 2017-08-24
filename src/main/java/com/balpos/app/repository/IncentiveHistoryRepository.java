package com.balpos.app.repository;

import com.balpos.app.domain.IncentiveHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the IncentiveHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IncentiveHistoryRepository extends JpaRepository<IncentiveHistory, Long> {

}
