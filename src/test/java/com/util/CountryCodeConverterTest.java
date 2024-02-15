package com.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CountryCodeConverterTest {

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

    @Test
    public void testConvertCountryNameToCode_InvalidCountryName_ShouldReturnNull() {

        String countryName = "Non-existing country";
        String countryCode = CountryCodeConverter.convertCountryNameToCode(countryName);
        assertNull(countryCode);
    }

    @Test
    public void testConvertCountryNameToCode_CountryNameIsNull_ShouldReturnNull() {

        String countryName = null;
        String countryCode = CountryCodeConverter.convertCountryNameToCode(countryName);
        assertNull(countryCode);
    }

    @Test
    public void testConvertCountryNameToCode_CountryNameIsEmpty_ShouldReturnNull() {

        String countryName = "";
        String countryCode = CountryCodeConverter.convertCountryNameToCode(countryName);
        assertNull(countryCode);
    }
}