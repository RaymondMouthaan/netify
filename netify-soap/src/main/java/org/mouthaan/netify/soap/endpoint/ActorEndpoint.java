package org.mouthaan.netify.soap.endpoint;

import lombok.AllArgsConstructor;
import org.mouthaan.namespace.netify.general.*;
import org.mouthaan.netify.common.soap.ActorFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@AllArgsConstructor
public class ActorEndpoint {

    private static final String NAMESPACE_URI = "http://mouthaan.org/namespace/netify/general";
    private final ActorFacade actorFacade;
    private final MessageSource messageSource;


    private static final Logger LOGGER = LoggerFactory.getLogger(ActorEndpoint.class);

    /**
     * This getActorCount method returns total number of actors.
     *
     * @return getActorCountResponse the number of actors.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetActorCountRequest")
    @ResponsePayload
    public GetActorCountResponse getActorCount() {
        return actorFacade.getSoapActorCount();
    }

    /**
     * This getActorAll method returns all actors with optional filters.
     *
     * @param getActorAllRequest optional filters.
     * @return getActorAllResponse the list of actors.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetActorAllRequest")
    @ResponsePayload
    public GetActorAllResponse getActorAll(@RequestPayload GetActorAllRequest getActorAllRequest) {
        return actorFacade.getSoapActorAll(getActorAllRequest);
    }

    /**
     * This getActorById returns an actor based upon its id.
     *
     * @param getActorByIdRequest an actor id.
     * @return getActorByIdResponse the requested actor.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetActorByIdRequest")
    @ResponsePayload
    public GetActorByIdResponse getActorById(@RequestPayload GetActorByIdRequest getActorByIdRequest) {
        return actorFacade.getGetActorById(getActorByIdRequest);
    }

    /**
     * This addActor method adds a new actor and returns added actor.
     *
     * @param addActorRequest an actor to add.
     * @return addActorResponse the added actor.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "AddActorRequest")
    @ResponsePayload
    public AddActorResponse addActor(@RequestPayload AddActorRequest addActorRequest) {
        return actorFacade.addSoapActor(addActorRequest);
    }

    /**
     * This updateActor method updates an actor and returns updated actor.
     *
     * @param updateActorRequest an actor to updateMovieAddGenre.
     * @return updateActorResponse the updated actor.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "UpdateActorRequest")
    @ResponsePayload
    public UpdateActorResponse updateActor(@RequestPayload UpdateActorRequest updateActorRequest) {
        return actorFacade.updateSoapActor(updateActorRequest);

    }

    /**
     * This deleteActor method deletes an actor based upon its id.
     *
     * @param deleteActorRequest an actor id to delete.
     * @return deleteActorResponse the deleted actor.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DeleteActorRequest")
    @ResponsePayload
    public DeleteActorResponse deleteActor(@RequestPayload DeleteActorRequest deleteActorRequest) {
        return actorFacade.deleteSoapActor(deleteActorRequest);
    }
}
