package com.balpos.app.repository;

import com.balpos.app.domain.NotifcationTrigger;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the NotifcationTrigger entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotifcationTriggerRepository extends JpaRepository<NotifcationTrigger, Long> {

}
