package com.balpos.app.repository;

import com.balpos.app.domain.QuoteOfTheDayHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the QuoteOfTheDayHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuoteOfTheDayHistoryRepository extends JpaRepository<QuoteOfTheDayHistory, Long> {

}
