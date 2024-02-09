package com.dataretrieval;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AccuweatherDataRetrieval {

    public static void main(String[] args) {

        String apiKey = System.getenv("API_KEY");

        String postmanQueryCityUrl = "http://dataservice.accuweather.com/locations/v1/cities/search?apikey=" + apiKey + "&q=tallinn";
        String postmanQueryCityWeatherUrl = "http://dataservice.accuweather.com/forecasts/v1/daily/1day/127964?apikey=" + apiKey;

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet getRequest = new HttpGet(postmanQueryCityWeatherUrl);

        try {
            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() == 200) {

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                String line;
                StringBuilder result = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                System.out.println("Response: " + result.toString());
            } else {
                System.err.println("Failed to retrieve data. HTTP Error Code: " +
                        response.getStatusLine().getStatusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
