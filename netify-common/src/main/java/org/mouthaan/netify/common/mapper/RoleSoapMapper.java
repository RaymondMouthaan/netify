package org.mouthaan.netify.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mouthaan.namespace.netify.datatypes.role.Role;
import org.mouthaan.netify.service.dto.RoleDto;

@Mapper
public interface RoleSoapMapper {

    RoleSoapMapper MAPPER = Mappers.getMapper(RoleSoapMapper.class);

    RoleDto toRoleDto(Role role);

    Role toRole(RoleDto roleDto);
}
