package com.dataRetrieval;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class OpenWeatherDataRetrieval {

    private static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        System.out.println("Enter city name");
        String city = scanner.nextLine();
        try {

            String jsonData = OpenWeatherAPI.getWeatherData(city);
            System.out.println(jsonData);
            JSONObject jsonObject = new JSONObject(jsonData);
            float longitude = jsonObject.getJSONObject("coord").getFloat("lon");
            float latitude = jsonObject.getJSONObject("coord").getFloat("lat");
            float temperature = jsonObject.getJSONObject("main").getFloat("temp");
            float humidity = jsonObject.getJSONObject("main").getFloat("humidity");
            float windSpeed = jsonObject.getJSONObject("wind").getFloat("speed");
        } catch (Exception e) {
            e.printStackTrace();
        }
   }
}
class OpenWeatherAPI {
    private static final String API_KEY = "93853d6652c1788783d7471059c2cad0";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";

    //Create a client to get data form OpenWeather ----> return ENTITY
    public static String getWeatherData(String city) throws IOException {
        String apiUrl = String.format(API_URL, city, API_KEY);
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(apiUrl);
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity);
    }
}
