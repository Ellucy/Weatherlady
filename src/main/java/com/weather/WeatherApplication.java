package com.weather;

import com.entities.DataEntity;
import com.entities.WeatherAccuweather;
import com.retrievedata.AccuweatherDataRetrieval;
import com.retrievedata.OpenweatherDataRetrieval;
import com.retrievedata.WeatherstackDataRetrieval;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.entities.WeatherOpenweather;
import com.entities.WeatherWeatherstack;
import org.hibernate.Transaction;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
                        viewDisastersByCountryName();
                        break;
                    case "6":
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
        System.out.println("3. View disasters by disaster name");
        System.out.println("4. View disasters by city name");
        System.out.println("5. View disasters by country name");
        System.out.println("6. Exit program");
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
            OpenweatherDataRetrieval.downloadAndSetWeatherData(cityName, disaster, description, owApiKey);
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

        try (Session session = sessionFactory.openSession()) {

            Transaction transaction = session.beginTransaction();
            System.out.println("Please enter the name of the natural disaster you want to check: ");
            String disasterName = scanner.nextLine();
            String displayString = disasterName.substring(0, 1).toUpperCase() + disasterName.substring(1).toLowerCase();

            List<WeatherOpenweather> queryOpenWeather = session.createQuery("FROM WeatherOpenweather WHERE naturalDisaster= '" + disasterName + "'", WeatherOpenweather.class).getResultList();
            List<WeatherAccuweather> queryAccuweather = session.createQuery("FROM WeatherAccuweather WHERE naturalDisaster= '" + disasterName + "'", WeatherAccuweather.class).getResultList();
            List<WeatherWeatherstack> queryWeatherstack = session.createQuery("FROM WeatherWeatherstack WHERE naturalDisaster= '" + disasterName + "'", WeatherWeatherstack.class).getResultList();

            displayDisasters(displayString, queryOpenWeather, queryAccuweather, queryWeatherstack);

            System.out.println("Weather data fetched successfully!");

        } catch (Exception e) {
            System.out.println("Failed to fetch weather data. Error: " + e.getMessage());
        }
    }

    private static void viewDisastersByCityName() {

        try (Session session = sessionFactory.openSession()) {

            System.out.println("Enter the city name to view disasters that have happened there: ");
            String cityName = scanner.nextLine();
            String displayString = cityName.substring(0, 1).toUpperCase() + cityName.substring(1).toLowerCase();

            List<WeatherOpenweather> openweatherDisasters = session.createQuery("FROM WeatherOpenweather WHERE cityName = :cityName", WeatherOpenweather.class)
                    .setParameter("cityName", cityName)
                    .getResultList();

            List<WeatherAccuweather> accuweatherDisasters = session.createQuery("FROM WeatherAccuweather WHERE cityName = :cityName", WeatherAccuweather.class)
                    .setParameter("cityName", cityName)
                    .getResultList();

            List<WeatherWeatherstack> weatherstackDisasters = session.createQuery("FROM WeatherWeatherstack WHERE cityName = :cityName", WeatherWeatherstack.class)
                    .setParameter("cityName", cityName)
                    .getResultList();

            displayDisasters(displayString, openweatherDisasters, accuweatherDisasters, weatherstackDisasters);

        } catch (Exception e) {
            System.out.println("Failed to fetch disaster data by city name. Error: " + e.getMessage());
        }
    }

    private static void viewDisastersByCountryName() {

        try (Session session = sessionFactory.openSession()) {

            System.out.println("Enter the country name to view disasters that have happened there: ");
            String countryName = scanner.nextLine();
            String displayString = countryName.substring(0, 1).toUpperCase() + countryName.substring(1).toLowerCase();

            List<WeatherOpenweather> openweatherDisasters = session.createQuery("FROM WeatherOpenweather WHERE countryName = :countryName", WeatherOpenweather.class)
                    .setParameter("countryName", countryName)
                    .getResultList();

            List<WeatherAccuweather> accuweatherDisasters = session.createQuery("FROM WeatherAccuweather WHERE countryName = :countryName", WeatherAccuweather.class)
                    .setParameter("countryName", countryName)
                    .getResultList();

            List<WeatherWeatherstack> weatherstackDisasters = session.createQuery("FROM WeatherWeatherstack WHERE countryName = :countryName", WeatherWeatherstack.class)
                    .setParameter("countryName", countryName)
                    .getResultList();

            displayDisasters(displayString, openweatherDisasters, accuweatherDisasters, weatherstackDisasters);

        } catch (Exception e) {
            System.out.println("Failed to fetch disaster data by city name. Error: " + e.getMessage());
        }
    }

    private static void exitProgram() {
        System.out.println("Goodbye!");
        System.exit(0);
    }

    private static void printFetchedData(DataEntity entity) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(entity.getDate());

        String latitudeDirection = entity.getLatitude() >= 0 ? "N" : "S";
        String longitudeDirection = entity.getLongitude() >= 0 ? "E" : "W";

        System.out.println("Event: " + entity.getNaturalDisaster() + " (" + entity.getDescription() + ")"
                + "\n\tLocation: " + entity.getCityName() + ", " + entity.getCountryName()
                + " lat/lon: " + Math.abs(entity.getLatitude()) + "° " + latitudeDirection + ", " +
                Math.abs(entity.getLongitude()) + "° " + longitudeDirection
                + "\n\tDate: " + formattedDate
                + "\n\t\tTemperature: " + entity.getTemperature() + "°C"
                + "\n\t\tPressure: " + entity.getPressure()
                + "\n\t\tHumidity: " + entity.getHumidity()
                + "\n\t\tWind: Speed: " + entity.getWindSpeed() + ", Direction: " + entity.getWindDirection()
                + "\n");
    }

    private static void displayDisasters(String displayString, List<WeatherOpenweather> openweatherDisasters, List<WeatherAccuweather> accuweatherDisasters, List<WeatherWeatherstack> weatherstackDisasters) {
        System.out.println(displayString + " cases from Openweather\n");
        for (WeatherOpenweather disaster : openweatherDisasters) {
            printFetchedData(disaster);
        }

        System.out.println(displayString + " cases from Accuweather\n");
        for (WeatherAccuweather disaster : accuweatherDisasters) {
            printFetchedData(disaster);
        }

        System.out.println(displayString + " cases from Weatherstack\n");
        for (WeatherWeatherstack disaster : weatherstackDisasters) {
            printFetchedData(disaster);
        }
    }
}
