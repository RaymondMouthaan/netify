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
public class MovieClient {
    
    private final WebServiceTemplate webServiceTemplate;

    @Autowired
    public MovieClient(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    public void setDefaultUri(String uri) {
        this.webServiceTemplate.setDefaultUri(uri);
    }

    public GetMovieCountResponse getMovieCount() {

        ObjectFactory objectFactory = new ObjectFactory();

        log.debug("Client sending GetMovieCountRequest");

        GetMovieCountResponse getMovieCountResponse = (GetMovieCountResponse) webServiceTemplate.marshalSendAndReceive(objectFactory.createGetMovieCountRequest(null));

        log.debug("Client received Movie [count={}]", getMovieCountResponse.getCount());

        return getMovieCountResponse;
    }
    
    public GetMovieByIdResponse getMovieById(int id) {
        ObjectFactory factory = new ObjectFactory();
        GetMovieByIdRequest getMovieByIdRequest = factory.createGetMovieByIdRequest();
        getMovieByIdRequest.setId(id);

        log.debug("Client sending GetMovieByIdRequest [id={}]", getMovieByIdRequest.getId());

        GetMovieByIdResponse getMovieByIdResponse = (GetMovieByIdResponse) webServiceTemplate.marshalSendAndReceive(getMovieByIdRequest);

        log.debug("Client received Movie [id={}, title={}]",
                getMovieByIdResponse.getMovie().getId(),
                getMovieByIdResponse.getMovie().getTitle()
        );

        return getMovieByIdResponse;
    }

    public GetMovieAllResponse getMovieAll(GetMovieAllRequest getMovieAllRequest) {

        log.debug("Client sending GetMovieAllRequest [title={}]", getMovieAllRequest.getFilters().getTitle());

        GetMovieAllResponse getMovieAllResponse = (GetMovieAllResponse) webServiceTemplate.marshalSendAndReceive(getMovieAllRequest);

        getMovieAllResponse.getMovies().getMovie().forEach(movie -> log.debug("Client received Movie [id={}, title={}]",
                movie.getId(),
                movie.getTitle()
        ));

        return getMovieAllResponse;
    }
    
    public AddMovieResponse addMovie(AddMovieRequest addMovieRequest) {


        log.debug("Client sending AddMovieRequest [title={}]", addMovieRequest.getMovie().getTitle());

        AddMovieResponse addMovieResponse = (AddMovieResponse) webServiceTemplate.marshalSendAndReceive(addMovieRequest);

        log.debug("Client received Movie [id={}, title={}]",
                addMovieResponse.getMovie().getId(),
                addMovieResponse.getMovie().getTitle()
        );
                
        return addMovieResponse;
    }

    public UpdateMovieResponse updateMovie(UpdateMovieRequest updateMovieRequest) {
        log.debug("Client sending UpdateMovieRequest [id={}, title={}]",
                updateMovieRequest.getMovie().getId(),
                updateMovieRequest.getMovie().getTitle()
        );

        UpdateMovieResponse updateMovieResponse = (UpdateMovieResponse) webServiceTemplate.marshalSendAndReceive(updateMovieRequest);

        log.debug("Client received Movie [id={}, title={}]",
                updateMovieResponse.getMovie().getId(),
                updateMovieResponse.getMovie().getTitle()
        );
        return updateMovieResponse;
    }

    public UpdateMovieAddGenreResponse updateMovieAddGenre(UpdateMovieAddGenreRequest updateMovieAddGenreRequest) {
        log.debug("Client sending UpdateMovieAddGenreRequest [movieId={}]",
                updateMovieAddGenreRequest.getMovieId()
        );

        UpdateMovieAddGenreResponse updateMovieAddGenreResponse = (UpdateMovieAddGenreResponse) webServiceTemplate.marshalSendAndReceive(updateMovieAddGenreRequest);

        log.debug("Client received Movie [id={}]",
                updateMovieAddGenreResponse.getMovie().getId()
        );
        return updateMovieAddGenreResponse;
    }

    public UpdateMovieRemoveGenreResponse updateMovieRemoveGenre(UpdateMovieRemoveGenreRequest updateMovieRemoveGenreRequest) {
        log.debug("Client sending UpdateMovieRemoveGenreRequest [movieId={}]",
                updateMovieRemoveGenreRequest.getMovieId()
        );

        UpdateMovieRemoveGenreResponse updateMovieRemoveGenreResponse = (UpdateMovieRemoveGenreResponse) webServiceTemplate.marshalSendAndReceive(updateMovieRemoveGenreRequest);

        log.debug("Client received Movie [id={}]",
                updateMovieRemoveGenreResponse.getMovie().getId()
        );
        return updateMovieRemoveGenreResponse;
    }

    public UpdateMovieAddCastResponse updateMovieAddCast(UpdateMovieAddCastRequest updateMovieAddCastRequest) {
        log.debug("Client sending UpdateMovieAddCastRequest [movieId={}]",
                updateMovieAddCastRequest.getMovieId()
        );

        UpdateMovieAddCastResponse updateMovieAddCastResponse = (UpdateMovieAddCastResponse) webServiceTemplate.marshalSendAndReceive(updateMovieAddCastRequest);

        log.debug("Client received Movie [id={}]",
                updateMovieAddCastResponse.getMovie().getId()
        );
        return updateMovieAddCastResponse;
    }

    public UpdateMovieRemoveCastResponse updateMovieRemoveCast(UpdateMovieRemoveCastRequest updateMovieRemoveCastRequest) {
        log.debug("Client sending UpdateMovieRemoveCastResponse [movieId={}]",
                updateMovieRemoveCastRequest.getMovieId()
        );

        UpdateMovieRemoveCastResponse updateMovieRemoveCastResponse = (UpdateMovieRemoveCastResponse) webServiceTemplate.marshalSendAndReceive(updateMovieRemoveCastRequest);

        log.debug("Client received Movie [id={}]",
                updateMovieRemoveCastResponse.getMovie().getId()
        );
        return updateMovieRemoveCastResponse;
    }

    public DeleteMovieResponse deleteMovie(DeleteMovieRequest deleteMovieRequest) {
        log.debug("Client sending DeleteMovieRequest [id={}]", deleteMovieRequest.getId());

        DeleteMovieResponse deleteMovieResponse = (DeleteMovieResponse) webServiceTemplate.marshalSendAndReceive(deleteMovieRequest);

        log.debug("Client received Movie [id={}]",
                deleteMovieResponse.getMovie().getId());

        return deleteMovieResponse;
    }



}
