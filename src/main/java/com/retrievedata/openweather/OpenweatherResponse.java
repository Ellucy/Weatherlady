package com.retrievedata.openweather;

import com.entities.WeatherOpenweather;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Scanner;

public class OpenweatherResponse {

        public void getJsonData(String cityName){
        try {
            String jsonData = OpenweatherApiConnection.getWeatherData(cityName);
            JSONObject jsonObject = new JSONObject(jsonData);
            String country = jsonObject.getJSONObject("sys").getString("country");
            double latitude = jsonObject.getJSONObject("coord").getDouble("lat");
            double longitude = jsonObject.getJSONObject("coord").getDouble("lon");
            double temperature = jsonObject.getJSONObject("main").getDouble("temp");
            int pressure = jsonObject.getJSONObject("main").getInt("pressure");
            int humidity = jsonObject.getJSONObject("main").getInt("humidity");
            double windSpeed = jsonObject.getJSONObject("wind").getDouble("speed");

            WeatherOpenweather weatherOpenweather = new WeatherOpenweather();
            weatherOpenweather.setCountry(country);
            weatherOpenweather.setCityName(cityName);
            weatherOpenweather.setLatitude(latitude);
            weatherOpenweather.setLongitude(longitude);
            weatherOpenweather.setTemperature(temperature);
            weatherOpenweather.setPressure(pressure);
            weatherOpenweather.setHumidity(humidity);
            weatherOpenweather.setWindSpeed(windSpeed);
            weatherOpenweather.setNaturalDisaster("Volcanic eruption");
            weatherOpenweather.setDescription("Powerful, VEI 7");

            OpenweatherDatabaseConnection.insertOpenweatherData(weatherOpenweather);
            System.out.println("Weather data inserted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }
}