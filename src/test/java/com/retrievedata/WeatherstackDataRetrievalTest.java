package com.retrievedata;

import com.entities.WeatherWeatherstack;
import com.handlers.APIConnection;
import com.handlers.DatabaseConnector;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Objects;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class WeatherstackDataRetrievalTest {

    @Mock
    private APIConnection apiConnection;

    @Mock
    private DatabaseConnector databaseConnector;

    @InjectMocks
    private WeatherstackDataRetrieval dataRetrieval;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testDownloadAndSetWeatherData() throws IOException {

        // Mock JSON response
        JSONObject jsonData = new JSONObject();
        JSONObject location = new JSONObject();
        location.put("country", "United States of America");
        location.put("region", "New York");
        location.put("name", "New York");
        location.put("lat", 40.7128);
        location.put("lon", -74.006);
        location.put("localtime_epoch", 1645003200);
        jsonData.put("location", location);

        JSONObject current = new JSONObject();
        current.put("temperature", 10);
        current.put("pressure", 1012);
        current.put("humidity", 80);
        current.put("wind_speed", 10);
        current.put("wind_degree", 180);
        current.put("wind_dir", "S");
        jsonData.put("current", current);

        when(apiConnection.downloadWeatherData(anyString())).thenReturn(jsonData);

        // Call the method under test
        dataRetrieval.downloadAndSetWeatherData("New York", "Hurricane", "Category 5 hurricane");

        // Verify that the data is saved to the database
        ArgumentCaptor<WeatherWeatherstack> captor = ArgumentCaptor.forClass(WeatherWeatherstack.class);
        verify(databaseConnector, times(1)).saveWeatherData(captor.capture());

        // Assert on the saved WeatherWeatherstack object
        WeatherWeatherstack savedWeather = captor.getValue();
        assertEquals("United States of America", savedWeather.getCountryName());
        assertEquals("New York", savedWeather.getRegionName());
        assertEquals("New York", savedWeather.getCityName());
        assertEquals(40.7128, savedWeather.getLatitude(), 0.001);
        assertEquals(-74.006, savedWeather.getLongitude(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)

    public void testDownloadAndSetWeatherData_NullCityName() throws IOException {
        WeatherstackDataRetrieval weatherstackDataRetrieval = new WeatherstackDataRetrieval(databaseConnector, "api_key");
        weatherstackDataRetrieval.downloadAndSetWeatherData(null, "Hurricane", "Category 5 hurricane");
    }
}