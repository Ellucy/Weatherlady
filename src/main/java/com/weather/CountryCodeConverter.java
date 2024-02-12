package com.weather;

import java.util.Locale;

public class CountryCodeConverter {

    public static String convertCountryCodeToName(String countryCode) {
        Locale locale = new Locale("", countryCode);
        return locale.getDisplayCountry();
    }

    public static String convertCountryNameToCode(String countryName) {

        Locale[] locales = Locale.getAvailableLocales();

        for (Locale locale : locales) {
            String displayName = locale.getDisplayCountry();
            String countryCode = locale.getCountry();

            if (countryNameEquals(countryName, displayName)) {
                System.out.println("Here:" + countryCode);
                return countryCode;
            }
        }
        return null;
    }

    private static boolean countryNameEquals(String inputName, String displayName) {

        String[] input = inputName.split(" ");

        StringBuilder normalizedInput = new StringBuilder();
        for (String word : input) {
            if (!word.isEmpty()) {
                if (normalizedInput.length() > 0) {
                    normalizedInput.append(" ");
                }
                normalizedInput.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase());
            }
        }
        return normalizedInput.toString().equalsIgnoreCase(displayName);
    }
}
