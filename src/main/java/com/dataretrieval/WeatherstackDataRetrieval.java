package com.dataretrieval;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WeatherstackDataRetrieval {

    public static void main(String[] args) {

        String apiKey = System.getenv("WS_API_KEY");

        String weatherstackResponse = "http://api.weatherstack.com/current?access_key=" + apiKey + "&query=tallinn";

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet getRequest = new HttpGet(weatherstackResponse);

        try {
            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                JSONObject jsonResponse = new JSONObject(result.toString());
                System.out.println(jsonResponse);

                // Extracting current weather data
                JSONObject currentWeather = jsonResponse.getJSONObject("current");

                // Extract temperature
                int temperature = currentWeather.getInt("temperature");

                // Extract pressure
                int pressure = currentWeather.getInt("pressure");

                // Extract humidity
                int humidity = currentWeather.getInt("humidity");

                // Extract wind data
                int windSpeed = currentWeather.getInt("wind_speed");
                int windDegree = currentWeather.getInt("wind_degree");
                String windDirection = currentWeather.getString("wind_dir");

                // Print extracted data
                System.out.println("Average Temperature: " + temperature);
                System.out.println("Pressure: " + pressure);
                System.out.println("Humidity: " + humidity);
                System.out.println("Wind Speed: " + windSpeed + " mph");
                System.out.println("Wind Direction: " + windDirection + " (" + windDegree + "°)");

            } else {
                System.err.println("Failed to retrieve data. HTTP Error Code: " + response.getStatusLine().getStatusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
