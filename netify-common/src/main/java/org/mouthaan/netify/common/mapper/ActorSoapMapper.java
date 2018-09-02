package org.mouthaan.netify.common.mapper;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.mouthaan.namespace.netify.datatypes.actor.Actor;
import org.mouthaan.netify.service.dto.ActorDto;
import org.springframework.stereotype.Component;

@Component
public class ActorSoapMapper extends ConfigurableMapper {
    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(Actor.class, ActorDto.class)
                .byDefault()
                .register();
    }
}
