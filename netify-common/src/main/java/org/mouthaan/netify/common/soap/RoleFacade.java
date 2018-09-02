package org.mouthaan.netify.common.soap;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mouthaan.namespace.netify.datatypes.role.Cast;
import org.mouthaan.namespace.netify.datatypes.role.Role;
import org.mouthaan.namespace.netify.general.*;
import org.mouthaan.netify.common.mapper.RoleSoapMapper;
import org.mouthaan.netify.service.RoleService;
import org.mouthaan.netify.service.dto.CountDto;
import org.mouthaan.netify.service.dto.RoleDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@AllArgsConstructor
public class RoleFacade {
    private final RoleService roleService;
    private final RoleSoapMapper roleSoapMapper;

    public GetRoleCountResponse getSoapRoleCount() {
        CountDto countDto = roleService.countAll();
        GetRoleCountResponse getRoleCountResponse = new GetRoleCountResponse();
        getRoleCountResponse.setCount(countDto.getCount());
        return getRoleCountResponse;
    }

    public GetRoleAllResponse getSoapRoleAll(GetRoleAllRequest getRoleAllRequest) {
        Map<String,String> filterParams = new HashMap();
        if (null != getRoleAllRequest.getFilters()){
            if(null != getRoleAllRequest.getFilters().getCharacter())
                filterParams.put("character",getRoleAllRequest.getFilters().getCharacter());
            if(null != getRoleAllRequest.getFilters().getOrder())
                filterParams.put("order",getRoleAllRequest.getFilters().getOrder().toString());
            if(null != getRoleAllRequest.getFilters().getMovieId())
                filterParams.put("movieId",getRoleAllRequest.getFilters().getMovieId().toString());
            if(null != getRoleAllRequest.getFilters().getActorId())
                filterParams.put("actorId",getRoleAllRequest.getFilters().getActorId().toString());
        }

        List<RoleDto> castDto = this.roleService.findAll(filterParams);

        GetRoleAllResponse getRoleAllResponse = new GetRoleAllResponse();
        getRoleAllResponse.setCast(new Cast());
        castDto.forEach(roleDto -> {
            // Add roleDto to response
            getRoleAllResponse.getCast().getRole().add(roleSoapMapper.map(roleDto, Role.class));
        });
        return getRoleAllResponse;
    }

    public GetRoleByIdResponse getSoapRoleById(GetRoleByIdRequest getRoleByIdRequest) {
        // Find role by id
        RoleDto roleDto = roleService.findById(getRoleByIdRequest.getId());

        // Add roleDto to response
        GetRoleByIdResponse getRoleByIdResponse = new GetRoleByIdResponse();
        getRoleByIdResponse.setCast(new Cast());
        getRoleByIdResponse.getCast().getRole().add(roleSoapMapper.map(roleDto, Role.class));
        return getRoleByIdResponse;
    }
}
