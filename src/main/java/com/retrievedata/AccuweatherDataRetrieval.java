package com.retrievedata;

import com.entities.WeatherAccuweather;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.retrievedata.APIConnection.downloadWeatherData;

public class AccuweatherDataRetrieval {

    public static void downloadAndSetWeatherData(String cityName, String disaster, String description, String apiKey) throws IOException {

        String transformedInput = cityName.toLowerCase().replaceAll("\\s+", "");

        AccuweatherLocationDetails locationDetails = AccuweatherLocationHandler.getLocationDetails(apiKey, transformedInput);

        String locationKey = locationDetails.getLocationKey();

        String accuweatherOneDayUrl = "http://dataservice.accuweather.com/forecasts/v1/daily/1day/" + locationKey + "?apikey=" + apiKey + "&details=true";

        try {

            JSONObject jsonResponse = downloadWeatherData(accuweatherOneDayUrl);

            // DailyForecasts data, Information for current day, the first day in array (index 0)
            assert jsonResponse != null;
            JSONObject forecast = jsonResponse.getJSONArray("DailyForecasts").getJSONObject(0);

            // Temperature data
            JSONObject temperature = forecast.getJSONObject("Temperature");
            double minTemp = temperature.getJSONObject("Minimum").getDouble("Value");
            double maxTemp = temperature.getJSONObject("Maximum").getDouble("Value");
            double averageTemp = (minTemp + maxTemp) / 2;

            // Relative humidity data
            JSONObject relativeHumidity = forecast.getJSONObject("Day").getJSONObject("RelativeHumidity");
            int averageRH = relativeHumidity.getInt("Average");

            // Wind data
            JSONObject wind = forecast.getJSONObject("Day").getJSONObject("Wind");
            double windSpeed = wind.getJSONObject("Speed").getDouble("Value");
            int windDirectionDegrees = wind.getJSONObject("Direction").getInt("Degrees");
            String windDirectionLocalized = wind.getJSONObject("Direction").getString("Localized");

            // Parse and extract date
            String dateString = forecast.getString("Date");
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);

            // Create Weather entity
            WeatherAccuweather weather = new WeatherAccuweather();
            weather.setCountryName(locationDetails.getCountryName());
            weather.setRegionName(locationDetails.getRegionName());
            weather.setCityName(locationDetails.getCityName());
            weather.setLatitude(locationDetails.getLatitude());
            weather.setLongitude(locationDetails.getLongitude());
            weather.setDate(date);
            weather.setNaturalDisaster(disaster);
            weather.setDescription(description);

            // Transform fahrenheit to Celsius ((1°F − 32) × 5/9)
            double averageTempCelsius = (averageTemp - 32) * 5 / 9;
            DecimalFormat df = new DecimalFormat("#.##");
            double roundedTemp = Double.parseDouble(df.format(averageTempCelsius));
            weather.setTemperature(roundedTemp);

            weather.setHumidity(averageRH);
            weather.setWindDirection(windDirectionLocalized + " (" + windDirectionDegrees + "°)");
            weather.setWindSpeed(windSpeed);

            // Save Weather entity to the database
            DatabaseConnector.saveWeatherData(weather);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}