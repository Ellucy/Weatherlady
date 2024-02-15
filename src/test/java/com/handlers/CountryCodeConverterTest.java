package com.handlers;

import com.handlers.CountryCodeConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CountryCodeConverterTest {

    @Test
    public void testConvertCountryNameToCode_ShouldReturnCountryCode() {
        CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
        String countryName = "United States";
        String countryCode = countryCodeConverter.convertCountryNameToCode(countryName);
        assertEquals("US", countryCode);
    }

    @Test
    public void testConvertCountryNameToCode_CaseInsensitive_ShouldReturnCountryCode() {
        CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
        String countryName = "united states";
        String countryCode = countryCodeConverter.convertCountryNameToCode(countryName);
        assertEquals("US", countryCode);
    }

    @Test
    public void testConvertCountryNameToCode_InvalidCountryName_ShouldReturnNull() {
        CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
        String countryName = "Non-existing country";
        String countryCode = countryCodeConverter.convertCountryNameToCode(countryName);
        assertNull(countryCode);
    }

    @Test
    public void testConvertCountryNameToCode_CountryNameIsNull_ShouldReturnNull() {
        CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
        String countryName = null;
        String countryCode = countryCodeConverter.convertCountryNameToCode(countryName);
        assertNull(countryCode);
    }

    @Test
    public void testConvertCountryNameToCode_CountryNameIsEmpty_ShouldReturnNull() {
        CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
        String countryName = "";
        String countryCode = countryCodeConverter.convertCountryNameToCode(countryName);
        assertNull(countryCode);
    }
}