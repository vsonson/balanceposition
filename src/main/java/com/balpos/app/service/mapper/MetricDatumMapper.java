package com.balpos.app.service.mapper;

import com.balpos.app.domain.*;
import com.balpos.app.service.dto.MetricDatumDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity MetricDatum and its DTO MetricDatumDTO.
 */
@Mapper(componentModel = "spring", uses = {DataPointMapper.class, UserMapper.class, })
public interface MetricDatumMapper extends EntityMapper <MetricDatumDTO, MetricDatum> {

    @Mapping(source = "dataPoint.name", target = "dataPointName")
    @Mapping(source = "user.login", target = "userLogin")
    MetricDatumDTO toDto(MetricDatum metricDatum);

    @Mapping(source = "dataPointName", target = "dataPoint")
    @Mapping(source = "userLogin", target = "user")
    MetricDatum toEntity(MetricDatumDTO metricDatumDTO);
    default MetricDatum fromId(Long id) {
        if (id == null) {
            return null;
        }
        MetricDatum metricDatum = new MetricDatum();
        metricDatum.setId(id);
        return metricDatum;
    }
}
