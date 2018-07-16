package com.balpos.app.repository;

import com.balpos.app.domain.DataPoint;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the DataPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataPointRepository extends JpaRepository<DataPoint, Long>, JpaSpecificationExecutor<DataPoint> {

    @Cacheable("datapoints")
    DataPoint findByName(String datapointName);

}
