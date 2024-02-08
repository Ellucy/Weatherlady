package com.dataretrieval;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AccuweatherDataRetrieval {

    public static void main(String[] args) {

        String apiKey = System.getenv("AW_API_KEY");

        String accuweatherResponse = "http://dataservice.accuweather.com/forecasts/v1/daily/1day/349727?apikey=" + apiKey + "&details=true";

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet getRequest = new HttpGet(accuweatherResponse);

        try {
            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                // Parse JSON response
                JSONObject jsonResponse = new JSONObject(result.toString());

                // Headline data
                JSONObject headline = jsonResponse.getJSONObject("Headline");
                System.out.println("Headline: " + headline.getString("Text"));

                // DailyForecasts data
                // Information for today, the first day in array (index 0)
                JSONObject forecast = jsonResponse.getJSONArray("DailyForecasts").getJSONObject(0);

                // Temperature data
                JSONObject temperature = forecast.getJSONObject("Temperature");
                double minTemp = temperature.getJSONObject("Minimum").getDouble("Value");
                double maxTemp = temperature.getJSONObject("Maximum").getDouble("Value");
                double averageTemp = (minTemp + maxTemp) / 2;
                System.out.println("Average Temperature: " + averageTemp);

                // Relative humidity data
                JSONObject relativeHumidity = forecast.getJSONObject("Day").getJSONObject("RelativeHumidity");
                int minRH = relativeHumidity.getInt("Minimum");
                int maxRH = relativeHumidity.getInt("Maximum");
                int averageRH = relativeHumidity.getInt("Average");
                System.out.println("Average Relative Humidity: " + averageRH);

                // Wind data
                JSONObject wind = forecast.getJSONObject("Day").getJSONObject("Wind");
                double windSpeed = wind.getJSONObject("Speed").getDouble("Value");
                int windDirectionDegrees = wind.getJSONObject("Direction").getInt("Degrees");
                String windDirectionLocalized = wind.getJSONObject("Direction").getString("Localized");
                System.out.println("Wind Speed: " + windSpeed + " mph");
                System.out.println("Wind Direction: " + windDirectionLocalized + " (" + windDirectionDegrees + "°)");
            } else {
                System.err.println("Failed to retrieve data. HTTP Error Code: " + response.getStatusLine().getStatusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}