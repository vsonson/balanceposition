package com.balpos.app.service.mapper;

import com.balpos.app.domain.*;
import com.balpos.app.service.dto.MoodDatumDTO;

import com.balpos.app.web.rest.mapper.UserEmailToUserMapper;
import org.mapstruct.*;

/**
 * Mapper for the entity MoodDatum and its DTO MoodDatumDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, UserEmailToUserMapper.class})
public interface MoodDatumMapper extends EntityMapper <MoodDatumDTO, MoodDatum> {

    MoodDatumDTO toDto(MoodDatum moodDatum);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    MoodDatum toEntity(MoodDatumDTO moodDatumDTO);

    default MoodDatum fromId(Long id) {
        if (id == null) {
            return null;
        }
        MoodDatum moodDatum = new MoodDatum();
        moodDatum.setId(id);
        return moodDatum;
    }
}
