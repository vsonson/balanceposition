package com.balpos.app.service.impl;

import com.balpos.app.domain.User;
import com.balpos.app.security.SecurityUtils;
import com.balpos.app.service.UserInfoService;
import com.balpos.app.domain.UserInfo;
import com.balpos.app.repository.UserInfoRepository;
import com.balpos.app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * Service Implementation for managing UserInfo.
 */
@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService{

    private final Logger log = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    private final UserInfoRepository userInfoRepository;
    private final UserService userService;

    public UserInfoServiceImpl(UserInfoRepository userInfoRepository, UserService userService) {
        this.userInfoRepository = userInfoRepository;
        this.userService = userService;
    }

    /**
     * Save a userInfo.
     *
     * @param userInfo the entity to save
     * @return the persisted entity
     */
    @Override
    public UserInfo save(UserInfo userInfo) {
        log.debug("Request to save UserInfo : {}", userInfo);

        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        Optional<User> user = userService.getUserWithAuthoritiesByLogin(currentUserLogin);
        userInfo.setUser(user.get());

        return userInfoRepository.save(userInfo);
    }

    /**
     *  Get all the userInfos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserInfo> findAll(Pageable pageable) {
        log.debug("Request to get all UserInfos");

        Page<UserInfo> all;
        if( SecurityUtils.isCurrentUserInRole("ADMIN") ) {
            all = userInfoRepository.findAll(pageable);
        }else{
            User user = userService.getUserWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin()).get();
            all = userInfoRepository.findByUser(pageable, user);

        }
        return all;
    }

    /**
     *  Get one userInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public UserInfo findOne(Long id) {
        log.debug("Request to get UserInfo : {}", id);
        return userInfoRepository.findOne(id);
    }

    /**
     *  Delete the  userInfo by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserInfo : {}", id);
        userInfoRepository.delete(id);
    }
}
