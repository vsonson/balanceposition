package com.balpos.app.service.mapper;

import com.balpos.app.domain.DataPoint;
import com.balpos.app.repository.DataPointRepository;
import com.balpos.app.service.dto.DataPointDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper for the entity DataPoint and its DTO DataPointDTO.
 */
@Mapper(componentModel = "spring")
public abstract class DataPointMapper implements EntityMapper<DataPointDTO, DataPoint> {

    @Autowired
    private DataPointRepository dataPointRepository;

    @Mapping(target = "id", ignore = true)
    public abstract DataPoint toEntity(DataPointDTO dto);

    public DataPoint fromName(String dataPointName) {
        return dataPointRepository.findByName(dataPointName);
    }

    public DataPoint fromId(Long id) {
        if (id == null) {
            return null;
        }
        DataPoint dataPoint = new DataPoint();
        dataPoint.setId(id);
        return dataPoint;
    }
}
