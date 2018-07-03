package com.balpos.app.service.mapper;

import com.balpos.app.domain.*;
import com.balpos.app.service.dto.PerformanceDatumDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PerformanceDatum and its DTO PerformanceDatumDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface PerformanceDatumMapper extends EntityMapper <PerformanceDatumDTO, PerformanceDatum> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    PerformanceDatumDTO toDto(PerformanceDatum performanceDatum); 

    @Mapping(source = "userId", target = "user")
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
