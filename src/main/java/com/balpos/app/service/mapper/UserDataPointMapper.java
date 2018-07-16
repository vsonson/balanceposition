package com.balpos.app.service.mapper;

import com.balpos.app.domain.UserDataPoint;
import com.balpos.app.service.dto.UserDataPointDTO;
import com.balpos.app.web.rest.vm.UserDataPointVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DataPointMapper.class})
public interface UserDataPointMapper extends EntityMapper<UserDataPointDTO, UserDataPoint> {

    @Mapping(target = "dataPoint", source = "dataPointName")
    UserDataPointDTO toDto(UserDataPointVM vm);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    UserDataPoint toEntity(UserDataPointDTO dto);
}
