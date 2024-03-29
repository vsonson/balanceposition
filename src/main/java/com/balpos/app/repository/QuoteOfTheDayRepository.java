package com.balpos.app.repository;

import com.balpos.app.domain.QuoteOfTheDay;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the QuoteOfTheDay entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuoteOfTheDayRepository extends JpaRepository<QuoteOfTheDay, Long> {

    List<QuoteOfTheDay> findByIdNotIn(List<Long> ids);
}
