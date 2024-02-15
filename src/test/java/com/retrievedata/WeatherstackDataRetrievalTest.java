package com.retrievedata;

import com.entities.WeatherWeatherstack;
import com.handlers.APIConnection;
import com.handlers.DatabaseConnector;
import com.retrievedata.WeatherstackDataRetrieval;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(MockitoJUnitRunner.class)
public class WeatherstackDataRetrievalTest {

    @Mock
    private APIConnection apiConnection;

    @Mock
    private DatabaseConnector databaseConnector;

    @InjectMocks
    private WeatherstackDataRetrieval weatherstackDataRetrieval;

    @Test
    public void testDownloadAndSetWeatherData() throws IOException {
        // Mock JSON response
        String jsonResponse = "{\"location\":{\"country\":\"US\",\"region\":\"NY\",\"name\":\"New York\",\"lat\":40.712,\"lon\":-74.006,\"localtime_epoch\":1645003200},\"current\":{\"temperature\":10,\"pressure\":1012,\"humidity\":80,\"wind_speed\":10,\"wind_degree\":180,\"wind_dir\":\"S\"}}";

        // Mock static method call for APIConnection
        mockStatic(APIConnection.class);
        when(APIConnection.downloadWeatherData(anyString())).thenReturn(new JSONObject(jsonResponse));

        // Mock static method call for DatabaseConnector
        mockStatic(DatabaseConnector.class);

        // Call the method under test
        WeatherstackDataRetrieval.downloadAndSetWeatherData("New York", "Hurricane", "Category 5 hurricane", "api_key");

        // Verify that the data is saved to the database
        ArgumentCaptor<WeatherWeatherstack> captor = ArgumentCaptor.forClass(WeatherWeatherstack.class);
        verifyStatic(DatabaseConnector.class, times(1));
        DatabaseConnector.saveWeatherData(captor.capture());

        // Assert on the saved WeatherWeatherstack object
        WeatherWeatherstack savedWeather = captor.getValue();
        assertEquals("US", savedWeather.getCountryName());
        assertEquals("NY", savedWeather.getRegionName());
        assertEquals("New York", savedWeather.getCityName());
        assertEquals(40.712, savedWeather.getLatitude(), 0.001);
        assertEquals(-74.006, savedWeather.getLongitude(), 0.001);
    }
}