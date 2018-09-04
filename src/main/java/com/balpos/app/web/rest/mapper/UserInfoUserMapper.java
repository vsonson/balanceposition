package com.balpos.app.web.rest.mapper;

import com.balpos.app.domain.UserInfo;
import com.balpos.app.web.rest.vm.UserInfoUserVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {
    UserEmailToUserMapper.class
})
public abstract class UserInfoUserMapper implements VMMapper<UserInfoUserVM, UserInfo> {

    @Mappings({
        @Mapping(target = "networkMembers", ignore = true),
        @Mapping(target = "metricHistories", ignore = true),
        @Mapping(target = "programHistories", ignore = true),
        @Mapping(target = "userNotifications", ignore = true),
        @Mapping(target = "wellnessHistories", ignore = true),
        @Mapping(target = "incentiveHistories", ignore = true),
        @Mapping(target = "quoteOfTheDays", ignore = true)
    })
    public abstract UserInfo toEntity(UserInfoUserVM vm);

    public abstract UserInfoUserVM toVm(UserInfo entity);

}
