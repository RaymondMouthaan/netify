package org.mouthaan.netify.soap.endpoint;

import org.mouthaan.namespace.netify.datatypes.actor.Actor;
import org.mouthaan.namespace.netify.datatypes.actor.Gender;
import org.mouthaan.namespace.netify.datatypes.genre.Genre;
import org.mouthaan.namespace.netify.datatypes.genre.Genres;
import org.mouthaan.namespace.netify.datatypes.movie.Movie;
import org.mouthaan.namespace.netify.datatypes.movie.MovieFilters;
import org.mouthaan.namespace.netify.datatypes.role.Cast;
import org.mouthaan.namespace.netify.datatypes.role.Role;
import org.mouthaan.namespace.netify.general.*;
import org.mouthaan.netify.soap.app.NetifySoapApplication;
import org.mouthaan.netify.soap.client.MovieClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.GregorianCalendar;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

/**
 * Movie Integration Test
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = NetifySoapApplication.class)
@ActiveProfiles("local")
//@Slf4j
public class MovieEndpointIT {

    @Autowired
    private MovieClient movieClient;

    @LocalServerPort
    private int port;

    @Before
    public void setUp() throws Exception {
        movieClient.setDefaultUri("http://localhost:" + port + "/movies/netify_3.0");
    }

    @Test
    public void shouldGetMovieCount() {
        // When
        GetMovieCountResponse getMovieCountResponse = movieClient.getMovieCount();

        // Then
        assertThat(getMovieCountResponse, is(notNullValue()));
        assertThat(getMovieCountResponse.getCount(), greaterThan(200L));
    }

    @Test
    public void shouldGetMovieById() {
        // Given
        int anId = 33; // Unforgiven

        // When
        GetMovieByIdResponse getMovieByIdResponse = movieClient.getMovieById(anId);

        // Then
        assertThat(getMovieByIdResponse, is(notNullValue()));
        assertThat(getMovieByIdResponse.getMovie().getId(), is(anId));
        assertThat(getMovieByIdResponse.getMovie().getTitle(), is("Unforgiven"));
    }

    @Test
    public void shouldGetMovieAllFilter() {
        // Given
        GetMovieAllRequest getMovieAllRequest = new GetMovieAllRequest();
        getMovieAllRequest.setFilters(new MovieFilters());
        getMovieAllRequest.getFilters().setTitle("Unforgiven");

        // When
        GetMovieAllResponse movieAllResponse = movieClient.getMovieAll(getMovieAllRequest);

        // Then
        assertThat(movieAllResponse, is(notNullValue()));
        assertThat(movieAllResponse.getMovies().getMovie().get(0).getId(), is(notNullValue()));
        assertThat(movieAllResponse.getMovies().getMovie().get(0).getTitle(), is("Unforgiven"));
        assertThat(movieAllResponse.getMovies().getMovie().get(0).getRuntime(), is(131));
        assertThat(movieAllResponse.getMovies().getMovie().get(0).getGenres(), is(notNullValue()));
        assertThat(movieAllResponse.getMovies().getMovie().get(0).getCast(), is(notNullValue()));
    }

    @Test
    public void shouldGetMovieAllNoFilter() {
        // Given
        GetMovieAllRequest getMovieAllRequest = new GetMovieAllRequest();
        getMovieAllRequest.setFilters(new MovieFilters());

        // When
        GetMovieAllResponse movieAllResponse = movieClient.getMovieAll(getMovieAllRequest);

        // Then
        assertThat(movieAllResponse, is(notNullValue()));
        assertThat(movieAllResponse.getMovies().getMovie().get(0).getId(), is(notNullValue()));
        assertThat(movieAllResponse.getMovies().getMovie().size(), greaterThan(240));
    }

    @Test
    public void shouldAddAndDeleteMovie() throws DatatypeConfigurationException {

        // Add Movie Test
        // Given
        AddMovieRequest addMovieRequest = new AddMovieRequest();
        addMovieRequest.setMovie(new Movie());

        addMovieRequest.getMovie().setImdbId("t74578");
        addMovieRequest.getMovie().setTitle("Star Wars I");
        addMovieRequest.getMovie().setOriginalTitle("Star Wars the beginning");
        XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar(1958, 4, 5, 0, 0, 0));
        addMovieRequest.getMovie().setReleaseDate(date);
        addMovieRequest.getMovie().setRuntime(189);
        addMovieRequest.getMovie().setOriginalLanguage("en");
        addMovieRequest.getMovie().setPopularity(3.5468);
        addMovieRequest.getMovie().setHomepage("http://google.com");
        addMovieRequest.getMovie().setBudget(1500000);
        addMovieRequest.getMovie().setRevenue(1);
        addMovieRequest.getMovie().setStatus("Released");
        addMovieRequest.getMovie().setTagline("A true story");
        addMovieRequest.getMovie().setOverview("Luck blabla");

        // Add genre
        addMovieRequest.getMovie().setGenres(new Genres());
        Genre genre = new Genre();
        genre.setName("Drama");
        addMovieRequest.getMovie().getGenres().getGenre().add(genre);

        // Add Cast
        addMovieRequest.getMovie().setCast(new Cast());
        Role role = new Role();
        role.setCharacter("Yolo");
        role.setOrder(1);
        // Add Actor to role
        Actor actor = new Actor();
        actor.setName("Chris Brown");
        actor.setGender(Gender.MALE);
        role.setActor(actor);
        addMovieRequest.getMovie().getCast().getRole().add(role);

        // When
        AddMovieResponse addMovieResponse = movieClient.addMovie(addMovieRequest);

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
        DeleteMovieResponse deleteMovieResponse = movieClient.deleteMovie(deleteMovieRequest);

        // Then
        assertThat(deleteMovieResponse, is(notNullValue()));
        assertThat(deleteMovieResponse.getMovie().getId(), is(addMovieResponse.getMovie().getId()));
    }


    @Test
    public void shouldUpdateMovie() {
        // Given
        UpdateMovieRequest updateMovieRequest = new UpdateMovieRequest();
        updateMovieRequest.setMovie(new Movie());
        updateMovieRequest.getMovie().setId(11);
        updateMovieRequest.getMovie().setTitle("Star Wars");
        updateMovieRequest.getMovie().setRuntime(180);

        // When
        UpdateMovieResponse updateMovieResponse = movieClient.updateMovie(updateMovieRequest);

        // Then
        assertThat("updateMovieResponse should not be null", updateMovieResponse, is(notNullValue()));
        assertThat(updateMovieRequest.getMovie().getId(), is(11));
        assertThat(updateMovieResponse.getMovie().getTitle(), is("Star Wars"));
        assertThat("Runtime should be 180", updateMovieResponse.getMovie().getRuntime(), is(180));
    }

    /**
     * Update a Movie with adding one genre
     */
    @Test
    public void shouldUpdateMovieAddOneGenre() {
        // Given
        Integer movieId = 2503;
        String genreName = "Horror";

        UpdateMovieAddGenreRequest updateMovieAddGenreRequest = new UpdateMovieAddGenreRequest();
        updateMovieAddGenreRequest.setMovieId(movieId);
        Genre genre = new Genre();
        genre.setName(genreName);
        updateMovieAddGenreRequest.getGenre().add(genre);

        // When
        UpdateMovieAddGenreResponse updateMovieAddGenreResponse = movieClient.updateMovieAddGenre(updateMovieAddGenreRequest);

        // Then
        assertThat(updateMovieAddGenreResponse, is(notNullValue()));
        final boolean[] found = {false};
        updateMovieAddGenreResponse.getMovie().getGenres().getGenre().forEach(g -> {
            if (g.getName().equals(genreName)) {
                found[0] = true;
            }
        });
        assertThat("The new added genre not found in movie: " + genreName, found[0], is(true));
    }

    /**
     * Update a Movie with adding two genres
     */
    @Test
    public void shouldUpdateMovieAddTwoGenre() {
        // Given
        Integer movieId = 218;
        String genreName1 = "Horror";
        String genreName2 = "Adventure";

        UpdateMovieAddGenreRequest updateMovieAddGenreRequest = new UpdateMovieAddGenreRequest();
        updateMovieAddGenreRequest.setMovieId(movieId);
        Genre newGenre1 = new Genre();
        newGenre1.setName(genreName1);
        Genre newGenre2 = new Genre();
        newGenre2.setName(genreName2);
        updateMovieAddGenreRequest.getGenre().add(newGenre1);
        updateMovieAddGenreRequest.getGenre().add(newGenre2);

        // When
        UpdateMovieAddGenreResponse updateMovieAddGenreResponse = movieClient.updateMovieAddGenre(updateMovieAddGenreRequest);

        // Then
        assertThat(updateMovieAddGenreResponse, is(notNullValue()));
        final boolean[] found = {false, false};
        updateMovieAddGenreResponse.getMovie().getGenres().getGenre().forEach(g -> {
            if (g.getName().equals(genreName1)) {
                found[0] = true;
            }
            if (g.getName().equals(genreName2)) {
                found[1] = true;
            }
        });
        assertThat("The new added genre not found in movie: " + genreName1, found[0], is(true));
        assertThat("The new added genre not found in movie: " + genreName2, found[1], is(true));
    }


    /**
     * Update a Movie to remove one genre
     */
    @Test
    public void shouldUpdateMovieRemoveOneGenre() {
        // Given
        int movieId = 155;
        String genreName = "Crime";

        Genre removeGenre = new Genre();
        removeGenre.setName(genreName);

        UpdateMovieRemoveGenreRequest updateMovieRemoveGenreRequest = new UpdateMovieRemoveGenreRequest();
        updateMovieRemoveGenreRequest.setMovieId(movieId);
        updateMovieRemoveGenreRequest.getGenre().add(removeGenre);

        // When
        UpdateMovieRemoveGenreResponse updateMovieRemoveGenreResponse = movieClient.updateMovieRemoveGenre(updateMovieRemoveGenreRequest);

        // Then
        final boolean[] notFound = {true};
        updateMovieRemoveGenreResponse.getMovie().getGenres().getGenre().forEach(g -> {
            if (g.getName().equals(genreName)) {
                notFound[0] = false;
            }
        });
        assertThat("The removed genre found in movie: " + removeGenre, notFound[0]);
    }

    /**
     * Update a Movie to remove two genres
     */
    @Test
    public void shouldUpdateMovieRemoveTwoGenre() {
        // Given
        int movieId = 422;
        String genreName1 = "Fantasy";
        String genreName2 = "Drama";

        Genre removeGenre1 = new Genre();
        removeGenre1.setName(genreName1);
        Genre removeGenre2 = new Genre();
        removeGenre2.setName(genreName2);

        UpdateMovieRemoveGenreRequest updateMovieRemoveGenreRequest = new UpdateMovieRemoveGenreRequest();
        updateMovieRemoveGenreRequest.setMovieId(movieId);
        updateMovieRemoveGenreRequest.getGenre().add(removeGenre1);
        updateMovieRemoveGenreRequest.getGenre().add(removeGenre2);

        // When
        UpdateMovieRemoveGenreResponse updateMovieRemoveGenreResponse = movieClient.updateMovieRemoveGenre(updateMovieRemoveGenreRequest);

        // Then
        final boolean[] notFound = {true, true};
        updateMovieRemoveGenreResponse.getMovie().getGenres().getGenre().forEach(g -> {
            if (g.getName().equals(genreName1)) {
                notFound[0] = false;
            }
            if (g.getName().equals(genreName2)) {
                notFound[1] = false;
            }
        });
        assertThat("The removed genre 1 found in movie: " + removeGenre1, notFound[0]);
        assertThat("The removed genre 2 found in movie: " + removeGenre2, notFound[1]);
    }

    /**
     * Update a Movie with add a cast with one role
     */
    @Test
    public void shouldUpdateMovieAddCastWithOneRole() {
        // Given
        int movieId = 630;

        Role newRole1 = new Role();
        newRole1.setCharacter("Yoloki");
        newRole1.setOrder(10);
        Actor newActor1 = new Actor();
        newActor1.setName("Eric Baldwin");
        newActor1.setGender(Gender.MALE);
        newRole1.setActor(newActor1);

        UpdateMovieAddCastRequest updateMovieAddCastRequest = new UpdateMovieAddCastRequest();
        updateMovieAddCastRequest.setMovieId(movieId);
        updateMovieAddCastRequest.setCast(new Cast());
        updateMovieAddCastRequest.getCast().getRole().add(newRole1);

        // When
        UpdateMovieAddCastResponse updateMovieAddCastResponse = movieClient.updateMovieAddCast(updateMovieAddCastRequest);


        // Then
        final boolean[] found = {false};
        updateMovieAddCastResponse.getMovie().getCast().getRole().forEach(r -> {
            if (r.getCharacter().equals(newRole1.getCharacter())) {
                if (r.getOrder().equals(newRole1.getOrder())) {
                    if (!(r.getActor() == null)) {
                        found[0] = true;
                    }
                }
            }
        });
        assertThat("The new added role 1 with actor 1 not found in movie: " + newRole1.getCharacter(), found[0]);
        assertThat("The movie cast should have a size of 11.", updateMovieAddCastResponse.getMovie().getCast().getRole().size(), is(11));
    }

    /**
     * Update a Movie with add a cast with two roles
     */
    @Test
    public void shouldUpdateMovieAddCastWithTwoRole() {
        // Given
        int movieId = 3175;

        Role newRole1 = new Role();
        newRole1.setCharacter("Yoloki");
        newRole1.setOrder(10);
        Actor newActor1 = new Actor();
        newActor1.setName("Eric Baldwin");
        newActor1.setGender(Gender.MALE);
        newRole1.setActor(newActor1);

        Role newRole2 = new Role();
        newRole2.setCharacter("Mamayaki");
        newRole2.setOrder(11);
        Actor newActor2 = new Actor();
        newActor2.setName("Samantha Baldwin");
        newActor2.setGender(Gender.FEMALE);
        newRole2.setActor(newActor2);

        UpdateMovieAddCastRequest updateMovieAddCastRequest = new UpdateMovieAddCastRequest();
        updateMovieAddCastRequest.setMovieId(movieId);
        updateMovieAddCastRequest.setCast(new Cast());
        updateMovieAddCastRequest.getCast().getRole().add(newRole1);
        updateMovieAddCastRequest.getCast().getRole().add(newRole2);

        // When
        UpdateMovieAddCastResponse updateMovieAddCastResponse = movieClient.updateMovieAddCast(updateMovieAddCastRequest);

        // Then
        final boolean[] found = {false, false};
        updateMovieAddCastResponse.getMovie().getCast().getRole().forEach(r -> {
            if (r.getCharacter().equals(newRole1.getCharacter())) {
                if (r.getOrder().equals(newRole1.getOrder())) {
                    if (!(r.getActor() == null)) {
                        found[0] = true;
                    }
                }
            }
            if (r.getCharacter().equals(newRole2.getCharacter())) {
                if (r.getOrder().equals(newRole2.getOrder())) {
                    if (!(r.getActor() == null)) {
                        found[1] = true;
                    }
                }
            }
        });
        assertThat("The movie cast should have a size of 12.",updateMovieAddCastResponse.getMovie().getCast().getRole().size(), is(12));
        assertThat("The new added role 1 with actor 1 not found in movie: " + newRole1.getCharacter(), found[0]);
        assertThat("The new added role 2 with actor 2 not found in movie: " + newRole2.getCharacter(), found[1]);
    }

    /**
     * Update a Movie to remove one role with character from cast
     */
    @Test
    public void shouldUpdateMovieRemoveOneRoleWithCharacterFromCast() {
        // Given
        int movieId = 137;
        String removeCharacter = "Phil Connors";

        Role removeRole = new Role();
        removeRole.setCharacter(removeCharacter);

        UpdateMovieRemoveCastRequest updateMovieRemoveCastRequest = new UpdateMovieRemoveCastRequest();
        updateMovieRemoveCastRequest.setMovieId(movieId);
        updateMovieRemoveCastRequest.setCast(new Cast());
        updateMovieRemoveCastRequest.getCast().getRole().add(removeRole);

        // When
        UpdateMovieRemoveCastResponse updateMovieRemoveCastResponse = movieClient.updateMovieRemoveCast(updateMovieRemoveCastRequest);

        // Then
        final boolean[] notFound = {true};
        updateMovieRemoveCastResponse.getMovie().getCast().getRole().forEach(r -> {
            if (r.getCharacter().equals(removeRole.getCharacter())) {
                notFound[0] = false;
            }
        });
        assertThat("The movie cast does not match size 9.", updateMovieRemoveCastResponse.getMovie().getCast().getRole().size(), is(9));
        assertThat("The removed role 1 with character found in movie: " + removeRole.getCharacter(), notFound[0]);
    }

    /**
     * Update a Movie to remove two roles with character from cast
     */
    @Test
    public void shouldUpdateMovieRemoveTwoRolesWithCharacterFromCast() {
        // Given
        int movieId = 406;
        String removeCharacter1 = "Benoît";
        String removeCharacter2 = "Darty";

        Role removeRole1 = new Role();
        removeRole1.setCharacter(removeCharacter1);

        Role removeRole2 = new Role();
        removeRole2.setCharacter(removeCharacter2);

        UpdateMovieRemoveCastRequest updateMovieRemoveCastRequest = new UpdateMovieRemoveCastRequest();
        updateMovieRemoveCastRequest.setMovieId(movieId);
        updateMovieRemoveCastRequest.setCast(new Cast());
        updateMovieRemoveCastRequest.getCast().getRole().add(removeRole1);
        updateMovieRemoveCastRequest.getCast().getRole().add(removeRole2);

        // When
        UpdateMovieRemoveCastResponse updateMovieRemoveCastResponse = movieClient.updateMovieRemoveCast(updateMovieRemoveCastRequest);

        // Then
        final boolean[] notFound = {true, true};
        updateMovieRemoveCastResponse.getMovie().getCast().getRole().forEach(r -> {
            if (r.getCharacter().equals(removeRole1.getCharacter())) {
                notFound[0] = false;
            }
            if (r.getCharacter().equals(removeRole2.getCharacter())) {
                notFound[1] = false;
            }
        });
        assertThat("The movie cast does not match size 7.", updateMovieRemoveCastResponse.getMovie().getCast().getRole().size(), is(7));
        assertThat("The removed role 1 with character found in movie: " + removeRole1.getCharacter(), notFound[0]);
        assertThat("The removed role 2 with character found in movie: " + removeRole2.getCharacter(), notFound[1]);
    }

    /**
     * Update a Movie to remove one role with id from cast
     */
    @Test
    public void shouldUpdateMovieRemoveOneRoleWithIdFromCast() {
        // Given
        int movieId = 63;
        int roleId = movieClient.getMovieById(movieId).getMovie().getCast().getRole().get(0).getId();
        String characterToCheck = movieClient.getMovieById(movieId).getMovie().getCast().getRole().get(0).getCharacter();

        Role removeRole = new Role();
        removeRole.setId(roleId);

        UpdateMovieRemoveCastRequest updateMovieRemoveCastRequest = new UpdateMovieRemoveCastRequest();

        updateMovieRemoveCastRequest.setMovieId(movieId);
        updateMovieRemoveCastRequest.setCast(new Cast());
        updateMovieRemoveCastRequest.getCast().getRole().add(removeRole);

        // When
        UpdateMovieRemoveCastResponse updateMovieRemoveCastResponse = movieClient.updateMovieRemoveCast(updateMovieRemoveCastRequest);

        // Then
        final boolean[] notFound = {true, true};
        updateMovieRemoveCastResponse.getMovie().getCast().getRole().forEach(r -> {
            if (r.getId().equals(removeRole.getId())) {
                notFound[0] = false;
            }
            if (r.getCharacter().equals(characterToCheck)) {
                notFound[1] = false;
            }
        });
        assertThat("The movie cast does not match size 9.", updateMovieRemoveCastResponse.getMovie().getCast().getRole().size(), is(9));
        assertThat("The removed role 1 with roleId: " + roleId, notFound[0]);
        assertThat("The removed role 1 with character: " + characterToCheck, notFound[1]);
    }

    /**
     * Update a Movie to remove two roles with id from cast
     */
    @Test
    public void shouldUpdateMovieRemoveTwoRoleWithIdFromCast() {
        // Given
        int movieId = 578;

        UpdateMovieRemoveCastRequest updateMovieRemoveCastRequest = new UpdateMovieRemoveCastRequest();
        updateMovieRemoveCastRequest.setMovieId(movieId);
        updateMovieRemoveCastRequest.setCast(new Cast());

        int roleId1 = movieClient.getMovieById(movieId).getMovie().getCast().getRole().get(1).getId();
        String characterToCheck1 = movieClient.getMovieById(movieId).getMovie().getCast().getRole().get(1).getCharacter();
        Role removeRole1 = new Role();
        removeRole1.setId(roleId1);
        int roleId2 = movieClient.getMovieById(movieId).getMovie().getCast().getRole().get(2).getId();
        String characterToCheck2 = movieClient.getMovieById(movieId).getMovie().getCast().getRole().get(2).getCharacter();
        Role removeRole2 = new Role();
        removeRole2.setId(roleId2);

        updateMovieRemoveCastRequest.getCast().getRole().add(removeRole1);
        updateMovieRemoveCastRequest.getCast().getRole().add(removeRole2);

        // When
        UpdateMovieRemoveCastResponse updateMovieRemoveCastResponse = movieClient.updateMovieRemoveCast(updateMovieRemoveCastRequest);

        // Then
        final boolean[] notFound = {true, true, true, true};
        updateMovieRemoveCastResponse.getMovie().getCast().getRole().forEach(r -> {
            if (r.getId().equals(roleId1)) {
                notFound[0] = false;
            }
            if (r.getCharacter().equals(characterToCheck1)) {
                notFound[1] = false;
            }
            if (r.getId().equals(roleId2)) {
                notFound[2] = false;
            }
            if (r.getCharacter().equals(characterToCheck2)) {
                notFound[3] = false;
            }
        });
        assertThat("The removed role 1 with id : " + roleId1, notFound[0]);
        assertThat("The removed role 1 with character: " + characterToCheck1, notFound[1]);
        assertThat("The removed role 2 with id : " + roleId2, notFound[2]);
        assertThat("The removed role 2 with character: " + characterToCheck2, notFound[3]);
        assertThat("The movie cast does not match size 8.", updateMovieRemoveCastResponse.getMovie().getCast().getRole().size(), is(8));
    }
}
