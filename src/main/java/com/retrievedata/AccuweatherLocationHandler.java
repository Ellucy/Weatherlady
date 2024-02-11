package com.retrievedata;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AccuweatherLocationHandler {

    public static AccuweatherLocationDetails getLocationDetails(String apiKey, String requestedCity) throws IOException {

        String endpoint = "http://dataservice.accuweather.com/locations/v1/cities/search";
        String params = String.format("apikey=%s&q=%s", apiKey, requestedCity);
        String urlString = String.format("%s?%s", endpoint, params);

        // Open connection
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try {
            // Read response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse JSON response
            JSONArray jsonArray = new JSONArray(response.toString());
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            // Extract relevant information
            String locationKey = jsonObject.getString("Key");
            String cityName = jsonObject.getString("EnglishName");
            JSONObject regionObject = jsonObject.getJSONObject("Region");
            String regionName = regionObject.getString("EnglishName");
            JSONObject countryObject = jsonObject.getJSONObject("Country");
            String countryName = countryObject.getString("EnglishName");
            JSONObject geoPositionObject = jsonObject.getJSONObject("GeoPosition");
            double latitude = geoPositionObject.getDouble("Latitude");
            double longitude = geoPositionObject.getDouble("Longitude");

            // Print extracted information
            System.out.println("City Name: " + cityName);
            System.out.println("Region Name: " + regionName);
            System.out.println("Country Name: " + countryName);
            System.out.println("Latitude: " + latitude);
            System.out.println("Longitude: " + longitude);

            // Return location key
            return new AccuweatherLocationDetails(locationKey, cityName, regionName, countryName, latitude, longitude);
        } finally {
            connection.disconnect();
        }
    }
}