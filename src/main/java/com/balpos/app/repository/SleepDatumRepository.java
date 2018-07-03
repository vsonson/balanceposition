package com.balpos.app.repository;

import com.balpos.app.domain.SleepDatum;
import com.balpos.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the SleepDatum entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SleepDatumRepository extends JpaRepository<SleepDatum, Long>, JpaSpecificationExecutor<SleepDatum> {

    @Query("select sleep_data from SleepDatum sleep_data where sleep_data.user.login = ?#{principal.username}")
    List<SleepDatum> findByUserIsCurrentUser();

    List<SleepDatum> findByUserAndTimestampBetween(User user, LocalDateTime start, LocalDateTime end);
}
