package com.weather;

import com.entities.WeatherAccuweather;
import com.retrievedata.AccuweatherDataRetrieval;
import com.retrievedata.OpenweatherDataRetrieval;
import com.retrievedata.WeatherstackDataRetrieval;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.entities.WeatherOpenweather;
import com.entities.WeatherWeatherstack;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class WeatherApplication {

    private static final SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactory();

    public static Scanner scanner = new Scanner(System.in);

    private static final String awApiKey = System.getenv("AW_API_KEY");
    private static final String wsApiKey = System.getenv("WS_API_KEY");
    private static final String owApiKey = System.getenv("OW_API_KEY");

    public static void main(String[] args) {

        try {

            while (true) {

                displayMenu();

                String userInput = scanner.nextLine();
                if (!userInput.matches("\\d+")) {
                    System.out.println("Please enter a correct number. (1/2/3/4/5): ");
                    continue;
                }

                switch (userInput) {
                    case "1":
                        addNewNaturalDisaster();
                        break;
                    case "2":
                        viewDisastersByDate();
                        break;
                    case "3":
                        viewDisastersByName();
                        break;
                    case "4":
                        viewDisastersByCityName();
                        break;
                    case "5":
                        exitProgram();
                        break;
                    default:
                        System.out.println("Please enter a correct number: ");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void displayMenu() {
        System.out.println("Enter your choice:");
        System.out.println("1. Add new natural disaster");
        System.out.println("2. View disasters by date");
        System.out.println("3. View disasters by name");
        System.out.println("4. View disasters by city name");
        System.out.println("5. Exit program");
        System.out.print("Choice: ");
    }

    private static void addNewNaturalDisaster() throws IOException {

        System.out.println("Enter a city where natural disaster occurred: ");
        String cityName = scanner.nextLine();

        System.out.println("Enter the name of natural disaster that occurred: ");
        String disaster = scanner.nextLine();

        System.out.println("Enter description to the disaster: ");
        String description = scanner.nextLine();

        try {
            OpenweatherDataRetrieval.getJsonData(cityName, disaster, description, owApiKey);
            AccuweatherDataRetrieval.downloadAndSetWeatherData(cityName, disaster, description, awApiKey);
            WeatherstackDataRetrieval.downloadAndSetWeatherData(cityName, disaster, description, wsApiKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void viewDisastersByDate() {
        System.out.println("Displaying disasters from three different db tables");
    }

    private static void viewDisastersByName() {
        System.out.println("Displaying disasters from three different db tables");
        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(WeatherOpenweather.class)
                .addAnnotatedClass(WeatherAccuweather.class)
                .addAnnotatedClass(WeatherWeatherstack.class)
                .buildSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            System.out.println("Enter disaster name");
            String disasterName = scanner.nextLine();
            List<WeatherOpenweather> queryOpenWeather = session.createQuery("FROM WeatherOpenweather WHERE naturalDisaster= '" + disasterName +"'", WeatherOpenweather.class).getResultList();
            List<WeatherAccuweather> queryAccuweather = session.createQuery("FROM WeatherAccuweather WHERE naturalDisaster= '" + disasterName +"'", WeatherAccuweather.class).getResultList();
            List<WeatherWeatherstack> queryWeatherstack = session.createQuery("FROM WeatherWeatherstack WHERE naturalDisaster= '" + disasterName +"'", WeatherWeatherstack.class).getResultList();

            System.out.println("Disasters details from Openweather");
            for (WeatherOpenweather entity : queryOpenWeather) {
                System.out.println("Natural Disaster: " + entity.getNaturalDisaster()
                        + "\nDescription: " + entity.getDescription()
                        + "\nCountry: " + entity.getCountryName()
                        + "\nCity Name: " + entity.getCityName()
                        + "\nLatitude: " + entity.getLatitude()
                        + "\nLongitude: " + entity.getLongitude()
                        + "\nDate: " + entity.getDate()
                        + "\nTemperature: " + entity.getTemperature()
                        + "\nPressure: " + entity.getPressure()
                        +"\nHumidity: " + entity.getHumidity()
                        +"\nWind Speed: " + entity.getWindSpeed()
                        + "\nWind Degree: " + entity.getWindDirection());

            }
            System.out.println("Disasters details from Accuweather");
            for (WeatherAccuweather entity : queryAccuweather) {
                System.out.println("Natural Disaster: " + entity.getNaturalDisaster()
                        + "\nDescription: " + entity.getDescription()
                        + "\nCountry: " + entity.getCountryName()
                        + "\nCity Name: " + entity.getCityName()
                        + "\nLatitude: " + entity.getLatitude()
                        + "\nLongitude: " + entity.getLongitude()
                        + "\nDate: " + entity.getDate()
                        + "\nTemperature: " + entity.getTemperature()
                        + "\nPressure: " + entity.getPressure()
                        +"\nHumidity: " + entity.getHumidity()
                        + "\nWind Degree: " + entity.getWindDirection()
                        + "\nWind Speed: " + entity.getWindSpeed());

            }
            System.out.println("Disasters details from Weatherstack");
            for (WeatherWeatherstack entity : queryWeatherstack) {
                System.out.println("Natural Disaster: " + entity.getNaturalDisaster()
                        + "\nDescription: " + entity.getDescription()
                        + "\nCountry: " + entity.getCountryName()
                        + "\nRegion Name: " + entity.getRegionName()
                        + "\nCity Name: " + entity.getCityName()
                        + "\nLatitude: " + entity.getLatitude()
                        + "\nLongitude: " + entity.getLongitude()
                        + "\nDate: " + entity.getDate()
                        + "\nTemperature: " + entity.getTemperature()
                        + "\nPressure: " + entity.getPressure()
                        +"\nHumidity: " + entity.getHumidity()
                        + "\nWind Degree: " + entity.getWindDirection()
                        + "\nWind Speed: " + entity.getWindSpeed());

            }

            transaction.commit();
            System.out.println("Weather data fetched successfully!");
        } catch (Exception e) {
            System.out.println("Failed to fetch weather data. Error: " + e.getMessage());
        }
    }

    private static void viewDisastersByCityName() {


        try (Session session = sessionFactory.openSession()) {


        } catch (Exception e) {
            System.out.println("Failed to fetch disaster data by city name. Error: " + e.getMessage());
        }
    }

    private static void exitProgram() {
        System.out.println("Goodbye!");
        System.exit(0);
    }
}
