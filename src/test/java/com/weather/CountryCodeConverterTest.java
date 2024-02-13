package com.weather;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CountryCodeConverterTest{

    @Test
    public void testConvertCountryNameToCode_ShouldReturnCountryCode() {

        String countryName = "United States";
        String countryCode = CountryCodeConverter.convertCountryNameToCode(countryName);
        assertEquals("US", countryCode);
    }

    @Test
    public void testConvertCountryNameToCode_CaseInsensitive_ShouldReturnCountryCode() {

        String countryName = "united states";
        String countryCode = CountryCodeConverter.convertCountryNameToCode(countryName);
        assertEquals("US", countryCode);
    }
}