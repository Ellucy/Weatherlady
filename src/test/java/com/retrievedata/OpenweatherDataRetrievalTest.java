package com.retrievedata;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.entities.WeatherOpenweather;
import com.entities.WeatherWeatherstack;
import com.handlers.APIConnection;
import com.handlers.DatabaseConnector;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Objects;
@RunWith(MockitoJUnitRunner.class)
public class OpenweatherDataRetrievalTest {

    @Mock
    private DatabaseConnector databaseConnector;

    @Mock
    private APIConnection apiConnection;

    @Mock
    private OpenweatherDataRetrieval dataRetrieval;

    @Before
    public void setUp() {

        dataRetrieval = new OpenweatherDataRetrieval(databaseConnector, "owapikey");
        dataRetrieval.setApiConnection(apiConnection);

    }

     @Test
    public void testDownloadAndSetWeatherData() throws IOException {

        JSONObject jsonData = new JSONObject();
        jsonData.put("sys", new JSONObject().put("country", "US"));
        jsonData.put("coord", new JSONObject().put("lat", 40.7128).put("lon", -74.0060));
        jsonData.put("dt", 1613507761);
        jsonData.put("main", new JSONObject().put("temp", 20.5).put("pressure", 1013).put("humidity", 50));
        jsonData.put("wind", new JSONObject().put("deg", 180).put("speed", 5.5));

        when(apiConnection.downloadWeatherData(anyString())).thenReturn(jsonData);

        dataRetrieval.downloadAndSetWeatherData("New York", "Earthquake", "Earthquake RS 5");

         ArgumentCaptor<WeatherOpenweather> captor = ArgumentCaptor.forClass(WeatherOpenweather.class);
         verify(databaseConnector, times(1)).saveWeatherData(captor.capture());

         // Assert on the saved WeatherWeatherstack object
         WeatherOpenweather savedWeather = captor.getValue();
         assertEquals("US", savedWeather.getCountryName());
         assertEquals("New York", savedWeather.getCityName());
         assertEquals(40.7128, savedWeather.getLatitude());
         assertEquals(-74.006, savedWeather.getLongitude());
         assertEquals("Earthquake", savedWeather.getNaturalDisaster());
         assertEquals("Earthquake RS 5",savedWeather.getDescription());

    }
    @Test(expected = NullPointerException.class)
    public void testDownloadAndSetWeatherData_NullCityName() {
        OpenweatherDataRetrieval openweatherDataRetrieval = new OpenweatherDataRetrieval(databaseConnector, "dummyOwApiKey");
        openweatherDataRetrieval.downloadAndSetWeatherData(null, "Hurricane", "Category 5 hurricane");
    }
}

