package com.balpos.app.repository;

import com.balpos.app.domain.PathStep;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PathStep entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PathStepRepository extends JpaRepository<PathStep, Long> {

}
