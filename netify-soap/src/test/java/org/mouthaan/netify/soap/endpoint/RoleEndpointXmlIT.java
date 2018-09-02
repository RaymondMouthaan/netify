package org.mouthaan.netify.soap.endpoint;

import org.mouthaan.namespace.netify.general.GetRoleAllRequest;
import org.mouthaan.namespace.netify.general.GetRoleAllResponse;
import org.mouthaan.namespace.netify.general.GetRoleCountResponse;
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
 * Role Xml Integration Test
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = NetifySoapApplication.class)
@ActiveProfiles(profiles = {"local"})
//@Slf4j
public class RoleEndpointXmlIT {

    @Autowired
    private RoleEndpoint roleEndpoint;

    @Value(value = "classpath:soap/requests/role/GetRoleCountRequestTest.xml")
    private Resource getRoleCountRequestTestXml;

    @Value(value = "classpath:soap/requests/role/GetRoleByIdRequestTest.xml")
    private Resource getRoleByIdRequestTestXml;

    @Value(value = "classpath:soap/requests/role/GetRoleAllRequestFilterCharacterTest.xml")
    private Resource getRoleAllRequestFilterCharacterTestXml;

    @Value(value = "classpath:soap/requests/role/GetRoleAllRequestFilterActorIdTest.xml")
    private Resource getRoleAllRequestFilterActorIdTestXml;

    @Value(value = "classpath:soap/requests/role/GetRoleAllRequestNoFilterTest.xml")
    private Resource getRoleAllRequestNoFilterTestXml;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void shouldGetRoleCountXmlRequest() throws IOException, XmlUtilsException {
        // Given
        // XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(getRoleCountRequestTestXml.getInputStream(), Object.class);

        // When
        GetRoleCountResponse getRoleCountResponse = roleEndpoint.getRoleCount();

        // Then
        assertThat(getRoleCountResponse, is(notNullValue()));
        assertThat(getRoleCountResponse.getCount(), greaterThan(2400L));
    }

    // Todo: Running all together this test fails
//    @Test
//    public void shouldGetRoleByIdXmlRequest() throws IOException, XmlUtilsException {
//        // Given
//        GetRoleByIdRequest getRoleByIdRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(getRoleByIdRequestTestXml.getInputStream(), GetRoleByIdRequest.class);
//
//        // When
//        GetRoleByIdResponse getRoleByIdResponse = roleEndpoint.getRoleById(getRoleByIdRequest);
//
//        // Then
//        assertThat(getRoleByIdResponse, is(notNullValue()));
//        assertThat(getRoleByIdResponse.getCast().getRole(), is(notNullValue()));
//        assertThat( getRoleByIdResponse.getCast().getRole().get(0).getActor().getName(), equalTo(actorToCheck));
//
//    }

    @Test
    public void shouldGetRoleAllFilterCharacterXmlRequest() throws IOException, XmlUtilsException {
        // Given
        GetRoleAllRequest getRoleAllRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(getRoleAllRequestFilterCharacterTestXml.getInputStream(), GetRoleAllRequest.class);

        // When
        GetRoleAllResponse getRoleAllResponse = roleEndpoint.getRoleAll(getRoleAllRequest);

        // Then
        assertThat(getRoleAllResponse, is(notNullValue()));
        assertThat(getRoleAllResponse.getCast().getRole().get(0).getActor().getName(), equalTo("Matt Damon"));
    }

    @Test
    public void shouldGetRoleAllFilterActorIdXmlRequest() throws IOException, XmlUtilsException {
        // Given
        GetRoleAllRequest getRoleAllRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(getRoleAllRequestFilterActorIdTestXml.getInputStream(), GetRoleAllRequest.class);

        // When
        GetRoleAllResponse getRoleAllResponse = roleEndpoint.getRoleAll(getRoleAllRequest);

        // Then
        assertThat(getRoleAllResponse, is(notNullValue()));
        assertThat(getRoleAllResponse.getCast().getRole().get(0).getActor().getName(), equalTo("Matt Damon"));
    }
    @Test
    public void shouldGetRoleAllNoFilterXmlRequest() throws IOException, XmlUtilsException {
        // Given
        GetRoleAllRequest getRoleAllRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(getRoleAllRequestNoFilterTestXml.getInputStream(), GetRoleAllRequest.class);

        // When
        GetRoleAllResponse getRoleAllResponse = roleEndpoint.getRoleAll(getRoleAllRequest);

        // Then
        assertThat(getRoleAllResponse, is(notNullValue()));
        assertThat(getRoleAllResponse.getCast().getRole().size(), greaterThan(2400));
    }


}
