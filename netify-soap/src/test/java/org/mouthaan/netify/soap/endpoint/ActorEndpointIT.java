package org.mouthaan.netify.soap.endpoint;

import org.mouthaan.namespace.netify.datatypes.actor.Actor;
import org.mouthaan.namespace.netify.datatypes.actor.ActorFilters;
import org.mouthaan.namespace.netify.datatypes.actor.Gender;
import org.mouthaan.namespace.netify.general.*;
import org.mouthaan.netify.soap.app.NetifySoapApplication;
import org.mouthaan.netify.soap.client.ActorClient;
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
 * Actor Integration Test
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = NetifySoapApplication.class)
@ActiveProfiles("local")
//@Slf4j
public class ActorEndpointIT {

    @LocalServerPort
    private int port;

    @Autowired
    private ActorClient actorClient;

    @Before
    public void setUp() throws Exception {
        actorClient.setDefaultUri("http://localhost:" + port + "/movies/netify_3.0");
    }

    @Test
    public void shouldGetActorCount() throws Exception {
        // When
        GetActorCountResponse getActorCountResponse = actorClient.getActorCount();

        // Then
        assertThat(getActorCountResponse, is(notNullValue()));
        assertThat(getActorCountResponse.getCount(), greaterThan(1980L));
    }

    @Test
    public void shouldGetActorById() {
        // Given
        int actorId = 33; // Gary Sinise

        // When
        GetActorByIdResponse getActorByIdResponse = actorClient.getActorById(actorId);

        // Then
        assertThat(getActorByIdResponse, is(notNullValue()));
        assertThat(getActorByIdResponse.getActor().getId(), equalTo(actorId));
        assertThat(getActorByIdResponse.getActor().getName(), equalTo("Gary Sinise"));
        assertThat(getActorByIdResponse.getActor().getGender().value(), equalTo("MALE"));
    }

    @Test
    public void shouldGetActorAllFilter() {
        // Given
        GetActorAllRequest getActorAllRequest = new GetActorAllRequest();
        getActorAllRequest.setFilters(new ActorFilters());
        getActorAllRequest.getFilters().setName("Matt Damon");

        // When
        GetActorAllResponse actorAllResponse = actorClient.getActorAll(getActorAllRequest);

        // Then
        assertThat(actorAllResponse, is(notNullValue()));
        assertThat(actorAllResponse.getActors().getActor().get(0).getId(), is(notNullValue()));
        assertThat(actorAllResponse.getActors().getActor().get(0).getName(), equalTo("Matt Damon"));
        assertThat(actorAllResponse.getActors().getActor().get(0).getGender().value(), equalTo("MALE"));
    }

    @Test
    public void shouldGetActorAllNoFilter() {
        // Given
        GetActorAllRequest getActorAllRequest = new GetActorAllRequest();
        getActorAllRequest.setFilters(new ActorFilters());

        // When
        GetActorAllResponse actorAllResponse = actorClient.getActorAll(getActorAllRequest);

        // Then
        assertThat(actorAllResponse, is(notNullValue()));
        assertThat(actorAllResponse.getActors().getActor(), is(notNullValue()));
    }

    @Test
    public void shouldAddAndDeleteActor() {

        // Add Actor Test
        // Given
        AddActorRequest addActorRequest = new AddActorRequest();
        addActorRequest.setActor(new Actor());
        addActorRequest.getActor().setName("Chantal Janzen");
        addActorRequest.getActor().setGender(Gender.FEMALE);

        // When
        AddActorResponse addActorResponse = actorClient.addActor(addActorRequest);

        // Then
        assertThat(addActorResponse, is(notNullValue()));
        assertThat(addActorResponse.getActor().getName(), equalTo("Chantal Janzen"));
        assertThat(addActorResponse.getActor().getGender().value(), equalTo("FEMALE"));

        // Delete Actor Test
        // Given
        DeleteActorRequest deleteActorRequest = new DeleteActorRequest();
        deleteActorRequest.setId(addActorResponse.getActor().getId());

        // When
        DeleteActorResponse deleteActorResponse = actorClient.deleteActor(deleteActorRequest);

        // Then
        assertThat(deleteActorResponse, is(notNullValue()));
        assertThat(deleteActorResponse.getActor().getId(), equalTo(addActorResponse.getActor().getId()));
    }

    @Test
    public void shouldUpdateActor() {
        // Given
        UpdateActorRequest updateActorRequest = new UpdateActorRequest();
        updateActorRequest.setActor(new Actor());
        updateActorRequest.getActor().setId(16483);
        updateActorRequest.getActor().setName("Sylvester Stallone");
        updateActorRequest.getActor().setGender(Gender.MALE);

        // When
        UpdateActorResponse updateActorResponse = actorClient.updateActor(updateActorRequest);

        // Then
        assertThat(updateActorResponse, is(notNullValue()));
        assertThat(updateActorRequest.getActor().getId(), equalTo(16483));
        assertThat(updateActorResponse.getActor().getName(), equalTo("Sylvester Stallone"));
        assertThat(updateActorResponse.getActor().getGender(), equalTo(Gender.MALE));
    }
}