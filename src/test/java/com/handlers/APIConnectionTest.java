package com.handlers;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.handlers.APIConnection;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
public class APIConnectionTest {

    private APIConnection apiConnection;

    private static final int PORT = 8080;
    private WireMockServer wireMockServer;

    @BeforeEach
    public void setUp() {
        apiConnection = new APIConnection();
        wireMockServer = new WireMockServer(PORT);
        wireMockServer.start();
        WireMock.configureFor("localhost", PORT);
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testDownloadWeatherDataSuccess() {
        stubFor(get(urlEqualTo("/weather"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"temperature\": 25.0, \"humidity\": 60}")
                ));

        String url = "http://localhost:" + PORT + "/weather";
        try {
            JSONObject data = apiConnection.downloadWeatherData(url);
            assertNotNull(data);
            assertEquals(25.0, data.getDouble("temperature"), 0.01);
            assertEquals(60, data.getInt("humidity"));
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testDownloadWeatherDataFailure() {
        stubFor(get(urlEqualTo("/weather"))
                .willReturn(aResponse()
                        .withStatus(500)
                ));

        String url = "http://localhost:" + PORT + "/weather";
        try {
            JSONObject data = apiConnection.downloadWeatherData(url);
            assertNull(data); // Expecting null response for failure
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
}