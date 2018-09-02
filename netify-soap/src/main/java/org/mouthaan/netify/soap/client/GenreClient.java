package org.mouthaan.netify.soap.client;

import lombok.extern.slf4j.Slf4j;
import org.mouthaan.namespace.netify.general.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

@Component
@Slf4j
public class GenreClient {

    private final WebServiceTemplate webServiceTemplate;

    @Autowired
    public GenreClient(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    public void setDefaultUri(String uri) {
        this.webServiceTemplate.setDefaultUri(uri);
    }

    public GetGenreCountResponse getGenreCount() {

        ObjectFactory objectFactory = new ObjectFactory();

        log.debug("Client sending GetGenreCountRequest");

        GetGenreCountResponse getGenreCountResponse = (GetGenreCountResponse) webServiceTemplate.marshalSendAndReceive(objectFactory.createGetGenreCountRequest(null));

        log.debug("Client received Genre [count={}]", getGenreCountResponse.getCount());

        return getGenreCountResponse;
    }
    
    public GetGenreByIdResponse getGenreById(int id) {
        ObjectFactory factory = new ObjectFactory();
        GetGenreByIdRequest getGenreByIdRequest = factory.createGetGenreByIdRequest();
        getGenreByIdRequest.setId(id);

        log.debug("Client sending GetGenreByIdRequest [id={}]", getGenreByIdRequest.getId());

        GetGenreByIdResponse getGenreByIdResponse = (GetGenreByIdResponse) webServiceTemplate.marshalSendAndReceive(getGenreByIdRequest);

        log.debug("Client received Genre [id={}, name={}]",
                getGenreByIdResponse.getGenre().getId(),
                getGenreByIdResponse.getGenre().getName()
        );

        return getGenreByIdResponse;
    }

    public GetGenreAllResponse getGenreAll(GetGenreAllRequest getGenreAllRequest) {

        log.debug("Client sending GetGenreAllRequest [name={}]", getGenreAllRequest.getFilters().getName());

        GetGenreAllResponse getGenreAllResponse = (GetGenreAllResponse) webServiceTemplate.marshalSendAndReceive(getGenreAllRequest);

        getGenreAllResponse.getGenres().getGenre().forEach(genre -> log.debug("Client received Genre [id={}, name={}]",
                genre.getId(),
                genre.getName()
        ));

        return getGenreAllResponse;
    }
    
    public AddGenreResponse addGenre(AddGenreRequest addGenreRequest) {
        log.debug("Client sending AddGenreRequest [name={}]", addGenreRequest.getGenre().getName());

        AddGenreResponse addGenreResponse = (AddGenreResponse) webServiceTemplate.marshalSendAndReceive(addGenreRequest);

        log.debug("Client received Genre [id={}, name={}]",
                addGenreResponse.getGenre().getId(),
                addGenreResponse.getGenre().getName()
        );

        return addGenreResponse;
    }

    public UpdateGenreResponse updateGenre(UpdateGenreRequest updateGenreRequest) {
        log.debug("Client sending UpdateGenreRequest [id={}, name={}]",
                updateGenreRequest.getGenre().getId(),
                updateGenreRequest.getGenre().getName()
        );

        UpdateGenreResponse updateGenreResponse = (UpdateGenreResponse) webServiceTemplate.marshalSendAndReceive(updateGenreRequest);

        log.debug("Client received Genre [id={}, name={}]",
                updateGenreResponse.getGenre().getId(),
                updateGenreResponse.getGenre().getName()
        );
        return updateGenreResponse;
    }

    public DeleteGenreResponse deleteGenre(DeleteGenreRequest deleteGenreRequest) {
        log.debug("Client sending DeleteGenreRequest [id={}]", deleteGenreRequest.getId());

        DeleteGenreResponse deleteGenreResponse = (DeleteGenreResponse) webServiceTemplate.marshalSendAndReceive(deleteGenreRequest);

        log.debug("Client received Genre [id={}, name={}]",
                deleteGenreResponse.getGenre().getId(),
                deleteGenreResponse.getGenre().getName());

        return deleteGenreResponse;
    }
}
