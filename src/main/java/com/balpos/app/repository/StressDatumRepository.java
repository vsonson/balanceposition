package com.balpos.app.repository;

import com.balpos.app.domain.StressDatum;
import com.balpos.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the StressDatum entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StressDatumRepository extends JpaRepository<StressDatum, Long>, JpaSpecificationExecutor<StressDatum> {

    @Query("select stress_data from StressDatum stress_data where stress_data.user.login = ?#{principal.username}")
    List<StressDatum> findByUserIsCurrentUser();

    List<StressDatum> findByUserAndTimestampBetween(User user, ZonedDateTime start, ZonedDateTime end);
}
