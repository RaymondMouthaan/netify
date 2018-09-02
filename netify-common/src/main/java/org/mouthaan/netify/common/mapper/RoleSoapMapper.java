package org.mouthaan.netify.common.mapper;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.mouthaan.namespace.netify.datatypes.role.Role;
import org.mouthaan.netify.service.dto.RoleDto;
import org.springframework.stereotype.Component;

@Component
public class RoleSoapMapper extends ConfigurableMapper {
    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(Role.class, RoleDto.class)
                .byDefault()
                .register();
    }
}
