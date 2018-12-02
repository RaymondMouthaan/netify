package org.mouthaan.netify.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mouthaan.netify.domain.model.Bug;
import org.mouthaan.netify.service.dto.BugDto;

@Mapper
public interface BugServiceMapper {

    BugServiceMapper INSTANCE = Mappers.getMapper(BugServiceMapper.class);
    BugDto toBugDto(Bug bug);
    Bug toBug(BugDto bugDto);
}
