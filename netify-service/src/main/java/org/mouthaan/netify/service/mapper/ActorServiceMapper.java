package org.mouthaan.netify.service.mapper;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.mouthaan.netify.domain.model.Actor;
import org.mouthaan.netify.service.dto.ActorDto;
import org.springframework.stereotype.Component;

@Component
public class ActorServiceMapper extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(Actor.class, ActorDto.class)
                .mapNulls(false).mapNullsInReverse(false) // don't map nulls in both directions
                .byDefault()
                .register();
    }
}
