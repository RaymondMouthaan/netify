package org.mouthaan.netify.soap.client;

import lombok.extern.slf4j.Slf4j;
import org.mouthaan.namespace.netify.general.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

@Component
@Slf4j
public class ActorClient {

    private final WebServiceTemplate webServiceTemplate;

    @Autowired
    public ActorClient(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    public void setDefaultUri(String uri) {
        this.webServiceTemplate.setDefaultUri(uri);
    }

    public GetActorCountResponse getActorCount() {

        ObjectFactory objectFactory = new ObjectFactory();

        log.debug("Client sending GetActorCountRequest");

        GetActorCountResponse getActorCountResponse = (GetActorCountResponse) webServiceTemplate.marshalSendAndReceive(objectFactory.createGetActorCountRequest(null));

        log.debug("Client received Actor [count={}]", getActorCountResponse.getCount());

        return getActorCountResponse;
    }

    public GetActorByIdResponse getActorById(int id) {
        ObjectFactory factory = new ObjectFactory();
        GetActorByIdRequest getActorByIdRequest = factory.createGetActorByIdRequest();
        getActorByIdRequest.setId(id);

        log.debug("Client sending GetActorByIdRequest [id={}]", getActorByIdRequest.getId());

        GetActorByIdResponse getActorByIdResponse = (GetActorByIdResponse) webServiceTemplate.marshalSendAndReceive(getActorByIdRequest);

        log.debug("Client received Actor [id={}, name={}, gender={}]",
                getActorByIdResponse.getActor().getId(),
                getActorByIdResponse.getActor().getName(),
                getActorByIdResponse.getActor().getGender());

        return getActorByIdResponse;
    }

    public GetActorAllResponse getActorAll(GetActorAllRequest getActorAllRequest) {

        log.debug("Client sending GetActorAllRequest [name={}, gender={}]",
                getActorAllRequest.getFilters().getName(),
                getActorAllRequest.getFilters().getGender()
        );

        GetActorAllResponse getActorAllResponse = (GetActorAllResponse) webServiceTemplate.marshalSendAndReceive(getActorAllRequest);

        getActorAllResponse.getActors().getActor().forEach(actor -> log.debug("Client received Actor [id={}, name={}, gender={}]",
                actor.getId(),
                actor.getName(),
                actor.getGender()
        ));

        return getActorAllResponse;
    }

    public AddActorResponse addActor(AddActorRequest addActorRequest) {
        log.debug("Client sending AddActorRequest [name={}, gender={}]",
                addActorRequest.getActor().getName(),
                addActorRequest.getActor().getGender()
        );

        AddActorResponse addActorResponse = (AddActorResponse) webServiceTemplate.marshalSendAndReceive(addActorRequest);

        log.debug("Client received Actor [id={}, name={}, gender={}]",
                addActorResponse.getActor().getId(),
                addActorResponse.getActor().getName(),
                addActorResponse.getActor().getGender());

        return addActorResponse;
    }

    public UpdateActorResponse updateActor(UpdateActorRequest updateActorRequest) {
        log.debug("Client sending UpdateActorRequest [id={}, name={}, gender={}]",
                updateActorRequest.getActor().getId(),
                updateActorRequest.getActor().getName(),
                updateActorRequest.getActor().getGender()
        );

        UpdateActorResponse updateActorResponse = (UpdateActorResponse) webServiceTemplate.marshalSendAndReceive(updateActorRequest);

        log.debug("Client received Actor [id={}, name={}, gender={}]",
                updateActorResponse.getActor().getId(),
                updateActorResponse.getActor().getName(),
                updateActorResponse.getActor().getGender());

        return updateActorResponse;
    }

    public DeleteActorResponse deleteActor(DeleteActorRequest deleteActorRequest) {
        log.debug("Client sending DeleteActorRequest [id={}]", deleteActorRequest.getId());

        DeleteActorResponse deleteActorResponse = (DeleteActorResponse) webServiceTemplate.marshalSendAndReceive(deleteActorRequest);

        log.debug("Client received Actor [id={}]", deleteActorResponse.getActor().getId());

        return deleteActorResponse;
    }
}
