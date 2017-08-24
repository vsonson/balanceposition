package com.balpos.app.repository;

import com.balpos.app.domain.IncentiveAction;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the IncentiveAction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IncentiveActionRepository extends JpaRepository<IncentiveAction, Long> {

}
