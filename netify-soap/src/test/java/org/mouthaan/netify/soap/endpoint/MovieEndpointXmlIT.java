package org.mouthaan.netify.soap.endpoint;

import org.mouthaan.namespace.netify.general.AddMovieRequest;
import org.mouthaan.namespace.netify.general.AddMovieResponse;
import org.mouthaan.namespace.netify.general.DeleteMovieRequest;
import org.mouthaan.namespace.netify.general.DeleteMovieResponse;
import org.mouthaan.namespace.netify.general.GetMovieAllRequest;
import org.mouthaan.namespace.netify.general.GetMovieAllResponse;
import org.mouthaan.namespace.netify.general.GetMovieByIdRequest;
import org.mouthaan.namespace.netify.general.GetMovieByIdResponse;
import org.mouthaan.namespace.netify.general.GetMovieCountResponse;
import org.mouthaan.namespace.netify.general.UpdateMovieAddCastRequest;
import org.mouthaan.namespace.netify.general.UpdateMovieAddCastResponse;
import org.mouthaan.namespace.netify.general.UpdateMovieAddGenreRequest;
import org.mouthaan.namespace.netify.general.UpdateMovieAddGenreResponse;
import org.mouthaan.namespace.netify.general.UpdateMovieRemoveCastRequest;
import org.mouthaan.namespace.netify.general.UpdateMovieRemoveCastResponse;
import org.mouthaan.namespace.netify.general.UpdateMovieRemoveGenreRequest;
import org.mouthaan.namespace.netify.general.UpdateMovieRemoveGenreResponse;
import org.mouthaan.namespace.netify.general.UpdateMovieRequest;
import org.mouthaan.namespace.netify.general.UpdateMovieResponse;
import org.mouthaan.netify.common.util.XmlUtils;
import org.mouthaan.netify.common.util.XmlUtilsException;
import org.mouthaan.netify.soap.app.NetifySoapApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;


/**
 * Movie Xml Integration Test
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = NetifySoapApplication.class)
@ActiveProfiles(profiles = {"local"})
//@Slf4j
public class MovieEndpointXmlIT {
    @Autowired
    private MovieEndpoint movieEndpoint;

    @Value(value = "classpath:soap/requests/movie/GetMovieCountRequestTest.xml")
    private Resource getMovieCountRequestTestXml;

    @Value(value = "classpath:soap/requests/movie/GetMovieByIdRequestTest.xml")
    private Resource getMovieByIdRequestTestXml;

    @Value(value = "classpath:soap/requests/movie/GetMovieByIdForUpdatesRequestTest.xml")
    private Resource getMovieByIdForUpdatesRequestTestXml;

    @Value(value = "classpath:soap/requests/movie/GetMovieAllRequestFilterTest.xml")
    private Resource getMovieAllRequestFilterTestXml;

    @Value(value = "classpath:soap/requests/movie/GetMovieAllRequestNoFilterTest.xml")
    private Resource getMovieAllRequestNoFilterTestXml;

    @Value(value = "classpath:soap/requests/movie/AddMovieRequestTest.xml")
    private Resource addMovieRequestTestXml;

    @Value(value = "classpath:soap/requests/movie/UpdateMovieRequestTest.xml")
    private Resource updateMovieRequestTestXml;

    @Value(value = "classpath:soap/requests/movie/UpdateMovieAddCastRequestTest.xml")
    private Resource updateMovieAddCastRequestTestXml;

    @Value(value = "classpath:soap/requests/movie/UpdateMovieRemoveCastRequestTest.xml")
    private Resource updateMovieRemoveCastRequestTestXml;

    @Value(value = "classpath:soap/requests/movie/UpdateMovieAddGenreRequestTest.xml")
    private Resource updateMovieAddGenreRequestTestXml;

    @Value(value = "classpath:soap/requests/movie/UpdateMovieRemoveGenreRequestTest.xml")
    private Resource updateMovieRemoveGenreRequestTestXml;

    @Value(value = "classpath:soap/requests/movie/DeleteMovieRequestTest.xml")
    private Resource deleteMovieRequestTestXml;

    @Test
    public void shouldGetMovieCountXmlRequest() throws IOException, XmlUtilsException {
        // Given
        //XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(getMovieCountRequestTestXml.getInputStream(), Object.class);

        // When
        GetMovieCountResponse getMovieCountResponse = movieEndpoint.getMovieCount();

        // Then
        assertThat(getMovieCountResponse, is(notNullValue()));
        assertThat(getMovieCountResponse.getCount(), greaterThan(200L));
    }

    @Test
    public void shouldGetMovieByIdXmlRequest() throws IOException, XmlUtilsException {
        // Given
        GetMovieByIdRequest getMovieByIdRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(getMovieByIdRequestTestXml.getInputStream(), GetMovieByIdRequest.class);

        // When
        GetMovieByIdResponse getMovieByIdResponse = movieEndpoint.getMovieById(getMovieByIdRequest);

        // Then
        assertThat(getMovieByIdResponse, is(notNullValue()));
        assertThat(getMovieByIdResponse.getMovie().getId(), is(33));
        assertThat(getMovieByIdResponse.getMovie().getTitle(), is("Unforgiven"));
    }

    @Test
    public void shouldGetMovieAllFilterXmlRequest() throws IOException, XmlUtilsException {
        // Given
        GetMovieAllRequest getMovieAllRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(getMovieAllRequestFilterTestXml.getInputStream(), GetMovieAllRequest.class);

        // When
        GetMovieAllResponse getMovieAllResponse = movieEndpoint.getMovieAll(getMovieAllRequest);

        // Then
        assertThat(getMovieAllResponse, is(notNullValue()));
        assertThat(getMovieAllResponse.getMovies().getMovie().get(0).getId(), is(notNullValue()));
        assertThat(getMovieAllResponse.getMovies().getMovie().get(0).getTitle(), is("Unforgiven"));
        assertThat(getMovieAllResponse.getMovies().getMovie().get(0).getRuntime(), is(131));
        assertThat(getMovieAllResponse.getMovies().getMovie().get(0).getGenres(), is(notNullValue()));
        assertThat(getMovieAllResponse.getMovies().getMovie().get(0).getCast(), is(notNullValue()));
    }

    @Test
    public void shouldGetMovieAllNoFilterXmlRequest() throws IOException, XmlUtilsException {
        // Given
        GetMovieAllRequest getMovieAllRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(getMovieAllRequestNoFilterTestXml.getInputStream(), GetMovieAllRequest.class);

        // When
        GetMovieAllResponse getMovieAllResponse = movieEndpoint.getMovieAll(getMovieAllRequest);

        // Then
        assertThat(getMovieAllResponse, is(notNullValue()));
        assertThat(getMovieAllResponse.getMovies().getMovie().size(), greaterThan(240));
        assertThat(getMovieAllResponse.getMovies().getMovie().get(0).getId(), is(notNullValue()));

        assertThat(getMovieAllResponse.getMovies().getMovie().get(1).getId(), is(notNullValue()));

        assertThat(getMovieAllResponse.getMovies().getMovie().get(2).getId(), is(notNullValue()));
    }

    @Test
    public void shouldAddAndDeleteMovieXmlRequest() throws IOException, XmlUtilsException {
        // Add Movie Test
        // Given
        AddMovieRequest addMovieRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(addMovieRequestTestXml.getInputStream(), AddMovieRequest.class);

        // When
        AddMovieResponse addMovieResponse = movieEndpoint.addMovie(addMovieRequest);

        // Then
        assertThat(addMovieResponse, is(notNullValue()));
        assertThat(addMovieResponse.getMovie().getId(), is(notNullValue()));
        assertThat(addMovieResponse.getMovie().getTitle(), is("Star Wars I"));
        assertThat(addMovieResponse.getMovie().getGenres().getGenre().get(0).getId(), is(notNullValue()));
        assertThat(addMovieResponse.getMovie().getGenres().getGenre().get(0).getName(), is("Drama"));
        assertThat(addMovieResponse.getMovie().getCast().getRole().get(0).getCharacter(), is("Yolo"));
        assertThat(addMovieResponse.getMovie().getCast().getRole().get(0).getActor().getName(), is("Chris Brown"));

        // Delete Movie Test
        // Given
        DeleteMovieRequest deleteMovieRequest = new DeleteMovieRequest();
        deleteMovieRequest.setId(addMovieResponse.getMovie().getId());

        // When
        DeleteMovieResponse deleteMovieResponse = movieEndpoint.deleteMovie(deleteMovieRequest);

        // Then
        assertThat(deleteMovieResponse, is(notNullValue()));
        assertThat(deleteMovieResponse.getMovie().getId(), is(addMovieResponse.getMovie().getId()));
    }


    @Test
    public void shouldUpdateMovieXmlRequest() throws IOException, XmlUtilsException {
        // Given
        UpdateMovieRequest updateMovieRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(updateMovieRequestTestXml.getInputStream(), UpdateMovieRequest.class);

        // When
        UpdateMovieResponse updateMovieResponse = movieEndpoint.updateMovie(updateMovieRequest);

        // Then
        assertThat(updateMovieResponse, is(notNullValue()));
        assertThat(updateMovieRequest.getMovie().getId(), is(11));
        assertThat(updateMovieResponse.getMovie().getTitle(), is("Star Wars"));
        assertThat(updateMovieResponse.getMovie().getRuntime(), is(150));
    }

    @Test
    public void shouldUpdateMovieAddAndremoveCastXmlRequest() throws IOException, XmlUtilsException {
        // Given
        UpdateMovieAddCastRequest updateMovieAddCastRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(updateMovieAddCastRequestTestXml.getInputStream(), UpdateMovieAddCastRequest.class);
        UpdateMovieRemoveCastRequest updateMovieRemoveCastRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(updateMovieRemoveCastRequestTestXml.getInputStream(), UpdateMovieRemoveCastRequest.class);
        GetMovieByIdRequest getMovieByIdRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(getMovieByIdForUpdatesRequestTestXml.getInputStream(), GetMovieByIdRequest.class);
        GetMovieByIdResponse getMovieByIdResponse = movieEndpoint.getMovieById(getMovieByIdRequest);
        int movieId  = getMovieByIdResponse.getMovie().getId();
        String title = getMovieByIdResponse.getMovie().getTitle();
        int runtime = getMovieByIdResponse.getMovie().getRuntime();

        // When
        UpdateMovieAddCastResponse updateMovieAddCastResponse = movieEndpoint.updateMovieAddCast(updateMovieAddCastRequest);

        // Then
        assertThat(updateMovieAddCastResponse, is(notNullValue()));
        assertThat(updateMovieAddCastResponse.getMovie().getId(), is(movieId));
        assertThat(updateMovieAddCastResponse.getMovie().getTitle(), is(title));
        assertThat(updateMovieAddCastResponse.getMovie().getRuntime(), is(runtime));
        int castSize = updateMovieAddCastResponse.getMovie().getCast().getRole().size();
        assertThat(castSize, is(11));
        List<String> updatedCast = IntStream.range(0, castSize-1).mapToObj(x -> updateMovieAddCastResponse.getMovie().getCast().getRole().get(x).getCharacter()).collect(Collectors.toList());
        assertThat(updatedCast, hasItem("Captain kirk"));

        // When
        UpdateMovieRemoveCastResponse updateMovieRemoveResponse = movieEndpoint.updateMovieRemoveCast(updateMovieRemoveCastRequest);

        // Then
        assertThat(updateMovieRemoveResponse, is(notNullValue()));
        assertThat(updateMovieRemoveResponse.getMovie().getId(), is(movieId));
        assertThat(updateMovieRemoveResponse.getMovie().getTitle(), is(title));
        assertThat(updateMovieRemoveResponse.getMovie().getCast().getRole().size(), is(10));
        List<String> newUpdatedCast = IntStream.range(0, castSize-1)
                .mapToObj(x -> updateMovieRemoveResponse.getMovie().getCast().getRole().get(x).getCharacter())
                .collect(Collectors.toList());
        assertThat(newUpdatedCast, not(hasItem("Captain kirk")));

    }
    @Test
    public void shouldUpdateMovieAddAndRemoveGenreXmlRequest() throws IOException, XmlUtilsException {

        // Given
        UpdateMovieAddGenreRequest updateMovieAddGenreRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(updateMovieAddGenreRequestTestXml.getInputStream(), UpdateMovieAddGenreRequest.class);
        UpdateMovieRemoveGenreRequest updateMovieRemoveGenreRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(updateMovieRemoveGenreRequestTestXml.getInputStream(), UpdateMovieRemoveGenreRequest.class);
        GetMovieByIdRequest getMovieByIdRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(getMovieByIdForUpdatesRequestTestXml.getInputStream(), GetMovieByIdRequest.class);
        GetMovieByIdResponse getMovieByIdResponse = movieEndpoint.getMovieById(getMovieByIdRequest);
        int movieId  = getMovieByIdResponse.getMovie().getId();
        String title = getMovieByIdResponse.getMovie().getTitle();
        int runtime = getMovieByIdResponse.getMovie().getRuntime();
        int genreSize = getMovieByIdResponse.getMovie().getGenres().getGenre().size();

        // When
        UpdateMovieAddGenreResponse updateMovieAddGenreResponse = movieEndpoint.updateMovieAddGenre(updateMovieAddGenreRequest);

        // Then
        assertThat(updateMovieAddGenreResponse, is(notNullValue()));
        assertThat(updateMovieAddGenreResponse.getMovie().getId(), is(movieId));
        assertThat(updateMovieAddGenreResponse.getMovie().getTitle(), is(title));
        assertThat(updateMovieAddGenreResponse.getMovie().getRuntime(), is(runtime));
        assertThat(updateMovieAddGenreResponse.getMovie().getGenres().getGenre().size(), is(genreSize + 2));
        List<String> updatedGenres = IntStream.range(0,updateMovieAddGenreResponse.getMovie().getGenres().getGenre().size() - 1)
                .mapToObj(x -> updateMovieAddGenreResponse.getMovie().getGenres().getGenre().get(x).getName())
                .collect(Collectors.toList());
        assertThat(updatedGenres, hasItem("Chickflick"));
        assertThat(updatedGenres, hasItem("Comedy"));

        // When
        UpdateMovieRemoveGenreResponse updateMovieRemoveGenreResponse = movieEndpoint.updateMovieRemoveGenre(updateMovieRemoveGenreRequest);

        // Then
        assertThat(updateMovieRemoveGenreResponse, is(notNullValue()));
        assertThat(updateMovieRemoveGenreResponse.getMovie().getId(), is(movieId));
        assertThat(updateMovieRemoveGenreResponse.getMovie().getTitle(), is(title));
        assertThat(updateMovieRemoveGenreResponse.getMovie().getRuntime(), is(runtime));
        // 2 Genres removed, one does not exist. So Size = size - 1
        assertThat(updateMovieRemoveGenreResponse.getMovie().getGenres().getGenre().size(), is(genreSize + 1));
        List<String> newUpdatedGenres = IntStream.range(0,updateMovieRemoveGenreResponse.getMovie().getGenres().getGenre().size() - 1)
                .mapToObj(x -> updateMovieRemoveGenreResponse.getMovie().getGenres().getGenre().get(x).getName())
                .collect(Collectors.toList());
        assertThat(newUpdatedGenres, hasItem("Chickflick"));
        assertThat(newUpdatedGenres, not(hasItem("Comedy")));
    }
}
