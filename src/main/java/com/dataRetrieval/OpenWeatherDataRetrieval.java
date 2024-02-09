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
    private static String city = scanner.nextLine();

    public static void main(String[] args) {
        try {


            String jsonData = OpenWeatherAPI.getWeatherData(city);
            System.out.println(jsonData);
            JSONObject jsonObject = new JSONObject(jsonData);
            float longitude = jsonObject.getJSONObject("coord").getFloat("lon");
            float latitude = jsonObject.getJSONObject("coord").getFloat("lat");
            float temperature = jsonObject.getJSONObject("main").getFloat("temp");
            float humidity = jsonObject.getJSONObject("main").getFloat("humidity");
            float windSpeed = jsonObject.getJSONObject("wind").getFloat("speed");
            Database.insertWeatherData(longitude, latitude,city, temperature, humidity, windSpeed);
            System.out.println("Weather data inserted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
   }
}
class OpenWeatherAPI {
    private static final String API_KEY = "your api key";
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

class Database {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/weather_db";
    private static final String JDBC_USER = "username";
    private static final String JDBC_PASSWORD = "password";

  public static void insertWeatherData(float longitude, float latitude, String city, float temperature, float humidity, float windSpeed) throws SQLException {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "INSERT INTO weather (longitude, latitude,city, temperature, humidity, windSpeed) VALUES (?,?,?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                 statement.setFloat(1, longitude);
                statement.setFloat(2, latitude);
                statement.setString(3, city);
                statement.setFloat(4, temperature);
                statement.setFloat(5, humidity);
                statement.setFloat(6, windSpeed);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}