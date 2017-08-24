package com.balpos.app.service;

import com.balpos.app.domain.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing UserInfo.
 */
public interface UserInfoService {

    /**
     * Save a userInfo.
     *
     * @param userInfo the entity to save
     * @return the persisted entity
     */
    UserInfo save(UserInfo userInfo);

    /**
     *  Get all the userInfos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<UserInfo> findAll(Pageable pageable);

    /**
     *  Get the "id" userInfo.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    UserInfo findOne(Long id);

    /**
     *  Delete the "id" userInfo.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
