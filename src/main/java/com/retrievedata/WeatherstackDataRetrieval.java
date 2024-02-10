package com.retrievedata;

import com.entities.WeatherWeatherstack;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.hibernate.cfg.Configuration;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Date;

import static com.retrievedata.WeatherData.downloadWeatherData;

public class WeatherstackDataRetrieval {

    private static final SessionFactory sessionFactory;

    static {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(WeatherWeatherstack.class)
                .buildSessionFactory();
    }

    public static void main(String[] args) {

        String apiKey = System.getenv("WS_API_KEY");

        try {
            downloadAndSaveWeatherData(apiKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void downloadAndSaveWeatherData(String apiKey) throws IOException {

        String requestedCity = "California";
        String transformedInput = requestedCity.toLowerCase().replaceAll("\\s+", "");

        String weatherstackResponse = "http://api.weatherstack.com/current?access_key=" + apiKey + "&query=" + transformedInput;

        try {
            JSONObject jsonResponse = downloadWeatherData(weatherstackResponse);

                // Extracting current weather data
                JSONObject currentWeather = jsonResponse.getJSONObject("current");

                // Extract temperature, pressure, humidity, wind speed, and wind direction
                int temperature = currentWeather.getInt("temperature");
                int pressure = currentWeather.getInt("pressure");
                int humidity = currentWeather.getInt("humidity");
                int windSpeed = currentWeather.getInt("wind_speed");
                int windDegree = currentWeather.getInt("wind_degree");
                String windDirection = currentWeather.getString("wind_dir");

                // Print extracted data
                System.out.println("Average Temperature: " + temperature);
                System.out.println("Pressure: " + pressure);
                System.out.println("Humidity: " + humidity);
                System.out.println("Wind Speed: " + windSpeed + " mph");
                System.out.println("Wind Direction: " + windDirection + " (" + windDegree + "°)");

                // Create a WeatherWeatherstack object and populate it with the extracted data
                WeatherWeatherstack weather = new WeatherWeatherstack();
                weather.setCountryName(jsonResponse.getJSONObject("location").getString("country"));
                weather.setRegionName(jsonResponse.getJSONObject("location").getString("region"));
                weather.setCityName(jsonResponse.getJSONObject("location").getString("name"));
                weather.setLatitude(jsonResponse.getJSONObject("location").getDouble("lat"));
                weather.setLongitude(jsonResponse.getJSONObject("location").getDouble("lon"));
                weather.setDate(new Date(jsonResponse.getJSONObject("location").getLong("localtime_epoch") * 1000));
                weather.setTemperature((double) temperature);
                weather.setPressure(pressure);
                weather.setHumidity(humidity);
                weather.setWindDirection(windDirection + " (" + windDegree + "°)");
                weather.setWindSpeed((double) windSpeed);

                // Save the WeatherWeatherstack object to the database
                saveWeatherData(weather);


//            } else {
//                System.err.println("Failed to retrieve data. HTTP Error Code: " + response.getStatusLine().getStatusCode());
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void saveWeatherData(WeatherWeatherstack weather) {

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            WeatherWeatherstack managedWeather = session.merge(weather);
            session.merge(managedWeather);

            transaction.commit();
            System.out.println("Weather data saved successfully!");
        } catch (Exception e) {
            System.out.println("Failed to save weather data. Error: " + e.getMessage());
        }
    }
}
