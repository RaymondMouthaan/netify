package org.mouthaan.netify.soap.endpoint;

import lombok.AllArgsConstructor;
import org.mouthaan.namespace.netify.datatypes.movie.Movie;
import org.mouthaan.namespace.netify.datatypes.movie.Movies;
import org.mouthaan.namespace.netify.general.*;
import org.mouthaan.netify.common.mapper.GenreSoapMapper;
import org.mouthaan.netify.common.mapper.MovieSoapMapper;
import org.mouthaan.netify.common.mapper.RoleSoapMapper;
import org.mouthaan.netify.common.soap.MovieFacade;
import org.mouthaan.netify.domain.dao.SearchCriteria;
import org.mouthaan.netify.service.MovieService;
import org.mouthaan.netify.service.dto.GenreDto;
import org.mouthaan.netify.service.dto.MovieDto;
import org.mouthaan.netify.service.dto.RoleDto;
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
public class MovieEndpoint {
    private static final String NAMESPACE_URI = "http://mouthaan.org/namespace/netify/general";

    private final MovieFacade movieFacade;
    private final MessageSource messageSource;
    
    /**
     * This getMovieCount method returns total number of movies.
     *
     * @return getMovieCountResponse the number of movies.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetMovieCountRequest")
    @ResponsePayload
    public GetMovieCountResponse getMovieCount() {
        return movieFacade.getSoapMovieCount();
    }

    /**
     * This getMovieAll method returns all movies with optional filters.
     *
     * @param getMovieAllRequest optional filters.
     * @return getMovieAllResponse the list of movies.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetMovieAllRequest")
    @ResponsePayload
    public GetMovieAllResponse getMovieAll(@RequestPayload GetMovieAllRequest getMovieAllRequest) {
        return movieFacade.getSoapMovieAll(getMovieAllRequest);
    }

    /**
     * This getMovieById returns a movie based upon its id.
     *
     * @param getMovieByIdRequest a movie id.
     * @return getMovieByIdResponse the requested movie.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetMovieByIdRequest")
    @ResponsePayload
    public GetMovieByIdResponse getMovieById(@RequestPayload GetMovieByIdRequest getMovieByIdRequest) {
        return movieFacade.getSoapMovieById(getMovieByIdRequest);
    }

    /**
     * This addMovie method adds a new movie and returns added movie.
     *
     * @param addMovieRequest a movie to add.
     * @return addMovieResponse the added movie.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "AddMovieRequest")
    @ResponsePayload
    public AddMovieResponse addMovie(@RequestPayload AddMovieRequest addMovieRequest) {
        return movieFacade.addSoapMovie(addMovieRequest);
    }

    /**
     * This updateMovie method updates a movie and returns updated movie.
     * Todo : Update on basic info
     * @param updateMovieRequest a movie to updateMovieAddGenre.
     * @return updateMovieResponse the updated movie.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "UpdateMovieRequest")
    @ResponsePayload
    public UpdateMovieResponse updateMovie(@RequestPayload UpdateMovieRequest updateMovieRequest) {
        return movieFacade.updateSoapMovie(updateMovieRequest);
    }

    /**
     * This updateMovieAddGenre method updates a movie by adding genres and returns updated movie.
     *
     * @param updateMovieAddGenreRequest a movie id with a list of genres to add.
     * @return updateMovieAddGenreResponse the updated movie with added genres.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "UpdateMovieAddGenreRequest")
    @ResponsePayload
    public UpdateMovieAddGenreResponse updateMovieAddGenre(@RequestPayload UpdateMovieAddGenreRequest updateMovieAddGenreRequest) {
        return movieFacade.updateSoapMovieAddGenre(updateMovieAddGenreRequest);
    }

    /**
     * This updateMovieRemoveGenre method updates a movie by removing genres and returns updated movie.
     *
     * @param updateMovieRemoveGenreRequest a movie id with a list of genres to remove.
     * @return updateMovieRemoveGenreResponse the updated movie with genres removed.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "UpdateMovieRemoveGenreRequest")
    @ResponsePayload
    public UpdateMovieRemoveGenreResponse updateMovieRemoveGenre(@RequestPayload UpdateMovieRemoveGenreRequest updateMovieRemoveGenreRequest) {
        return movieFacade.updateSoapMovieRemoveGenre(updateMovieRemoveGenreRequest);
    }
    
    /**
     * This updateMovieAddCast method updates a movie by adding cast and returns updated movie.
     *
     * @param updateMovieAddCastRequest a movie id with a cast (list of roles) to add.
     * @return updateMovieAddCastResponse the updated movie with added cast.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "UpdateMovieAddCastRequest")
    @ResponsePayload
    public UpdateMovieAddCastResponse updateMovieAddCast(@RequestPayload UpdateMovieAddCastRequest updateMovieAddCastRequest) {
        return movieFacade.updateSoapMovieAddCast(updateMovieAddCastRequest);
    }

    /**
     * This updateMovieRemoveCast method updates a movie by removing cast (list of roles) and returns updated movie.
     *
     * @param updateMovieRemoveCastRequest a movie id with a cast (list of roles) to remove.
     * @return updateMovieRemoveCastResponse the updated movie with cast removed.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "UpdateMovieRemoveCastRequest")
    @ResponsePayload
    public UpdateMovieRemoveCastResponse updateMovieRemoveCast(@RequestPayload UpdateMovieRemoveCastRequest updateMovieRemoveCastRequest) {
        return movieFacade.updateSoapMovieRemoveCast(updateMovieRemoveCastRequest);
    }
    
    /**
     * This deleteMovie method deletes an movie based upon its id.
     *
     * @param deleteMovieRequest an movie id to delete.
     * @return deleteMovieResponse the deleted movie.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DeleteMovieRequest")
    @ResponsePayload
    public DeleteMovieResponse deleteMovie(@RequestPayload DeleteMovieRequest deleteMovieRequest) {
        return movieFacade.deleteSoapMovie(deleteMovieRequest);
    }
}
