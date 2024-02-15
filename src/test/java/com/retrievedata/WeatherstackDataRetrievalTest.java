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

//        when(apiConnection.downloadWeatherData(anyString())).thenReturn(new JSONObject(jsonResponse));

        // Call the method under test
        weatherstackDataRetrieval.downloadAndSetWeatherData("New York", "Hurricane", "Category 5 hurricane");

        // Verify that the data is saved to the database
        ArgumentCaptor<WeatherWeatherstack> captor = ArgumentCaptor.forClass(WeatherWeatherstack.class);
        verify(databaseConnector, times(1));
        databaseConnector.saveWeatherData(captor.capture());

        // Assert on the saved WeatherWeatherstack object
        WeatherWeatherstack savedWeather = captor.getValue();
        assertEquals("United States of America", savedWeather.getCountryName());
        assertEquals("New York", savedWeather.getRegionName());
        assertEquals("New York", savedWeather.getCityName());
        assertEquals(40.714, savedWeather.getLatitude(), 0.001);
        assertEquals(-74.006, savedWeather.getLongitude(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)

    public void testDownloadAndSetWeatherData_NullCityName() throws IOException {

        weatherstackDataRetrieval.downloadAndSetWeatherData(null, "Hurricane", "Category 5 hurricane");
    }
}