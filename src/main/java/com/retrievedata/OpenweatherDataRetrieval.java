package com.retrievedata;

import com.entities.WeatherOpenweather;
import com.handlers.APIConnection;
import com.handlers.DatabaseConnector;
import lombok.Data;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
@Data
public class OpenweatherDataRetrieval {

    private final DatabaseConnector databaseConnector;
    private APIConnection apiConnection;
    private final String owApiKey;
    public OpenweatherDataRetrieval(DatabaseConnector databaseConnector, String owApiKey) {
        this.databaseConnector = databaseConnector;
        this.apiConnection = new APIConnection();
        this.owApiKey = owApiKey;
    }

    public void downloadAndSetWeatherData(String cityName, String disaster, String description) {

        String transformedInput = cityName.toLowerCase().replaceAll("\\s+", "%20");
        String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";

        try {

            JSONObject jsonData = apiConnection.downloadWeatherData(String.format(API_URL, transformedInput, owApiKey));

            assert jsonData != null;
            String countryName = jsonData.getJSONObject("sys").getString("country");
            double latitude = jsonData.getJSONObject("coord").getDouble("lat");
            double longitude = jsonData.getJSONObject("coord").getDouble("lon");
            long dateLong = jsonData.getInt("dt");
            double temperature = jsonData.getJSONObject("main").getDouble("temp");
            int pressure = jsonData.getJSONObject("main").getInt("pressure");
            int humidity = jsonData.getJSONObject("main").getInt("humidity");
            double windDirection = jsonData.getJSONObject("wind").getDouble("deg");
            double windSpeed = jsonData.getJSONObject("wind").getDouble("speed");
            Date dateObject = new Date(dateLong * 1000);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = dateFormat.format(dateObject);
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);

            WeatherOpenweather weatherOpenweather = new WeatherOpenweather();
            weatherOpenweather.setCountryName(countryName);
            weatherOpenweather.setCityName(cityName);
            weatherOpenweather.setLatitude(latitude);
            weatherOpenweather.setLongitude(longitude);
            weatherOpenweather.setDate(date);
            weatherOpenweather.setTemperature(temperature);
            weatherOpenweather.setPressure(pressure);
            weatherOpenweather.setHumidity(humidity);
            weatherOpenweather.setWindDirection(windDirection + "°");
            weatherOpenweather.setWindSpeed(windSpeed);
            weatherOpenweather.setNaturalDisaster(disaster);
            weatherOpenweather.setDescription(description);

            databaseConnector.saveWeatherData(weatherOpenweather);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}