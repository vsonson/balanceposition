package com.balpos.app.repository;

import com.balpos.app.domain.ProgramHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ProgramHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProgramHistoryRepository extends JpaRepository<ProgramHistory, Long> {

}
