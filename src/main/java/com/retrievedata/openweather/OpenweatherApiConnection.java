package com.retrievedata.openweather;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

class OpenweatherApiConnection {
    private static final String API_KEY = System.getenv("OW_API_KEY");
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";

    //Create a client to get data form OpenWeather ----> return ENTITY
    public static String getWeatherData(String cityName) throws IOException {
        String openweatherUrl = String.format(API_URL, cityName, API_KEY);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(openweatherUrl);
        HttpResponse response = httpClient.execute(httpGet);
        if (response.getStatusLine().getStatusCode() != 200) {
            System.err.println("Failed to retrieve data. HTTP Error Code: " + response.getStatusLine().getStatusCode());
        }
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity);
    }
}
