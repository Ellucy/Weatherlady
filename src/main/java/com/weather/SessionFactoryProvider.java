package com.weather;

import com.entities.WeatherAccuweather;
import com.entities.WeatherOpenweather;
import com.entities.WeatherWeatherstack;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionFactoryProvider {

    @Getter
    private static final SessionFactory sessionFactory;

    static {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(WeatherOpenweather.class)
                .addAnnotatedClass(WeatherAccuweather.class)
                .addAnnotatedClass(WeatherWeatherstack.class)
                .buildSessionFactory();
    }

}

