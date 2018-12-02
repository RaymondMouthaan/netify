package org.mouthaan.netify.service.mapper;

import org.junit.jupiter.api.Test;
import org.mouthaan.netify.domain.model.Actor;
import org.mouthaan.netify.domain.model.Gender;
import org.mouthaan.netify.service.dto.ActorDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ActorServiceMapperTest {
    @Test
    void mapActorToActorDtoTest() {

        // given
        Actor actor = new Actor();
        actor.setId(1);
        actor.setGender(Gender.MALE);
        actor.setName("Raymond");

        // when
        ActorDto actorDto = ActorServiceMapper.MAPPER.toActorDto(actor);

        // then
        assertNotNull(actorDto);
        assertEquals(actor.getId(), actorDto.getId());
        assertEquals(actor.getName(), actorDto.getName());
        assertEquals(actor.getGender(), actorDto.getGender());

    }

    @Test
    void mapActorDtoToActorTest() {

        // given
        ActorDto actorDto = new ActorDto();
        actorDto.setId(1);
        actorDto.setGender(Gender.MALE);
        actorDto.setName("Raymond");

        // when
        Actor actor = ActorServiceMapper.MAPPER.toActor(actorDto);

        // then
        assertNotNull(actorDto);
        assertEquals(actorDto.getId(), actor.getId());
        assertEquals(actorDto.getName(), actor.getName());
        assertEquals(actorDto.getGender(), actor.getGender());

    }
}