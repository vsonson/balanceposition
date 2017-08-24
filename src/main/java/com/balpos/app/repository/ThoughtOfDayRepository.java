package com.balpos.app.repository;

import com.balpos.app.domain.ThoughtOfDay;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ThoughtOfDay entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ThoughtOfDayRepository extends JpaRepository<ThoughtOfDay, Long> {

}
