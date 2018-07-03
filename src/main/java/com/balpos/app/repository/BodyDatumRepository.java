package com.balpos.app.repository;

import com.balpos.app.domain.BodyDatum;
import com.balpos.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the BodyDatum entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BodyDatumRepository extends JpaRepository<BodyDatum, Long>, JpaSpecificationExecutor<BodyDatum> {

    @Query("select body_data from BodyDatum body_data where body_data.user.login = ?#{principal.username}")
    List<BodyDatum> findByUserIsCurrentUser();

    List<BodyDatum> findByUserAndTimestampBetween(User user, LocalDateTime start, LocalDateTime end);

}
