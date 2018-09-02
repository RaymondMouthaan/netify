package org.mouthaan.netify.soap.endpoint;

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
 * Genre Xml Integration Test
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = NetifySoapApplication.class)
@ActiveProfiles(profiles = {"local", "integration-test"})
//@Slf4j
public class GenreEndpointXmlIT {

    @Autowired
    private GenreEndpoint genreEndpoint;

    @Value(value = "classpath:soap/requests/genre/GetGenreCountRequestTest.xml")
    private Resource getGenreCountRequestTestXml;

    @Value(value = "classpath:soap/requests/genre/GetGenreByIdRequestTest.xml")
    private Resource getGenreByIdRequestTestXml;

    @Value(value = "classpath:soap/requests/genre/GetGenreAllRequestFilterTest.xml")
    private Resource getGenreAllRequestFilterTestXml;

    @Value(value = "classpath:soap/requests/genre/GetGenreAllRequestNoFilterTest.xml")
    private Resource getGenreAllRequestNoFilterTestXml;

    @Value(value = "classpath:soap/requests/genre/AddGenreRequestTest.xml")
    private Resource addGenreRequestTestXml;

    @Value(value = "classpath:soap/requests/genre/UpdateGenreRequestTest.xml")
    private Resource updateGenreRequestTestXml;

    @Value(value = "classpath:soap/requests/genre/DeleteGenreRequestTest.xml")
    private Resource deleteGenreRequestTestXml;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void shouldGetGenreCountXmlRequest() throws IOException, XmlUtilsException {
        // Given
        //XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(getGenreCountRequestTestXml.getInputStream(), Object.class);

        // When
        GetGenreCountResponse getGenreCountResponse = genreEndpoint.getGenreCount();

        // Then
        assertThat(getGenreCountResponse, is(notNullValue()));
        assertThat(getGenreCountResponse.getCount(), greaterThan(15L));
    }

    @Test
    public void shouldGetGenreByIdXmlRequest() throws IOException, XmlUtilsException {
        // Given
        GetGenreByIdRequest getGenreByIdRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(getGenreByIdRequestTestXml.getInputStream(), GetGenreByIdRequest.class);

        // When
        GetGenreByIdResponse getGenreByIdResponse = genreEndpoint.getGenreById(getGenreByIdRequest);

        // Then
        assertThat(getGenreByIdResponse, is(notNullValue()));
        assertThat(getGenreByIdResponse.getGenre().getId(), equalTo(53));
        assertThat(getGenreByIdResponse.getGenre().getName(), equalTo("Thriller"));
    }

    @Test
    public void shouldGetGenreAllFilterXmlRequest() throws IOException, XmlUtilsException {
        // Given
        GetGenreAllRequest getGenreAllRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(getGenreAllRequestFilterTestXml.getInputStream(), GetGenreAllRequest.class);

        // When
        GetGenreAllResponse getGenreAllResponse = genreEndpoint.getGenreAll(getGenreAllRequest);

        // Then
        assertThat(getGenreAllResponse, is(notNullValue()));
        assertThat(getGenreAllResponse.getGenres().getGenre().get(0).getName(), equalTo("Horror"));
    }

    @Test
    public void shouldGetGenreAllNoFilterXmlRequest() throws IOException, XmlUtilsException {
        // Given
        GetGenreAllRequest getGenreAllRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(getGenreAllRequestNoFilterTestXml.getInputStream(), GetGenreAllRequest.class);

        // When
        GetGenreAllResponse getGenreAllResponse = genreEndpoint.getGenreAll(getGenreAllRequest);

        // Then
        assertThat(getGenreAllResponse, is(notNullValue()));
        assertThat(getGenreAllResponse.getGenres().getGenre().get(0).getId(), is(notNullValue()));
        assertThat(getGenreAllResponse.getGenres().getGenre().get(0).getName(), equalTo("Adventure"));

        assertThat(getGenreAllResponse, is(notNullValue()));
        assertThat(getGenreAllResponse.getGenres().getGenre().get(0).getId(), is(notNullValue()));
        assertThat(getGenreAllResponse.getGenres().getGenre().get(1).getName(), equalTo("Fantasy"));

        assertThat(getGenreAllResponse, is(notNullValue()));
        assertThat(getGenreAllResponse.getGenres().getGenre().get(0).getId(), is(notNullValue()));
        assertThat(getGenreAllResponse.getGenres().getGenre().get(2).getName(), equalTo("Animation"));
    }

    @Test
    public void shouldAddAndDeleteGenreXmlRequest() throws IOException, XmlUtilsException {
        // Add Genre Test
        // Given
        AddGenreRequest addGenreRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(addGenreRequestTestXml.getInputStream(), AddGenreRequest.class);

        // When
        AddGenreResponse addGenreResponse = genreEndpoint.addGenre(addGenreRequest);

        // Then
        assertThat(addGenreResponse, is(notNullValue()));
        assertThat(addGenreResponse.getGenre().getId(), is(notNullValue()));
        assertThat(addGenreResponse.getGenre().getName(), equalTo("Adventure II"));

        // Delete Genre Test
        // Given
        DeleteGenreRequest deleteGenreRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(deleteGenreRequestTestXml.getInputStream(), DeleteGenreRequest.class);
        // Override id from xml by id from add
        deleteGenreRequest.setId(addGenreResponse.getGenre().getId());

        // When
        DeleteGenreResponse deleteGenreResponse = genreEndpoint.deleteGenre(deleteGenreRequest);

        // Then
        assertThat(deleteGenreResponse, is(notNullValue()));
        assertThat(deleteGenreResponse.getGenre().getId(), equalTo(addGenreResponse.getGenre().getId()));
    }

    @Test
    public void shouldUpdateGenreXmlRequest() throws IOException, XmlUtilsException {
        // Given
        UpdateGenreRequest updateGenreRequest = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(updateGenreRequestTestXml.getInputStream(), UpdateGenreRequest.class);

        // When
        UpdateGenreResponse updateGenreResponse = genreEndpoint.updateGenre(updateGenreRequest);

        // Then
        assertThat(updateGenreResponse, is(notNullValue()));
        assertThat(updateGenreResponse.getGenre().getId(), equalTo(updateGenreRequest.getGenre().getId()));
        assertThat(updateGenreResponse.getGenre().getName(), equalTo("Comedy"));
    }
}
