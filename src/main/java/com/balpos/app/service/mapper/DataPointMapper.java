package com.balpos.app.service.mapper;

import com.balpos.app.domain.DataPoint;
import com.balpos.app.service.dto.DataPointDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity DataPoint and its DTO DataPointDTO.
 */
@Mapper(componentModel = "spring")
public interface DataPointMapper extends EntityMapper<DataPointDTO, DataPoint> {

    default DataPoint fromId(Long id) {
        if (id == null) {
            return null;
        }
        DataPoint dataPoint = new DataPoint();
        dataPoint.setId(id);
        return dataPoint;
    }
}
