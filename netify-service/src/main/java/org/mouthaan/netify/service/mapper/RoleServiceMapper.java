package org.mouthaan.netify.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mouthaan.netify.domain.model.Role;
import org.mouthaan.netify.service.dto.RoleDto;

@Mapper
public interface RoleServiceMapper {

    RoleServiceMapper MAPPER = Mappers.getMapper(RoleServiceMapper.class);

    RoleDto toRoleDto(Role role);

    Role toRole(RoleDto roleDto);
}
