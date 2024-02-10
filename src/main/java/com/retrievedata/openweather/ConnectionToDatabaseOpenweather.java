package com.retrievedata.openweather;


import com.entities.WeatherOpenweather;
import org.hibernate.Session;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


public class ConnectionToDatabaseOpenweather {
    private static final SessionFactory sessionFactory;

    static {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(WeatherOpenweather.class)
                .buildSessionFactory();
    }
    public static void insertOpenweatherData(WeatherOpenweather openweather) {

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            WeatherOpenweather managedWeather = session.merge(openweather);
            session.merge(managedWeather);

            transaction.commit();
            System.out.println("Weather data saved successfully!");
        } catch (Exception e) {
            System.out.println("Failed to save weather data. Error: " + e.getMessage());
        }
    }
}