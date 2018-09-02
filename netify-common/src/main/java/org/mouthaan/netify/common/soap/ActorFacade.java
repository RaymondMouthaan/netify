package org.mouthaan.netify.common.soap;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mouthaan.namespace.netify.datatypes.actor.Actor;
import org.mouthaan.namespace.netify.datatypes.actor.Actors;
import org.mouthaan.namespace.netify.general.*;
import org.mouthaan.netify.common.mapper.ActorSoapMapper;
import org.mouthaan.netify.service.ActorService;
import org.mouthaan.netify.service.dto.ActorDto;
import org.mouthaan.netify.service.dto.CountDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@AllArgsConstructor
public class ActorFacade {
    private final ActorService actorService;
    private final ActorSoapMapper actorSoapMapper;

    public GetActorCountResponse getSoapActorCount() {
        CountDto countDto = actorService.countAll();
        GetActorCountResponse getActorCountResponse = new GetActorCountResponse();
        getActorCountResponse.setCount(countDto.getCount());
        return getActorCountResponse;
    }

    public GetActorAllResponse getSoapActorAll(GetActorAllRequest getActorAllRequest) {
        Map<String,String> filterParams = new HashMap();
        if (null != getActorAllRequest.getFilters()) {
            if (getActorAllRequest.getFilters().getName() != null)
                filterParams.put("name",getActorAllRequest.getFilters().getName());
            if (getActorAllRequest.getFilters().getGender() != null)
                filterParams.put("gender",getActorAllRequest.getFilters().getGender().toUpperCase());
        }

        List<ActorDto> actorDtos = this.actorService.findAll(filterParams);

        GetActorAllResponse getActorAllResponse = new GetActorAllResponse();
        getActorAllResponse.setActors(new Actors());
        actorDtos.forEach(actorDto -> {
            // Add actorDto to response
            getActorAllResponse.getActors().getActor().add(actorSoapMapper.map(actorDto, Actor.class));
        });
        return getActorAllResponse;
    }

    public GetActorByIdResponse getGetActorById(GetActorByIdRequest getActorByIdRequest) {
        // Find actor by id
        ActorDto actorDto = actorService.findById(getActorByIdRequest.getId());

        // Add actorDto to response
        GetActorByIdResponse getActorByIdResponse = new GetActorByIdResponse();
        getActorByIdResponse.setActor(actorSoapMapper.map(actorDto, Actor.class));
        return getActorByIdResponse;
    }

    public AddActorResponse addSoapActor(AddActorRequest addActorRequest) {
        ActorDto actorDto = actorSoapMapper.map(addActorRequest.getActor(), ActorDto.class);

        // Add actorDto to addActorResponse
        AddActorResponse addActorResponse = new AddActorResponse();
        addActorResponse.setActor(actorSoapMapper.map(actorService.add(actorDto), Actor.class));
        return addActorResponse;
    }

    public UpdateActorResponse updateSoapActor(UpdateActorRequest updateActorRequest) {
        ActorDto actorDto = actorService.update(updateActorRequest.getActor().getId(),
                actorSoapMapper.map(updateActorRequest.getActor(), ActorDto.class));

        // Add actorDto to response
        UpdateActorResponse updateActorResponse = new UpdateActorResponse();
        updateActorResponse.setActor(actorSoapMapper.map(actorDto, Actor.class));
        return updateActorResponse;
    }

    public DeleteActorResponse deleteSoapActor(DeleteActorRequest deleteActorRequest) {
        actorService.delete(deleteActorRequest.getId());

        DeleteActorResponse deleteActorResponse = new DeleteActorResponse();
        Actor actor = new Actor();
        actor.setId(deleteActorRequest.getId());
        deleteActorResponse.setActor(actor);
        return deleteActorResponse;
    }
}
