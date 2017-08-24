package com.balpos.app.repository;

import com.balpos.app.domain.Incentive;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Incentive entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IncentiveRepository extends JpaRepository<Incentive, Long> {

}
