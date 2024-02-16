package com.retrievedata;

import com.entities.WeatherAccuweather;
import com.handlers.APIConnection;
import com.handlers.DatabaseConnector;
import lombok.Data;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class AccuweatherDataRetrieval {

    private final DatabaseConnector databaseConnector;
    private APIConnection apiConnection;
    private final String awApiKey;

    public AccuweatherDataRetrieval(DatabaseConnector databaseConnector, String awApiKey) {
        this.databaseConnector = databaseConnector;
        this.apiConnection = new APIConnection();
        this.awApiKey = awApiKey;
    }

    public void downloadAndSetWeatherData(String cityName, String disaster, String description) throws IOException {

        String transformedInput = cityName.toLowerCase().replaceAll("\\s+", "");
        AccuweatherLocationDetails locationDetails = AccuweatherLocationHandler.getLocationDetails(awApiKey, transformedInput);

        String locationKey = locationDetails.getLocationKey();
        String accuweatherOneDayUrl = "http://dataservice.accuweather.com/forecasts/v1/daily/1day/" + locationKey + "?apikey=" + awApiKey + "&details=true";

        try {

            JSONObject jsonResponse = apiConnection.downloadWeatherData(accuweatherOneDayUrl);

            assert jsonResponse != null;
            JSONObject forecast = jsonResponse.getJSONArray("DailyForecasts").getJSONObject(0);

            // Temperature data
            JSONObject temperature = forecast.getJSONObject("Temperature");
            double minTemp = temperature.getJSONObject("Minimum").getDouble("Value");
            double maxTemp = temperature.getJSONObject("Maximum").getDouble("Value");
            double averageTemp = (minTemp + maxTemp) / 2;

            // Humidity data
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

            // Transform fahrenheit to Celsius ((1°F − 32) × 5/9)
            double averageTempCelsius = (averageTemp - 32) * 5 / 9;
            DecimalFormat df = new DecimalFormat("#.##");
            double roundedTemp = Double.parseDouble(df.format(averageTempCelsius));

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
            weather.setTemperature(roundedTemp);
            weather.setHumidity(averageRH);
            weather.setWindDirection(windDirectionLocalized + " (" + windDirectionDegrees + "°)");
            weather.setWindSpeed(windSpeed);

            // Save Weather entity to the database
            databaseConnector.saveWeatherData(weather);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}