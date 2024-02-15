package com.retrievedata;

import com.handlers.APIConnection;

import com.handlers.DatabaseConnector;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class OpenweatherDataRetrievalTest {

    @Mock
    private APIConnection apiConnection;

    @Mock
    private DatabaseConnector databaseConnector;

    @Mock
    private OpenweatherDataRetrieval openweatherDataRetrieval;
    @BeforeEach


    @Test
    public void testDownloadAndSetWeatherData() throws IOException {

        String jsonResponse = "{\"sys\":{\"country\":\"US\"},\"coord\":{\"lat\":40.7128,\"lon\":-74.0060},\"dt\":1644884400,\"main\":{\"temp\":10.0,\"pressure\":1013,\"humidity\":50},\"wind\":{\"deg\":180,\"speed\":5.0}}";

        when(apiConnection.downloadWeatherData(anyString())).thenReturn(new JSONObject(jsonResponse));

        doNothing().when(databaseConnector).saveWeatherData(any());


        openweatherDataRetrieval.downloadAndSetWeatherData("New york", "Earthquake", "Earthquake RS 7");
        verify(databaseConnector, times(1)).saveWeatherData(any());

    }

    @Test(expected = NullPointerException.class)
    public void testDownloadAndSetWeatherData_NullCityName() throws IOException {

        openweatherDataRetrieval.downloadAndSetWeatherData(null, "Hurricane", "Category 5 hurricane");
    }
}
