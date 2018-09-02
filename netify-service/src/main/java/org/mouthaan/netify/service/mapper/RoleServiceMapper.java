package org.mouthaan.netify.service.mapper;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.mouthaan.netify.domain.model.Role;
import org.mouthaan.netify.service.dto.RoleDto;
import org.springframework.stereotype.Component;

@Component
public class RoleServiceMapper extends ConfigurableMapper {
    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(Role.class, RoleDto.class)
//                .field("genres", "genres")
//                .field("cast", "cast")
                .mapNulls(false).mapNullsInReverse(false) // don't map nulls in both directions
                .byDefault()
                .register();
    }
}
