package org.mouthaan.netify.service.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mouthaan.netify.domain.model.Actor;
import org.mouthaan.netify.service.dto.ActorDto;

@Mapper
public interface ActorServiceMapper {

    ActorServiceMapper MAPPER = Mappers.getMapper(ActorServiceMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "gender", source = "gender")
    ActorDto toActorDto(Actor actor);

    @InheritInverseConfiguration
    Actor toActor(ActorDto actorDto);
}
