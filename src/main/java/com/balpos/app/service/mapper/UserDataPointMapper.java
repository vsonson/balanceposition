package com.balpos.app.service.mapper;

import com.balpos.app.domain.UserDataPoint;
import com.balpos.app.service.dto.UserDataPointDTO;
import com.balpos.app.web.rest.vm.DataPointVM;
import com.balpos.app.web.rest.vm.PostDataPointVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DataPointMapper.class})
public interface UserDataPointMapper extends EntityMapper<UserDataPointDTO, UserDataPoint> {

    @Mapping(target = "dataPoint", source = "dataPointName")
    UserDataPointDTO toDto(PostDataPointVM vm);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    UserDataPoint toEntity(UserDataPointDTO dto);

    List<DataPointVM> toDpVM(List<UserDataPointDTO> userDataPoints);

    @Mapping(target = "name", source = "dataPoint.name")
    @Mapping(target = "order", source = "dataPoint.order")
    @Mapping(target = "type", source = "dataPoint.type")
    @Mapping(target = "onePerDay", source = "dataPoint.onePerDay")
    DataPointVM toDpVM(UserDataPointDTO userDataPoints);
}
