package com.retrievedata;

import com.entities.WeatherAccuweather;
import com.handlers.APIConnection;
import com.handlers.DatabaseConnector;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccuweatherDataRetrievalTest {

    @Mock
    private DatabaseConnector databaseConnector;

    @Mock
    private APIConnection apiConnection;

    @Mock
    private AccuweatherDataRetrieval accuweatherDataRetrieval;

    @Before
    public void setUp() {

        accuweatherDataRetrieval = new AccuweatherDataRetrieval(databaseConnector, "awApikey");
        accuweatherDataRetrieval.setApiConnection(apiConnection);

    }

    @Test
    public void testDownloadAndSetWeatherData() throws IOException {

        JSONObject jsonResponse = new JSONObject();
        JSONObject forecast = new JSONObject();
        JSONObject temperature = new JSONObject();
        temperature.put("Minimum", new JSONObject().put("Value", 20.0));
        temperature.put("Maximum", new JSONObject().put("Value", 30.0));

        forecast.put("Temperature", temperature);
        JSONObject day = new JSONObject();
        day.put("RelativeHumidity", new JSONObject().put("Average", 50));
        JSONObject wind = new JSONObject();
        wind.put("Speed", new JSONObject().put("Value", 10.0));
        wind.put("Direction", new JSONObject().put("Degrees", 180).put("Localized", "South"));
        day.put("Wind", wind);
        forecast.put("Day", day);
        jsonResponse.put("DailyForecasts", new JSONObject[]{forecast});

        when(apiConnection.downloadWeatherData(anyString())).thenReturn(jsonResponse);

        accuweatherDataRetrieval.downloadAndSetWeatherData("New York", "Earthquake", "Earthquake RS 5");

        WeatherAccuweather weatherAccuweather = new WeatherAccuweather();
        weatherAccuweather.setNaturalDisaster("Earthquake");
        weatherAccuweather.setDescription("Earthquake RS 5");
        weatherAccuweather.setTemperature(30.5);
        weatherAccuweather.setHumidity(50);
        weatherAccuweather.setWindDirection("South" + " (" + 180 + "°)");
        weatherAccuweather.setWindSpeed(10.0);

        verify(databaseConnector).saveWeatherData(argThat(dataEntity ->
                Objects.equals(dataEntity.getNaturalDisaster(), weatherAccuweather.getNaturalDisaster()) &&
                        Objects.equals(dataEntity.getDescription(), weatherAccuweather.getDescription()) &&
                        Objects.equals(dataEntity.getTemperature(), weatherAccuweather.getTemperature()) &&
                        Objects.equals(dataEntity.getHumidity(), weatherAccuweather.getHumidity()) &&
                        Objects.equals(dataEntity.getWindDirection(), weatherAccuweather.getWindDirection()) &&
                        Objects.equals(dataEntity.getWindSpeed(), weatherAccuweather.getWindSpeed())
        ));
    }

    @Test(expected = NullPointerException.class)
    public void testDownloadAndSetWeatherData_NullCityName() throws IOException {
        AccuweatherDataRetrieval accuweatherDataRetrieval = new AccuweatherDataRetrieval(databaseConnector, "dummyOwApiKey");
        accuweatherDataRetrieval.downloadAndSetWeatherData(null, "Earthquake", "Earthquake RS 5");
    }
}