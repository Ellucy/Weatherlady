package com.handlers;

import com.retrievedata.AccuweatherDataRetrieval;
import com.retrievedata.OpenweatherDataRetrieval;
import com.retrievedata.WeatherstackDataRetrieval;
import org.junit.jupiter.api.Test;
import com.handlers.WeatherApplicationController;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WeatherApplicationControllerTest {

    @Test
    public void testAddingNaturalDisaster() {
        AccuweatherDataRetrieval accuweatherDataRetrieval = mock(AccuweatherDataRetrieval.class);
        OpenweatherDataRetrieval openweatherDataRetrieval = mock(OpenweatherDataRetrieval.class);
        WeatherstackDataRetrieval weatherstackDataRetrieval = mock(WeatherstackDataRetrieval.class);

    }

    public void testGetDisastersByDate() {
    }

    public void testConvertStringToTimestamp() {
    }

    public void testGetDisastersByName() {
    }

    public void testGetDisastersByCityName() {
    }

    public void testGetDisastersByCountryName() {
    }
}