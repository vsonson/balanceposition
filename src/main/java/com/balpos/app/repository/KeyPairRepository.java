package com.balpos.app.repository;

import com.balpos.app.domain.KeyPair;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the KeyPair entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KeyPairRepository extends JpaRepository<KeyPair, Long> {

}
