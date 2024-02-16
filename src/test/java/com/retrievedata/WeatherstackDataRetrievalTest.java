package com.retrievedata;

import com.entities.WeatherWeatherstack;
import com.handlers.APIConnection;
import com.handlers.DatabaseConnector;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class WeatherstackDataRetrievalTest {

    @Mock
    private APIConnection apiConnection;

    @Mock
    private DatabaseConnector databaseConnector;

    @Mock
    private WeatherstackDataRetrieval dataRetrieval;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        dataRetrieval = new WeatherstackDataRetrieval(databaseConnector, "wsapikey");
        dataRetrieval.setApiConnection(apiConnection);

    }

    @Test
    public void testDownloadAndSetWeatherData() throws IOException {

        JSONObject jsonData = new JSONObject();
        jsonData.put("sys", new JSONObject().put("country", "United States of America"));
        jsonData.put("coord", new JSONObject().put("lat", 40.7128).put("lon", -74.0060));
        jsonData.put("main", new JSONObject().put("temp", 20.5).put("pressure", 1013).put("humidity", 50));
        jsonData.put("wind", new JSONObject().put("deg", 180).put("speed", 5.5));

        when(apiConnection.downloadWeatherData(anyString())).thenReturn(new JSONObject(jsonData));

        // Call the method under test
        dataRetrieval.downloadAndSetWeatherData("New York", "Hurricane", "Category 5 hurricane");

        WeatherWeatherstack weatherWeatherstack = new WeatherWeatherstack();
        weatherWeatherstack.setCityName("New York");
        weatherWeatherstack.setCountryName("United States of America");
        weatherWeatherstack.setLatitude(40.7128);
        weatherWeatherstack.setLongitude(-74.0060);
        weatherWeatherstack.setDescription("Category 5 hurricane");

        System.out.println(weatherWeatherstack.getCityName());
        System.out.println(weatherWeatherstack.getCountryName());
        System.out.println(weatherWeatherstack.getLatitude());
        System.out.println(weatherWeatherstack.getLongitude());
        System.out.println(weatherWeatherstack.getDescription());

        verify(databaseConnector).saveWeatherData(argThat(weatherData ->
                Objects.equals("New York", weatherData.getCityName()) &&
                        Objects.equals("United States of America", weatherData.getCountryName()) &&
                        Objects.equals(40.7128, weatherData.getLatitude()) &&
                        Objects.equals(-74.006, weatherData.getLongitude()) &&
                        Objects.equals("Category 5 hurricane", weatherData.getDescription())
        ));
    }

    @Test(expected = IllegalArgumentException.class)

    public void testDownloadAndSetWeatherData_NullCityName() throws IOException {
        WeatherstackDataRetrieval weatherstackDataRetrieval = new WeatherstackDataRetrieval(databaseConnector, "api_key");
        weatherstackDataRetrieval.downloadAndSetWeatherData(null, "Hurricane", "Category 5 hurricane");
    }
}