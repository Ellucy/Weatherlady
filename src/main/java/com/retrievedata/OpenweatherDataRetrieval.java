package com.retrievedata;

import com.entities.WeatherOpenweather;
import org.json.JSONObject;

import java.util.Date;

public class OpenweatherDataRetrieval {

    public static void downloadAndSetWeatherData(String cityName, String disaster, String description, String apiKey) {

        String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";
        try {
            JSONObject jsonData = APIConnection.downloadWeatherData(String.format(API_URL, cityName, apiKey));
            assert jsonData != null;
            String countryName = jsonData.getJSONObject("sys").getString("country");
            double latitude = jsonData.getJSONObject("coord").getDouble("lat");
            double longitude = jsonData.getJSONObject("coord").getDouble("lon");
            int dateString = jsonData.getInt("dt");
            double temperature = jsonData.getJSONObject("main").getDouble("temp");
            int pressure = jsonData.getJSONObject("main").getInt("pressure");
            int humidity = jsonData.getJSONObject("main").getInt("humidity");
            double windDirection = jsonData.getJSONObject("wind").getDouble("deg");
            double windSpeed = jsonData.getJSONObject("wind").getDouble("speed");

            WeatherOpenweather weatherOpenweather = new WeatherOpenweather();
            weatherOpenweather.setCountryName(countryName);
            weatherOpenweather.setCityName(cityName);
            weatherOpenweather.setLatitude(latitude);
            weatherOpenweather.setLongitude(longitude);
            Date date = new Date(dateString);
            weatherOpenweather.setDate(date);
            weatherOpenweather.setTemperature(temperature);
            weatherOpenweather.setPressure(pressure);
            weatherOpenweather.setHumidity(humidity);
            weatherOpenweather.setWindDirection(windDirection + "°");
            weatherOpenweather.setWindSpeed(windSpeed);
            weatherOpenweather.setNaturalDisaster(disaster);
            weatherOpenweather.setDescription(description);

            DatabaseConnector.saveWeatherData(weatherOpenweather);
            System.out.println("Weather data inserted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}