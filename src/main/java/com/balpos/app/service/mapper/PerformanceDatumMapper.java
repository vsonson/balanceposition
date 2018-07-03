package com.balpos.app.service.mapper;

import com.balpos.app.domain.*;
import com.balpos.app.service.dto.PerformanceDatumDTO;

import com.balpos.app.web.rest.mapper.UserEmailToUserMapper;
import org.mapstruct.*;

/**
 * Mapper for the entity PerformanceDatum and its DTO PerformanceDatumDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, UserEmailToUserMapper.class})
public interface PerformanceDatumMapper extends EntityMapper <PerformanceDatumDTO, PerformanceDatum> {

    PerformanceDatumDTO toDto(PerformanceDatum performanceDatum);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    PerformanceDatum toEntity(PerformanceDatumDTO performanceDatumDTO);

    default PerformanceDatum fromId(Long id) {
        if (id == null) {
            return null;
        }
        PerformanceDatum performanceDatum = new PerformanceDatum();
        performanceDatum.setId(id);
        return performanceDatum;
    }
}
