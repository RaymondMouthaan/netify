package org.mouthaan.netify.common.mapper;

import org.junit.jupiter.api.Test;
import org.mouthaan.namespace.netify.datatypes.actor.Actor;
import org.mouthaan.namespace.netify.datatypes.actor.Gender;
import org.mouthaan.netify.service.dto.ActorDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ActorSoapMapperTest {
    @Test
    void mapActorToActorDtoTest() {

        // Given
        Actor actor = new Actor();
        actor.setId(1);
        actor.setGender(Gender.MALE);
        actor.setName("Raymond");

        // When
        ActorDto actorDto = ActorSoapMapper.MAPPER.toActorDto(actor);

        // Then
        assertNotNull(actorDto);
        assertEquals(actor.getId(), actorDto.getId());
        assertEquals(actor.getName(), actorDto.getName());
        assertEquals(actor.getGender().value(), actorDto.getGender().value());

    }

    @Test
    void mapActorDtoToActorTest() {

        // given
        ActorDto actorDto = new ActorDto();
        actorDto.setId(1);
        actorDto.setGender(org.mouthaan.netify.domain.model.Gender.MALE);
        actorDto.setName("Raymond");

        // when
        Actor actor = ActorSoapMapper.MAPPER.toActor(actorDto);

        // then
        assertNotNull(actorDto);
        assertEquals(actorDto.getId(), actor.getId());
        assertEquals(actorDto.getName(), actor.getName());
        assertEquals(actorDto.getGender().value(), actor.getGender().value());

    }
}