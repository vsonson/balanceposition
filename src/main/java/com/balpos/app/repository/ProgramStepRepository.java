package com.balpos.app.repository;

import com.balpos.app.domain.ProgramStep;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ProgramStep entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProgramStepRepository extends JpaRepository<ProgramStep, Long> {

}
