package com.handlers;

import java.util.Locale;

public class CountryCodeConverter {

    public static String convertCountryNameToCode(String countryName) {

        if (countryName == null || countryName.isEmpty()) {
            return null;
        }

        Locale[] locales = Locale.getAvailableLocales();

        for (Locale locale : locales) {
            String displayName = locale.getDisplayCountry();
            String countryCode = locale.getCountry();

            if (countryNameEquals(countryName, displayName)) {
                return countryCode;
            }
        }
        return null;
    }

    private static boolean countryNameEquals(String inputName, String displayName) {

        if (inputName == null) {
            return false;
        }

        String[] input = inputName.split(" ");

        StringBuilder normalizedInput = new StringBuilder();
        for (String word : input) {
            if (!word.isEmpty()) {
                if (!normalizedInput.isEmpty()) {
                    normalizedInput.append(" ");
                }
                normalizedInput.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase());
            }
        }
        return normalizedInput.toString().equalsIgnoreCase(displayName);
    }
}
