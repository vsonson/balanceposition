package com.balpos.app.repository;

import com.balpos.app.domain.LookupValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface MetricValueMappingLookupRepository extends JpaRepository<LookupValue, Long>, JpaSpecificationExecutor<LookupValue> {

    Optional<LookupValue> findByDatapointNameAndSubclassNameAndSourceValue(String datapointName, String subclassName, String sourceValue);
}
