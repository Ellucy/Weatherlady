package com.handlers;

import com.entities.WeatherData;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class DatabaseConnector {

    private final SessionFactory sessionFactory;

    public DatabaseConnector() {
        this.sessionFactory = SessionFactoryProvider.getSessionFactory();
    }

    public  <T extends WeatherData> void saveWeatherData(T weatherData) {

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