package com.retrievedata;


import com.entities.WeatherAccuweather;
import com.entities.WeatherOpenweather;
import com.entities.WeatherWeatherstack;
import org.hibernate.Session;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


public class DatabaseConnector {
    private static final SessionFactory sessionFactory;

    static {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(WeatherOpenweather.class)
                .addAnnotatedClass(WeatherAccuweather.class)
                .addAnnotatedClass(WeatherWeatherstack.class)
                .buildSessionFactory();
    }
    public static <T extends WeatherData> void saveWeatherData(T weatherData) {

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            T managedWeatherData = session.merge(weatherData);
            session.merge(managedWeatherData);
            transaction.commit();
            System.out.println("Weather data saved successfully!");
        } catch (Exception e) {
            System.out.println("Failed to save weather data. Error: " + e.getMessage());
        }
    }
}