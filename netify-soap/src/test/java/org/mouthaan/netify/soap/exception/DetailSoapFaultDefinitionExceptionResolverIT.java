package org.mouthaan.netify.soap.exception;

import org.mouthaan.netify.soap.app.NetifySoapApplication;
import org.mouthaan.netify.soap.client.MovieClient;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ws.soap.client.SoapFaultClientException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Exception Integration Test
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = NetifySoapApplication.class)
@ActiveProfiles("local")

public class DetailSoapFaultDefinitionExceptionResolverIT {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private MovieClient movieClient;
    @LocalServerPort
    private int port;

    @Before
    public void setUp() throws Exception {
        movieClient.setDefaultUri("http://localhost:" + port + "/movies/netify_3.0");
    }

    @Test
    public void shouldGiveDetailedExceptionWhenGetMovieByNonexistingId() {
        // Check dat de NotFoundException wordt gegooid, fout tekst wordt gecontroleerd in systeemtest.
        int anId = 32; // Deze bestaat niet.
        boolean caught = false;

        try {
            movieClient.getMovieById(anId);
        } catch (SoapFaultClientException ex) {
            System.out.println(ex.getFaultStringOrReason());
            assertThat(ex.getFaultStringOrReason(), containsString("NotFoundException"));
            caught = true;
        }
        assertThat(caught, equalTo(true));
    }
}