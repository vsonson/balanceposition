package com.balpos.app.service.mapper;

import com.balpos.app.domain.FocusDatum;
import com.balpos.app.service.dto.FocusDatumDTO;
import com.balpos.app.web.rest.mapper.UserEmailToUserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity FocusDatum and its DTO FocusDatumDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, UserEmailToUserMapper.class})
public interface FocusDatumMapper extends EntityMapper<FocusDatumDTO, FocusDatum> {

    FocusDatumDTO toDto(FocusDatum focusDatum);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    FocusDatum toEntity(FocusDatumDTO focusDatumDTO);

    default FocusDatum fromId(Long id) {
        if (id == null) {
            return null;
        }

        FocusDatum focusDatum = new FocusDatum();
        focusDatum.setId(id);
        return focusDatum;
    }
}
