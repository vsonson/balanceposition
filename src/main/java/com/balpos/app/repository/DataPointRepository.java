package com.balpos.app.repository;

import com.balpos.app.domain.DataPoint;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the DataPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataPointRepository extends JpaRepository<DataPoint, Long>, JpaSpecificationExecutor<DataPoint> {

}
