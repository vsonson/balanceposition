package com.balpos.app.repository;

import com.balpos.app.domain.User;
import com.balpos.app.domain.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the UserInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    Page<UserInfo> findByUser(Pageable pageable, User user);

    UserInfo findByIdAndUser(Long id, User user);

    void deleteByIdAndUser(Long id, User user);
}
