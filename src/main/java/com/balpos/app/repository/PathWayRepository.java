package com.balpos.app.repository;

import com.balpos.app.domain.PathWay;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PathWay entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PathWayRepository extends JpaRepository<PathWay, Long> {

}
