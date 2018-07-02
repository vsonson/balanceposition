package com.balpos.app.repository;

import com.balpos.app.domain.MoodDatum;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the MoodDatum entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MoodDatumRepository extends JpaRepository<MoodDatum, Long>, JpaSpecificationExecutor<MoodDatum> {

    @Query("select mood_data from MoodDatum mood_data where mood_data.user.login = ?#{principal.username}")
    List<MoodDatum> findByUserIsCurrentUser();

}
