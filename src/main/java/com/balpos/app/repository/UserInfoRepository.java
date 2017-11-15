package com.balpos.app.repository;

import com.balpos.app.domain.User;
import com.balpos.app.domain.UserInfo;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the UserInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    UserInfo findOneByUser(User user);
}
