package org.mouthaan.netify.soap.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.mouthaan.namespace.netify.general.*;
import org.mouthaan.netify.common.util.XmlUtils;
import org.mouthaan.netify.common.util.XmlUtilsException;
import org.mouthaan.netify.soap.app.NetifySoapApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

/**
 * Actor Xml Integration Test
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = NetifySoapApplication.class)
@ActiveProfiles(profiles = {"local"})
@Slf4j
public class ActorEndpointXmlIT {
    @Autowired
    private ActorEndpoint actorEndpoint;

    @Value(value = "classpath:soap/requests/actor/GetActorCountRequestTest.xml")
    private Resource getActorCountRequestTestXml;

    @Value(value = "classpath:soap/requests/actor/GetActorByIdRequestTest.xml")
    private Resource getActorByIdRequestTestXml;

    @Value(value = "classpath:soap/requests/actor/GetActorAllRequestFilterTest.xml")
    private Resource getActorAllRequestFilterTestXml;

    @Value(value = "classpath:soap/requests/actor/GetActorAllRequestNoFilterTest.xml")
    private Resource getActorAllRequestNoFilterTestXml;

    @Value(value = "classpath:soap/requests/actor/AddActorRequestTest.xml")
    private Resource addActorRequestTestXml;

    @Value(value = "classpath:soap/requests/actor/UpdateActorRequestTest.xml")
    private Resource updateActorRequestTestXml;

    @Value(value = "classpath:soap/requests/actor/DeleteActorRequestTest.xml")
    private Resource deleteActorRequestTestXml;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void shouldGetActorCountXmlRequest() throws IOException, XmlUtilsException {
        // Given
        //XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(getActorCountRequestTestXml.getInputStream(), Object.class);

        // When
        GetActorCountResponse getActorCountResponse = actorEndpoint.getActorCount();

        // Then
        assertThat(getActorCountResponse, is(notNullValue()));
        assertThat(getActorCountResponse.getCount(), greaterThan(1980L));
    }

    @Test
    public void shouldGetActorByIdXmlRequest() throws IOException, XmlUtilsException {
        // Given
        GetActorByIdRequest getActorByIdRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(getActorByIdRequestTestXml.getInputStream(), GetActorByIdRequest.class);

        // When
        GetActorByIdResponse getActorByIdResponse = actorEndpoint.getActorById(getActorByIdRequest);

        // Then
        assertThat(getActorByIdResponse, is(notNullValue()));
        assertThat(getActorByIdResponse.getActor().getId(), equalTo(33));
        assertThat(getActorByIdResponse.getActor().getName(), equalTo("Gary Sinise"));
        assertThat(getActorByIdResponse.getActor().getGender().value(), equalTo("MALE"));
    }

    @Test
    public void shouldGetActorAllFilterXmlRequest() throws IOException, XmlUtilsException {
        // Given
        GetActorAllRequest getActorAllRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(getActorAllRequestFilterTestXml.getInputStream(), GetActorAllRequest.class);

        // When
        GetActorAllResponse getActorAllResponse = actorEndpoint.getActorAll(getActorAllRequest);

        // Then
        assertThat(getActorAllResponse, is(notNullValue()));
        assertThat(getActorAllResponse.getActors().getActor().get(0).getName(), equalTo("Matt Damon"));
        assertThat(getActorAllResponse.getActors().getActor().get(0).getGender().value(), equalTo("MALE"));
    }

    @Test
    public void shouldGetActorAllNoFilterXmlRequest() throws IOException, XmlUtilsException {
        // Given
        GetActorAllRequest getActorAllRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(getActorAllRequestNoFilterTestXml.getInputStream(), GetActorAllRequest.class);

        // When
        GetActorAllResponse getActorAllResponse = actorEndpoint.getActorAll(getActorAllRequest);

        // Then
        assertThat(getActorAllResponse, is(notNullValue()));
        assertThat(getActorAllResponse.getActors().getActor().get(0).getId(), is(notNullValue()));
        assertThat(getActorAllResponse.getActors().getActor().get(0).getName(), equalTo("Mark Hamill"));
        assertThat(getActorAllResponse.getActors().getActor().get(0).getGender().value(), equalTo("MALE"));

        assertThat(getActorAllResponse, is(notNullValue()));
        assertThat(getActorAllResponse.getActors().getActor().get(0).getId(), is(notNullValue()));
        assertThat(getActorAllResponse.getActors().getActor().get(1).getName(), equalTo("Harrison Ford"));
        assertThat(getActorAllResponse.getActors().getActor().get(1).getGender().value(), equalTo("MALE"));

        assertThat(getActorAllResponse, is(notNullValue()));
        assertThat(getActorAllResponse.getActors().getActor().get(0).getId(), is(notNullValue()));
        assertThat(getActorAllResponse.getActors().getActor().get(2).getName(), equalTo("Carrie Fisher"));
        assertThat(getActorAllResponse.getActors().getActor().get(2).getGender().value(), equalTo("FEMALE"));
    }

    @Test
    public void shouldAddAndDeleteActorXmlRequest() throws IOException, XmlUtilsException {
        // Add Actor Test
        // Given
        AddActorRequest addActorRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(addActorRequestTestXml.getInputStream(), AddActorRequest.class);

        // When
        AddActorResponse addActorResponse = actorEndpoint.addActor(addActorRequest);

        // Then
        assertThat(addActorResponse, is(notNullValue()));
        assertThat(addActorResponse.getActor().getId(), is(notNullValue()));
        assertThat(addActorResponse.getActor().getName(), equalTo("Chantal Janzen"));
        assertThat(addActorResponse.getActor().getGender().value(), equalTo("FEMALE"));

        // Delete Actor Test
        // Given
        DeleteActorRequest deleteActorRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(deleteActorRequestTestXml.getInputStream(), DeleteActorRequest.class);
        // Override id from xml by id from add
        deleteActorRequest.setId(addActorResponse.getActor().getId());

        // When
        DeleteActorResponse deleteActorResponse = actorEndpoint.deleteActor(deleteActorRequest);

        // Then
        assertThat(deleteActorResponse, is(notNullValue()));
        assertThat(deleteActorResponse.getActor().getId(), equalTo(addActorResponse.getActor().getId()));
    }

    @Test
    public void shouldUpdateActorXmlRequest() throws IOException, XmlUtilsException {
        // Given
        UpdateActorRequest updateActorRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(updateActorRequestTestXml.getInputStream(), UpdateActorRequest.class);

        // When
        UpdateActorResponse updateActorResponse = actorEndpoint.updateActor(updateActorRequest);

        // Then
        assertThat(updateActorResponse, is(notNullValue()));
        assertThat(updateActorRequest.getActor().getId().equals(updateActorResponse.getActor().getId()), is(notNullValue()));
        assertThat(updateActorResponse.getActor().getName(), equalTo("Sylvester Stallone"));
        assertThat(updateActorResponse.getActor().getGender().value(), equalTo("MALE"));
    }
}
