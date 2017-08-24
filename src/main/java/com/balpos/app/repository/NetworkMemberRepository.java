package com.balpos.app.repository;

import com.balpos.app.domain.NetworkMember;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the NetworkMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NetworkMemberRepository extends JpaRepository<NetworkMember, Long> {

}
