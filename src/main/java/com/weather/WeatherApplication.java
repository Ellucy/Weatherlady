package com.weather;

import com.entities.Location;
import com.entities.WeatherAccuweather;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Scanner;

public class WeatherApplication {

    private static final SessionFactory sessionFactory;

    static {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(Location.class)
                .addAnnotatedClass(WeatherAccuweather.class)
                .buildSessionFactory();
    }

    public static void main(String[] args) {

        try (Session session = sessionFactory.openSession()) {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                displayMenu();
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        addLocation(session);
                        break;
                    case 2:
                        displayLocations(session);
                        break;
                    case 3:
                        downloadWeatherValues(session);
                        break;
                    case 4:
                        exitProgram();
                        break;
                    default:
                        System.out.println("Please enter a correct number.");
                }
            }
        }
    }

    private static void displayMenu() {
        System.out.println("Enter your choice (1/2/3/4): ");
    }

    private static void downloadWeatherValues(Session session) {
        System.out.println("Downloading weather values");
    }

    private static void exitProgram() {
        System.out.println("Goodbye!");
        System.exit(0);
    }

    private static void displayLocations(Session session) {
        System.out.println("Displaying locations");
    }

    private static void addLocation(Session session) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter longitude:");
        double longitude = scanner.nextDouble();
        System.out.println("Enter latitude:");
        double latitude = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.println("Enter city name:");
        String cityName = scanner.nextLine();
        System.out.println("Enter region (optional):");
        String region = scanner.nextLine();
        System.out.println("Enter country name:");
        String countryName = scanner.nextLine();

        Location location = new Location();
        location.setLongitude(longitude);
        location.setLatitude(latitude);
        location.setCityName(cityName);
        location.setRegion(region);
        location.setCountryName(countryName);

        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(location);
            transaction.commit();
            System.out.println("Location added successfully!");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Failed to add location. Error: " + e.getMessage());
        }
    }

}
