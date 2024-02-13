package com.retrievedata;

import com.entities.WeatherWeatherstack;
import com.weather.WeatherController;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherstackDataRetrieval {

    public static void downloadAndSetWeatherData(String cityName, String disaster, String description, String apiKey) throws IOException {

        String transformedInput = cityName.toLowerCase().replaceAll("\\s+", "%20");
        String weatherstackResponse = "http://api.weatherstack.com/current?access_key=" + apiKey + "&query=" + transformedInput;

        try {
            JSONObject jsonResponse = WeatherController.downloadWeatherData(weatherstackResponse);

            // Extracting current weather data
            assert jsonResponse != null;
            JSONObject currentWeather = jsonResponse.getJSONObject("current");

            // Extract temperature, pressure, humidity, wind speed, and wind direction
            int temperature = currentWeather.getInt("temperature");
            int pressure = currentWeather.getInt("pressure");
            int humidity = currentWeather.getInt("humidity");
            int windSpeed = currentWeather.getInt("wind_speed");
            int windDegree = currentWeather.getInt("wind_degree");
            String windDirection = currentWeather.getString("wind_dir");
            long dateLong = jsonResponse.getJSONObject("location").getLong("localtime_epoch");
            Date dateObject =  new Date(dateLong * 1000);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = dateFormat.format(dateObject);
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);

            // Create a WeatherWeatherstack object and populate it with the extracted data
            WeatherWeatherstack weather = new WeatherWeatherstack();
            weather.setCountryName(jsonResponse.getJSONObject("location").getString("country"));
            weather.setRegionName(jsonResponse.getJSONObject("location").getString("region"));
            weather.setCityName(cityName);
            weather.setLatitude(jsonResponse.getJSONObject("location").getDouble("lat"));
            weather.setLongitude(jsonResponse.getJSONObject("location").getDouble("lon"));
            weather.setDate(date);
            weather.setTemperature((double) temperature);
            weather.setPressure(pressure);
            weather.setHumidity(humidity);
            weather.setWindDirection(windDirection + " (" + windDegree + "°)");
            weather.setWindSpeed((double) windSpeed);
            weather.setNaturalDisaster(disaster);
            weather.setDescription(description);

            // Save the WeatherWeatherstack object to the database
            WeatherController.saveWeatherData(weather);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
