package com.balpos.app.repository;

import com.balpos.app.domain.WellnessItem;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the WellnessItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WellnessItemRepository extends JpaRepository<WellnessItem, Long> {

}
