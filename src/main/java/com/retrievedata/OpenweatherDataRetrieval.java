package com.retrievedata;

import com.entities.WeatherOpenweather;
import org.json.JSONObject;

public class OpenweatherDataRetrieval {

    public static void getJsonData(String cityName) {
        String API_KEY = System.getenv("OW_API_KEY");
        String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";
        try {
            JSONObject jsonData = APIConnection.downloadWeatherData(String.format(API_URL, cityName, API_KEY));
            assert jsonData != null;
            String country = jsonData.getJSONObject("sys").getString("country");
            double latitude = jsonData.getJSONObject("coord").getDouble("lat");
            double longitude = jsonData.getJSONObject("coord").getDouble("lon");
            double temperature = jsonData.getJSONObject("main").getDouble("temp");
            int pressure = jsonData.getJSONObject("main").getInt("pressure");
            int humidity = jsonData.getJSONObject("main").getInt("humidity");
            double windSpeed = jsonData.getJSONObject("wind").getDouble("speed");

            WeatherOpenweather weatherOpenweather = new WeatherOpenweather();
            weatherOpenweather.setCountry(country);
            weatherOpenweather.setCityName(cityName);
            weatherOpenweather.setLatitude(latitude);
            weatherOpenweather.setLongitude(longitude);
            weatherOpenweather.setTemperature(temperature);
            weatherOpenweather.setPressure(pressure);
            weatherOpenweather.setHumidity(humidity);
            weatherOpenweather.setWindSpeed(windSpeed);

            OpenweatherDatabaseConnection.insertOpenweatherData(weatherOpenweather);
            System.out.println("Weather data inserted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}