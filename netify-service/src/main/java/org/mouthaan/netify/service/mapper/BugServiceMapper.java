package org.mouthaan.netify.service.mapper;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.mouthaan.netify.domain.model.Bug;
import org.mouthaan.netify.service.dto.BugDto;
import org.springframework.stereotype.Component;

@Component
public class BugServiceMapper extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(Bug.class, BugDto.class)
                .mapNulls(false).mapNullsInReverse(false) // don't map nulls in both directions
                .byDefault()
                .register();
    }
}
