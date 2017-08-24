package com.balpos.app.repository;

import com.balpos.app.domain.UserNotification;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the UserNotification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {

}
