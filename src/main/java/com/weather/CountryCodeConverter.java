package com.weather;

import java.util.Locale;

public class CountryCodeConverter {

    public static String convertCountryCodeToName(String countryCode) {
        Locale locale = new Locale("", countryCode);
        return locale.getDisplayCountry();
    }
}
