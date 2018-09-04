package com.balpos.app.service.mapper;

import com.balpos.app.domain.Note;
import com.balpos.app.service.dto.NoteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DataPointMapper.class, UserMapper.class})
public interface NoteMapper extends EntityMapper<NoteDTO, Note> {

    @Mapping(target = "dataPoint", source = "dataPointName")
    @Mapping(target = "user", ignore = true)
    Note toEntity(NoteDTO noteDTO);

    @Mapping(target = "dataPointName", source = "dataPoint.name")
    NoteDTO toDto(Note note);
}
