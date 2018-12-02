package org.mouthaan.netify.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mouthaan.namespace.netify.datatypes.actor.Actor;
import org.mouthaan.netify.service.dto.ActorDto;

@Mapper
public interface ActorSoapMapper {

    ActorSoapMapper MAPPER = Mappers.getMapper(ActorSoapMapper.class);

    ActorDto toActorDto(Actor actor);

    Actor toActor(ActorDto actorDto);
}
