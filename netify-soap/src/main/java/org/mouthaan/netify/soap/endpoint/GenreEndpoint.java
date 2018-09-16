package org.mouthaan.netify.soap.endpoint;

import lombok.AllArgsConstructor;
import org.mouthaan.namespace.netify.general.*;
import org.mouthaan.netify.common.soap.GenreFacade;
import org.springframework.context.MessageSource;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@AllArgsConstructor
public class GenreEndpoint {
    
    private static final String NAMESPACE_URI = "http://mouthaan.org/namespace/netify/general";
    private final GenreFacade genreFacade;
    private final MessageSource messageSource;
    
    /**
     * This getGenreCount method returns total number of genres.
     *
     * @return getGenreCountResponse the number of genres.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetGenreCountRequest")
    @ResponsePayload
    public GetGenreCountResponse getGenreCount() {
        return genreFacade.getSoapGenreCount();
    }

    /**
     * This getGenreAll method returns all genres with optional filters.
     *
     * @param getGenreAllRequest optional filters.
     * @return getGenreAllResponse the list of genres.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetGenreAllRequest")
    @ResponsePayload
    public GetGenreAllResponse getGenreAll(@RequestPayload GetGenreAllRequest getGenreAllRequest) {
        return genreFacade.getSoapGenreAll(getGenreAllRequest);
    }

    /**
     * This getGenreById returns a genre based upon its id.
     *
     * @param getGenreByIdRequest a genre id.
     * @return getGenreByIdResponse the requested genre.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetGenreByIdRequest")
    @ResponsePayload
    public GetGenreByIdResponse getGenreById(@RequestPayload GetGenreByIdRequest getGenreByIdRequest) {
        return genreFacade.getSoapGenreById(getGenreByIdRequest);
    }

    /**
     * This addGenre method adds a new genre and returns added genre.
     *
     * @param addGenreRequest a genre to add.
     * @return addGenreResponse the added genre.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "AddGenreRequest")
    @ResponsePayload
    public AddGenreResponse addGenre(@RequestPayload AddGenreRequest addGenreRequest) {
        return genreFacade.addSoapGenre(addGenreRequest);
    }

    /**
     * This updateGenre method updates a genre and returns updated genre.
     *
     * @param updateGenreRequest a genre to updateMovieAddGenre.
     * @return updateGenreResponse the updated genre.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "UpdateGenreRequest")
    @ResponsePayload
    public UpdateGenreResponse updateGenre(@RequestPayload UpdateGenreRequest updateGenreRequest) {
        return genreFacade.updateSoapGenre(updateGenreRequest);
    }

    /**
     * This deleteGenre method deletes an genre based upon its id.
     *
     * @param deleteGenreRequest an genre id to delete.
     * @return deleteGenreResponse the deleted genre.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DeleteGenreRequest")
    @ResponsePayload
    public DeleteGenreResponse deleteGenre(@RequestPayload DeleteGenreRequest deleteGenreRequest) {
        return genreFacade.deleteSoapGenre(deleteGenreRequest);
    }
}