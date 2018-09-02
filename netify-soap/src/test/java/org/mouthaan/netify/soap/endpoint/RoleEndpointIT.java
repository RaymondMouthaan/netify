package org.mouthaan.netify.soap.endpoint;

import org.mouthaan.namespace.netify.datatypes.actor.ActorFilters;
import org.mouthaan.namespace.netify.datatypes.role.RoleFilters;
import org.mouthaan.namespace.netify.general.GetActorAllRequest;
import org.mouthaan.namespace.netify.general.GetRoleAllRequest;
import org.mouthaan.namespace.netify.general.GetRoleAllResponse;
import org.mouthaan.namespace.netify.general.GetRoleByIdResponse;
import org.mouthaan.namespace.netify.general.GetRoleCountResponse;
import org.mouthaan.netify.soap.client.ActorClient;
import org.mouthaan.netify.soap.app.NetifySoapApplication;
import org.mouthaan.netify.soap.client.RoleClient;
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
 * Role Integration Test
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = NetifySoapApplication.class)
@ActiveProfiles("local")
//@Slf4j
public class RoleEndpointIT {
    @Autowired
    private RoleClient roleClient;

    @Autowired
    private ActorClient actorClient;

    @LocalServerPort
    private int port;

    @Before
    public void setUp() throws Exception {
        roleClient.setDefaultUri("http://localhost:" + port + "/movies/netify_3.0");
        actorClient.setDefaultUri("http://localhost:" + port + "/movies/netify_3.0");
    }

    @Test
    public void shouldGetRoleCharacterByActorId() {
        // Given
        // First determine the (dynamic) actorId.
        GetActorAllRequest getActorAllRequest = new GetActorAllRequest();
        getActorAllRequest.setFilters(new ActorFilters());
        getActorAllRequest.getFilters().setName("Kim Basinger");
        int actorId = actorClient.getActorAll(getActorAllRequest).getActors().getActor().get(0).getId();

        GetRoleAllRequest getRoleAllRequest = new GetRoleAllRequest();
        getRoleAllRequest.setFilters(new RoleFilters());
        getRoleAllRequest.getFilters().setActorId(actorId);

        // When
        GetRoleAllResponse getRoleAllResponse = roleClient.getRoleAll(getRoleAllRequest);

        // Then
        assertThat(getRoleAllResponse, is(notNullValue()));
        assertThat("The role character should match.", getRoleAllResponse.getCast().getRole().get(0).getCharacter(), equalTo("Lynn Bracken"));

    }

    @Test
    public void shouldGetRoleCount() {
        // When
        GetRoleCountResponse getRoleCountResponse = roleClient.getRoleCount();

        // Then
        assertThat(getRoleCountResponse, is(notNullValue()));
        assertThat(getRoleCountResponse.getCount(), greaterThan(15L));
    }

    @Test
    public void shouldGetRoleByRoleId() {
        // Given
        //First find a roleId another way (getRoleAll):
        GetRoleAllRequest getRoleAllRequest = new GetRoleAllRequest();
        getRoleAllRequest.setFilters(new RoleFilters());
        getRoleAllRequest.getFilters().setCharacter("Jason Bourne");
        int roleId = roleClient.getRoleAll(getRoleAllRequest).getCast().getRole().get(0).getId();

        // When
        GetRoleByIdResponse roleByIdResponse = roleClient.getRoleById(roleId);

        // Then
        assertThat(roleByIdResponse, is(notNullValue()));
        assertThat("The role ids should match.", roleByIdResponse.getCast().getRole().get(0).getId(), equalTo(roleId));

    }

    @Test
    public void shouldGetRoleAllFilter() {
        // Given
        GetRoleAllRequest getRoleAllRequest = new GetRoleAllRequest();
        getRoleAllRequest.setFilters(new RoleFilters());
        getRoleAllRequest.getFilters().setCharacter("Jason Bourne");

        // When
        GetRoleAllResponse getRoleAllResponse = roleClient.getRoleAll(getRoleAllRequest);

        // Then
        assertThat(getRoleAllResponse, is(notNullValue()));
        assertThat("The role character should match.", getRoleAllResponse.getCast().getRole().get(0).getCharacter(), equalTo("Jason Bourne"));
    }

    @Test
    public void shouldGetRoleAllNoFilter() {
        // Given
        GetRoleAllRequest getRoleAllRequest = new GetRoleAllRequest();
        getRoleAllRequest.setFilters(new RoleFilters());

        // When
        GetRoleAllResponse getRoleAllResponse = roleClient.getRoleAll(getRoleAllRequest);

        // Then
        assertThat(getRoleAllResponse, is(notNullValue()));
        assertThat(getRoleAllResponse.getCast().getRole().get(0).getId(), is(notNullValue()));
    }

}
