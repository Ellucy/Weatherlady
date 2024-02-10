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

    private static final SessionFactory sessionFactory;

    static {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(WeatherAccuweather.class)
                .buildSessionFactory();
    }

    public static void main(String[] args) {

        String apiKey = System.getenv("AW_API_KEY");

        try {
            downloadAndSaveWeatherData(apiKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void downloadAndSaveWeatherData(String apiKey) throws IOException {

        String requestedCity = "Liverpool";
        String transformedInput = requestedCity.toLowerCase().replaceAll("\\s+", "");

        LocationDetails locationDetails = AccuweatherLocationHandler.getLocationDetails(apiKey, transformedInput);

        String locationKey = locationDetails.getLocationKey();

        String accuweatherOneDayUrl = "http://dataservice.accuweather.com/forecasts/v1/daily/1day/" + locationKey + "?apikey=" + apiKey + "&details=true";
        String accuweatherFiveDaysUrl = "http://dataservice.accuweather.com/forecasts/v1/daily/5day/349727?apikey=" + apiKey + "&details=true";

        try {

            JSONObject jsonResponse = downloadWeatherData(accuweatherOneDayUrl);

            // DailyForecasts data
            // Information for today, the first day in array (index 0)
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
            weather.setNaturalDisaster("Volcanic eruption");
            weather.setDescription("Powerful, VEI 7");

            // Transform fahrenheit to Celsius ((1°F − 32) × 5/9)
            double averageTempCelsius = (averageTemp - 32) * 5 / 9;
            DecimalFormat df = new DecimalFormat("#.##");
            double roundedTemp = Double.parseDouble(df.format(averageTempCelsius));
            weather.setTemperature(roundedTemp);

            weather.setHumidity(averageRH);
            weather.setWindDirection(windDirectionLocalized + " (" + windDirectionDegrees + "°)");
            weather.setWindSpeed(windSpeed);

            // Save Weather entity to the database
            saveWeatherData(weather);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveWeatherData(WeatherAccuweather weather) {

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            WeatherAccuweather managedWeather = (WeatherAccuweather) session.merge(weather);
            session.merge(managedWeather);

            transaction.commit();
            System.out.println("Weather data saved successfully!");
        } catch (Exception e) {
            System.out.println("Failed to save weather data. Error: " + e.getMessage());
        }
    }
}