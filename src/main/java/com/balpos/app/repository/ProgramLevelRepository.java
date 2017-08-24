package com.balpos.app.repository;

import com.balpos.app.domain.ProgramLevel;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ProgramLevel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProgramLevelRepository extends JpaRepository<ProgramLevel, Long> {

}
