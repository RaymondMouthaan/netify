package org.mouthaan.netify.soap.endpoint;

import org.mouthaan.namespace.netify.datatypes.genre.Genre;
import org.mouthaan.namespace.netify.datatypes.genre.GenreFilters;
import org.mouthaan.namespace.netify.general.*;
import org.mouthaan.netify.soap.app.NetifySoapApplication;
import org.mouthaan.netify.soap.client.GenreClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

/**
 * Genre Integration Test
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = NetifySoapApplication.class)
@ActiveProfiles("local")
//@Slf4j
public class GenreEndpointIT {

    @Autowired
    private GenreClient genreClient;

    @LocalServerPort
    private int port;

    @Before
    public void setUp() throws Exception {
        genreClient.setDefaultUri("http://localhost:" + port + "/movies/netify_3.0");
    }

    @Test
    public void shouldGetGenreCount() {
        // When
        GetGenreCountResponse getGenreCountResponse = genreClient.getGenreCount();

        // Then
        assertThat(getGenreCountResponse, is(notNullValue()));
        assertThat(getGenreCountResponse.getCount(), greaterThan(15L));
    }

    @Test
    public void shouldGetGenreById() {
        // Given
        int genreId = 28; // Action

        // When
        GetGenreByIdResponse genreByIdResponse = genreClient.getGenreById(genreId);

        // Then
        assertThat(genreByIdResponse, is(notNullValue()));
        assertThat(genreByIdResponse.getGenre().getId(), equalTo(genreId));
        assertThat(genreByIdResponse.getGenre().getName(), equalTo("Action"));
    }

    @Test
    public void shouldGetGenreAllFilter() {
        // Given
        GetGenreAllRequest getGenreAllRequest = new GetGenreAllRequest();
        getGenreAllRequest.setFilters(new GenreFilters());
        getGenreAllRequest.getFilters().setName("Action");

        // When
        GetGenreAllResponse getGenreAllResponse = genreClient.getGenreAll(getGenreAllRequest);

        // Then
        assertThat(getGenreAllResponse, is(notNullValue()));
        assertThat(getGenreAllResponse.getGenres().getGenre().get(0).getName(), equalTo("Action"));
    }

    @Test
    public void shouldGetGenreAllNoFilter() {
        // Given
        GetGenreAllRequest getGenreAllRequest = new GetGenreAllRequest();
        getGenreAllRequest.setFilters(new GenreFilters());

        // When
        GetGenreAllResponse getGenreAllResponse = genreClient.getGenreAll(getGenreAllRequest);

        // Then
        assertThat(getGenreAllResponse, is(notNullValue()));
        assertThat(getGenreAllResponse.getGenres().getGenre(), is(notNullValue()));
    }

    @Test
    public void shouldAddAndDeleteGenre() {

        // Add Genre Test
        // Given
        AddGenreRequest addGenreRequest = new AddGenreRequest();
        addGenreRequest.setGenre(new Genre());
        addGenreRequest.getGenre().setName("Test Horror");

        // When
        AddGenreResponse addGenreResponse = genreClient.addGenre(addGenreRequest);

        // Then
        assertThat(addGenreResponse, is(notNullValue()));
        assertThat(addGenreResponse.getGenre().getName(), equalTo("Test Horror"));

        // Delete Genre Test
        // Given
        DeleteGenreRequest deleteGenreRequest = new DeleteGenreRequest();
        deleteGenreRequest.setId(addGenreResponse.getGenre().getId());

        // When
        DeleteGenreResponse deleteGenreResponse = genreClient.deleteGenre(deleteGenreRequest);

        // Then
        assertThat(deleteGenreResponse, is(notNullValue()));
        assertThat(deleteGenreResponse.getGenre().getId(), equalTo(addGenreResponse.getGenre().getId()));
    }

    @Test
    public void shouldUpdateGenre() {
        // Given
        UpdateGenreRequest updateGenreRequest = new UpdateGenreRequest();
        updateGenreRequest.setGenre(new Genre());
        updateGenreRequest.getGenre().setId(53);
        updateGenreRequest.getGenre().setName("Thriller");

        // When
        UpdateGenreResponse updateGenreResponse = genreClient.updateGenre(updateGenreRequest);

        // Then
        assertThat(updateGenreResponse, is(notNullValue()));
        assertThat(updateGenreRequest.getGenre().getId(), equalTo(53));
        assertThat(updateGenreResponse.getGenre().getName(), equalTo("Thriller"));
    }
}