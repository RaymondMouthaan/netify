package org.mouthaan.netify.soap.client;

import lombok.extern.slf4j.Slf4j;
import org.mouthaan.namespace.netify.general.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

@Component
@Slf4j
public class RoleClient {

    private final WebServiceTemplate webServiceTemplate;

    @Autowired
    public RoleClient(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    public void setDefaultUri(String uri) {
        this.webServiceTemplate.setDefaultUri(uri);
    }

    public GetRoleCountResponse getRoleCount() {

        ObjectFactory objectFactory = new ObjectFactory();

        log.debug("Client sending GetRoleCountRequest");

        GetRoleCountResponse getRoleCountResponse = (GetRoleCountResponse) webServiceTemplate.marshalSendAndReceive(objectFactory.createGetRoleCountRequest(null));

        log.debug("Client received Role [count={}]", getRoleCountResponse.getCount());

        return getRoleCountResponse;
    }

    public GetRoleByIdResponse getRoleById(int id) {
        ObjectFactory factory = new ObjectFactory();
        GetRoleByIdRequest getRoleByIdRequest = factory.createGetRoleByIdRequest();
        getRoleByIdRequest.setId(id);

        log.debug("Client sending GetRoleByIdRequest [id={}]", getRoleByIdRequest.getId());

        GetRoleByIdResponse getRoleByIdResponse = (GetRoleByIdResponse) webServiceTemplate.marshalSendAndReceive(getRoleByIdRequest);

        return getRoleByIdResponse;
    }

    public GetRoleAllResponse getRoleAll(GetRoleAllRequest getRoleAllRequest) {

        log.debug("Client sending GetRoleAllRequest [character={}, order={}, actorId={}, movieId={}]",
                getRoleAllRequest.getFilters().getCharacter(),
                getRoleAllRequest.getFilters().getOrder(),
                getRoleAllRequest.getFilters().getActorId(),
                getRoleAllRequest.getFilters().getMovieId()
        );

        GetRoleAllResponse getRoleAllResponse = (GetRoleAllResponse) webServiceTemplate.marshalSendAndReceive(getRoleAllRequest);

        return getRoleAllResponse;
    }
}
