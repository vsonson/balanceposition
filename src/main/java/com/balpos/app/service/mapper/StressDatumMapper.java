package com.balpos.app.service.mapper;

import com.balpos.app.domain.StressDatum;
import com.balpos.app.service.dto.StressDatumDTO;
import com.balpos.app.web.rest.mapper.UserEmailToUserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity StressDatum and its DTO StressDatumDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, UserEmailToUserMapper.class})
public interface StressDatumMapper extends EntityMapper<StressDatumDTO, StressDatum> {

    StressDatumDTO toDto(StressDatum stressDatum);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    StressDatum toEntity(StressDatumDTO stressDatumDTO);

    default StressDatum fromId(Long id) {
        if (id == null) {
            return null;
        }

        StressDatum stressDatum = new StressDatum();
        stressDatum.setId(id);
        return stressDatum;
    }
}
