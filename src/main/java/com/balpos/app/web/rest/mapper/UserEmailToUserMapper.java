package com.balpos.app.web.rest.mapper;

import com.balpos.app.domain.User;
import com.balpos.app.repository.UserRepository;
import liquibase.util.StringUtils;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class UserEmailToUserMapper {

    @Autowired
    private UserRepository userRepository;

    public User userEmailToUser(String user) {
        if (StringUtils.isEmpty(user)) return null;
        return userRepository.findOneByEmail(user).orElse(null);
    }

    public String userToUserEmail(User user) {
        if (user == null) return "";
        return user.getEmail();
    }
}
