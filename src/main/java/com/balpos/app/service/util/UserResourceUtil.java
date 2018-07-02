package com.balpos.app.service.util;

import com.balpos.app.domain.User;
import com.balpos.app.service.UserService;
import io.github.jhipster.service.filter.LongFilter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserResourceUtil {

    private final UserService userService;

    public UserResourceUtil(UserService userService) {
        this.userService = userService;
    }

    public LongFilter createUserFilterFromLogin(String userLogin) {
        Optional<User> queryResult = userService.getUserByLogin(userLogin);
        LongFilter userFilter = new LongFilter();
        queryResult.ifPresent(user -> userFilter.setEquals(user.getId()));
        return userFilter;
    }

    public Optional<User> getUserFromLogin( String userLogin ){
        return userService.getUserByLogin(userLogin);
    }
}
