package com.retrievedata;

import com.entities.WeatherAccuweather;
import com.entities.WeatherWeatherstack;
import com.handlers.APIConnection;
import com.handlers.DatabaseConnector;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Objects;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccuweatherDataRetrievalTest {

    @Mock
    private DatabaseConnector databaseConnector;

    @Mock
    private APIConnection apiConnection;

    @InjectMocks
    private AccuweatherDataRetrieval accuweatherDataRetrieval;

    @Mock
    private AccuweatherLocationHandler accuweatherLocationHandler;

    @Mock
    private AccuweatherLocationDetails accuweatherLocationDetails;

    @Before
    public void setUp() {
        accuweatherDataRetrieval = new AccuweatherDataRetrieval(databaseConnector, "awApikey");
        accuweatherDataRetrieval.setApiConnection(apiConnection);
        accuweatherDataRetrieval.setAccuweatherLocationHandler(accuweatherLocationHandler);
    }

    @Test
    public void testDownloadAndSetWeatherData() throws IOException {

        String jsonDataRetrieval = "{\"DailyForecasts\":[{\"Date\":\"2024-02-08T07:00:00-05:00\",\"Temperature\":{\"Minimum\":{\"Value\":40.0,\"Unit\":\"F\",\"UnitType\":18},\"Maximum\":{\"Value\":53.0,\"Unit\":\"F\",\"UnitType\":18}},\"Day\":{\"Wind\":{\"Speed\":{\"Value\":4.6,\"Unit\":\"mi/h\",\"UnitType\":9},\"Direction\":{\"Degrees\":101,\"Localized\":\"E\",\"English\":\"E\"}},\"RelativeHumidity\":{\"Minimum\":29,\"Maximum\":55,\"Average\":38}}}]}";

        JSONObject jsonResponse = new JSONObject(jsonDataRetrieval);

        when(accuweatherLocationHandler.getLocationDetails(anyString(), anyString())).thenReturn(accuweatherLocationDetails);
        when(accuweatherLocationDetails.getLocationKey()).thenReturn("locationkey");

        when(apiConnection.downloadWeatherData(anyString())).thenReturn(jsonResponse);

        accuweatherDataRetrieval.downloadAndSetWeatherData("New York", "Earthquake", "Earthquake RS 5");
        ArgumentCaptor<WeatherAccuweather> captor = ArgumentCaptor.forClass(WeatherAccuweather.class);
        verify(databaseConnector, times(1)).saveWeatherData(captor.capture());
        WeatherAccuweather savedWeather = captor.getValue();
        assertEquals("Thu Feb 08 00:00:00 EET 2024", savedWeather.getDate().toString());
        assertEquals("8.06", savedWeather.getTemperature().toString());
        assertEquals("38", savedWeather.getHumidity().toString());
        assertEquals("Earthquake", savedWeather.getNaturalDisaster());
        assertEquals("Earthquake RS 5", savedWeather.getDescription());
        assertEquals("E (101°)", savedWeather.getWindDirection());
        assertEquals(4.6, savedWeather.getWindSpeed());

    }

    @Test(expected = NullPointerException.class)
    public void testDownloadAndSetWeatherData_NullCityName() throws IOException {
        AccuweatherDataRetrieval accuweatherDataRetrieval = new AccuweatherDataRetrieval(databaseConnector, "dummyOwApiKey");
        accuweatherDataRetrieval.downloadAndSetWeatherData(null, "Earthquake", "Earthquake RS 5");
    }
}