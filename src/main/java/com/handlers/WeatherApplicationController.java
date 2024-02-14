package com.handlers;

import com.entities.WeatherAccuweather;
import com.entities.WeatherOpenweather;
import com.entities.WeatherWeatherstack;
import com.retrievedata.AccuweatherDataRetrieval;
import com.retrievedata.OpenweatherDataRetrieval;
import com.retrievedata.WeatherstackDataRetrieval;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WeatherApplicationController {

    private static final String awApiKey = System.getenv("AW_API_KEY");
    private static final String wsApiKey = System.getenv("WS_API_KEY");
    private static final String owApiKey = System.getenv("OW_API_KEY");

    private static final SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactory();

    public static void addingNaturalDisaster(String cityName, String disaster, String description) {
        try {
            OpenweatherDataRetrieval.downloadAndSetWeatherData(cityName, disaster, description, owApiKey);
            AccuweatherDataRetrieval.downloadAndSetWeatherData(cityName, disaster, description, awApiKey);
            WeatherstackDataRetrieval.downloadAndSetWeatherData(cityName, disaster, description, wsApiKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean getDisastersByDate(List<WeatherOpenweather> openweatherDisasters, Timestamp date,
                                             List<WeatherAccuweather> accuweatherDisasters,
                                             List<WeatherWeatherstack> weatherstackDisasters) {
        try (Session session = sessionFactory.openSession()) {

            openweatherDisasters.addAll(session.createQuery("FROM WeatherOpenweather WHERE date= :date ", WeatherOpenweather.class)
                    .setParameter("date", date)
                    .getResultList());
            accuweatherDisasters.addAll(session.createQuery("FROM WeatherAccuweather WHERE date= :date ", WeatherAccuweather.class)
                    .setParameter("date", date)
                    .getResultList());
            weatherstackDisasters.addAll(session.createQuery("FROM WeatherWeatherstack WHERE date= :date ", WeatherWeatherstack.class)
                    .setParameter("date", date)
                    .getResultList());
            return true;

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            return false;
        }
    }

    public static Timestamp convertStringToTimestamp(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(dateString);
            return new Timestamp(parsedDate.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean getDisastersByName(List<WeatherOpenweather> openweatherDisasters, String disasterName,
                                             List<WeatherAccuweather> accuweatherDisasters,
                                             List<WeatherWeatherstack> weatherstackDisasters) {
        try (Session session = sessionFactory.openSession()) {

            openweatherDisasters.addAll(session.createQuery("FROM WeatherOpenweather WHERE naturalDisaster= :naturalDisaster", WeatherOpenweather.class)
                    .setParameter("naturalDisaster", disasterName)
                    .getResultList());
            accuweatherDisasters.addAll(session.createQuery("FROM WeatherAccuweather WHERE naturalDisaster= :naturalDisaster", WeatherAccuweather.class)
                    .setParameter("naturalDisaster", disasterName)
                    .getResultList());
            weatherstackDisasters.addAll(session.createQuery("FROM WeatherWeatherstack WHERE naturalDisaster= :naturalDisaster", WeatherWeatherstack.class)
                    .setParameter("naturalDisaster", disasterName)
                    .getResultList());

            return true;

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            return false;
        }
    }

    public static boolean getDisastersByCityName(List<WeatherOpenweather> openweatherDisasters, String cityName,
                                                 List<WeatherAccuweather> accuweatherDisasters,
                                                 List<WeatherWeatherstack> weatherstackDisasters) {
        try (Session session = sessionFactory.openSession()) {

            openweatherDisasters.addAll(session.createQuery("FROM WeatherOpenweather WHERE cityName = :cityName", WeatherOpenweather.class)
                    .setParameter("cityName", cityName)
                    .getResultList());

            accuweatherDisasters.addAll(session.createQuery("FROM WeatherAccuweather WHERE cityName = :cityName", WeatherAccuweather.class)
                    .setParameter("cityName", cityName)
                    .getResultList());

            weatherstackDisasters.addAll(session.createQuery("FROM WeatherWeatherstack WHERE cityName = :cityName", WeatherWeatherstack.class)
                    .setParameter("cityName", cityName)
                    .getResultList());

            return true;
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            return false;
        }
    }

    public static boolean getDisastersByCountryName(List<WeatherOpenweather> openweatherDisasters, String countryName,
                                                    List<WeatherAccuweather> accuweatherDisasters,
                                                    List<WeatherWeatherstack> weatherstackDisasters, String[] words, StringBuilder displayStringBuilder) {
        try (Session session = sessionFactory.openSession()) {
            for (String word : words) {
                if (!word.isEmpty()) {
                    String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
                    displayStringBuilder.append(capitalizedWord).append(" ");
                }
            }

            String countryCode = CountryCodeConverter.convertCountryNameToCode(countryName);
            System.out.println(countryCode);

            openweatherDisasters.addAll(session.createQuery("FROM WeatherOpenweather WHERE countryName = :countryCode", WeatherOpenweather.class)
                    .setParameter("countryCode", countryCode)
                    .getResultList());

            accuweatherDisasters.addAll(session.createQuery("FROM WeatherAccuweather WHERE countryName = :countryName", WeatherAccuweather.class)
                    .setParameter("countryName", countryName)
                    .getResultList());

            weatherstackDisasters.addAll(session.createQuery("FROM WeatherWeatherstack WHERE countryName = :countryName", WeatherWeatherstack.class)
                    .setParameter("countryName", countryName)
                    .getResultList());
            return true;

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            return false;
        }
    }
}
