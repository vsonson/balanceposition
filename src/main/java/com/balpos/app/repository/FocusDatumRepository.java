package com.balpos.app.repository;

import com.balpos.app.domain.FocusDatum;
import com.balpos.app.domain.StressDatum;
import com.balpos.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the FocusDatum entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FocusDatumRepository extends JpaRepository<FocusDatum, Long>, JpaSpecificationExecutor<FocusDatum> {

    @Query("select focus_data from FocusDatum focus_data where focus_data.user.login = ?#{principal.username}")
    List<FocusDatum> findByUserIsCurrentUser();

    List<FocusDatum> findByUserAndTimestampBetween(User user, ZonedDateTime start, ZonedDateTime end);
}
