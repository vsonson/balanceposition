package com.balpos.app.repository;

import com.balpos.app.domain.PathAction;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PathAction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PathActionRepository extends JpaRepository<PathAction, Long> {

}
