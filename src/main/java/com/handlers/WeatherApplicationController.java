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
import java.util.List;

public class WeatherApplicationController {

    private  final SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactory();
    private final OpenweatherDataRetrieval openweatherDataRetrieval;
    private final AccuweatherDataRetrieval accuweatherDataRetrieval;
    private final WeatherstackDataRetrieval weatherstackDataRetrieval;
    private final CountryCodeConverter countryCodeConverter;
    public WeatherApplicationController() {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        this.openweatherDataRetrieval = new OpenweatherDataRetrieval(databaseConnector);
        this.accuweatherDataRetrieval = new AccuweatherDataRetrieval(databaseConnector);
        this.weatherstackDataRetrieval = new WeatherstackDataRetrieval(databaseConnector);
        this.countryCodeConverter= new CountryCodeConverter();
    }

    public void addingNaturalDisaster(String cityName, String disaster, String description) {
        try {
            openweatherDataRetrieval.downloadAndSetWeatherData(cityName, disaster, description);
            accuweatherDataRetrieval.downloadAndSetWeatherData(cityName, disaster, description);
            weatherstackDataRetrieval.downloadAndSetWeatherData(cityName, disaster, description);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getDisastersByDate(List<WeatherOpenweather> openweatherDisasters, Timestamp date,
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

    public boolean getDisastersByName(List<WeatherOpenweather> openweatherDisasters, String disasterName,
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

    public boolean getDisastersByCityName(List<WeatherOpenweather> openweatherDisasters, String cityName,
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

    public boolean getDisastersByCountryName(List<WeatherOpenweather> openweatherDisasters, String countryName,
                                                    List<WeatherAccuweather> accuweatherDisasters,
                                                    List<WeatherWeatherstack> weatherstackDisasters, String[] words, StringBuilder displayStringBuilder) {
        try (Session session = sessionFactory.openSession()) {
            for (String word : words) {
                if (!word.isEmpty()) {
                    String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
                    displayStringBuilder.append(capitalizedWord).append(" ");
                }
            }

            String countryCode = countryCodeConverter.convertCountryNameToCode(countryName);
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
