package com.balpos.app.service.mapper;

import com.balpos.app.domain.SleepDatum;
import com.balpos.app.service.dto.SleepDatumDTO;
import com.balpos.app.web.rest.mapper.UserEmailToUserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity SleepDatum and its DTO SleepDatumDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, UserEmailToUserMapper.class})
public interface SleepDatumMapper extends EntityMapper<SleepDatumDTO, SleepDatum> {

    SleepDatumDTO toDto(SleepDatum sleepDatum);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    SleepDatum toEntity(SleepDatumDTO sleepDatumDTO);

    default SleepDatum fromId(Long id) {
        if (id == null) {
            return null;
        }
        SleepDatum sleepDatum = new SleepDatum();
        sleepDatum.setId(id);
        return sleepDatum;
    }
}
