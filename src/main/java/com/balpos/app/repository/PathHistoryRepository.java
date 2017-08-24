package com.balpos.app.repository;

import com.balpos.app.domain.PathHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PathHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PathHistoryRepository extends JpaRepository<PathHistory, Long> {

}
