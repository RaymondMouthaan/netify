package org.mouthaan.netify.soap.endpoint;

import lombok.AllArgsConstructor;
import org.mouthaan.namespace.netify.datatypes.role.Cast;
import org.mouthaan.namespace.netify.datatypes.role.Role;
import org.mouthaan.namespace.netify.general.*;
import org.mouthaan.netify.common.mapper.RoleSoapMapper;
import org.mouthaan.netify.common.soap.RoleFacade;
import org.mouthaan.netify.domain.dao.SearchCriteria;
import org.mouthaan.netify.service.RoleService;
import org.mouthaan.netify.service.dto.RoleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Endpoint
@AllArgsConstructor
public class RoleEndpoint {

    private static final String NAMESPACE_URI = "http://duo.nl/namespace/netify/general";
    private final RoleFacade roleFacade;
    private final MessageSource messageSource;

    /**
     * This getRoleCount method returns total number of roles.
     *
     * @return getRoleCountResponse the number of roles.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetRoleCountRequest")
    @ResponsePayload
    public GetRoleCountResponse getRoleCount() {
        return roleFacade.getSoapRoleCount();
    }

    /**
     * This getRoleAll method returns all roles with optional filters.
     *
     * @param getRoleAllRequest optional filters.
     * @return getRoleAllResponse the list of roles.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetRoleAllRequest")
    @ResponsePayload
    public GetRoleAllResponse getRoleAll(@RequestPayload GetRoleAllRequest getRoleAllRequest) {
        return roleFacade.getSoapRoleAll(getRoleAllRequest);
    }

    /**
     * This getRoleById returns a role based upon its id.
     *
     * @param getRoleByIdRequest a role id.
     * @return getRoleByIdResponse the requested role.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetRoleByIdRequest")
    @ResponsePayload
    public GetRoleByIdResponse getRoleById(@RequestPayload GetRoleByIdRequest getRoleByIdRequest) {
        return roleFacade.getSoapRoleById(getRoleByIdRequest);
    }
}
