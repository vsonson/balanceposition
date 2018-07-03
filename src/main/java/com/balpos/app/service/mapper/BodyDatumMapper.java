package com.balpos.app.service.mapper;

import com.balpos.app.domain.BodyDatum;
import com.balpos.app.service.dto.BodyDatumDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity BodyDatum and its DTO BodyDatumDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface BodyDatumMapper extends EntityMapper<BodyDatumDTO, BodyDatum> {

    BodyDatumDTO toDto(BodyDatum bodyDatum);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    BodyDatum toEntity(BodyDatumDTO bodyDatumDTO);

    default BodyDatum fromId(Long id) {
        if (id == null) {
            return null;
        }
        BodyDatum bodyDatum = new BodyDatum();
        bodyDatum.setId(id);
        return bodyDatum;
    }
}
