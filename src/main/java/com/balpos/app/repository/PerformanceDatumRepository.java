package com.balpos.app.repository;

import com.balpos.app.domain.PerformanceDatum;
import com.balpos.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the PerformanceDatum entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PerformanceDatumRepository extends JpaRepository<PerformanceDatum, Long>, JpaSpecificationExecutor<PerformanceDatum> {

    @Query("select performance_data from PerformanceDatum performance_data where performance_data.user.login = ?#{principal.username}")
    List<PerformanceDatum> findByUserIsCurrentUser();

    List<PerformanceDatum> findByUserAndTimestampBetween(User user, LocalDateTime start, LocalDateTime end);

}