package com.weather;

import com.entities.DataEntity;
import com.entities.WeatherAccuweather;
import com.entities.WeatherOpenweather;
import com.entities.WeatherWeatherstack;
import com.handlers.DateConverter;
import com.handlers.WeatherApplicationController;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class WeatherApplication {

    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        try {

            while (true) {

                displayMenu();
                String userInput = scanner.nextLine();
                if (!userInput.matches("\\d+")) {
                    System.out.println("Please enter a correct number. (1/2/3/4/5/6): ");
                    continue;
                }

                switch (userInput) {
                    case "1" -> addNewNaturalDisaster();
                    case "2" -> viewDisastersByDate();
                    case "3" -> viewDisastersByName();
                    case "4" -> viewDisastersByCityName();
                    case "5" -> viewDisastersByCountryName();
                    case "6" -> exitProgram();
                    default -> System.out.println("Please enter a correct number: ");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void displayMenu() {

        System.out.print("""
            Enter your choice:
                1. Add new natural disaster
                2. View disasters by date
                3. View disasters by disaster name
                4. View disasters by city name
                5. View disasters by country name
                6. Exit program
            Choice:\t""");

    }

    private static void addNewNaturalDisaster() throws IOException {

        System.out.println("Enter a city where natural disaster occurred: ");
        String cityName = scanner.nextLine();

        System.out.println("Enter the name of natural disaster that occurred: ");
        String disaster = scanner.nextLine();

        System.out.println("Enter description to the disaster: ");
        String description = scanner.nextLine();

        WeatherApplicationController.addingNaturalDisaster(cityName, disaster, description);
    }


    private static void viewDisastersByDate() {

        System.out.println("Enter the date (yyyy-mm-dd) to view disasters that have happened there: ");
        String inputDate = scanner.nextLine();
        Timestamp date = DateConverter.convertStringToTimestamp(inputDate);
        String displayString = inputDate.substring(0, 1).toUpperCase() + inputDate.substring(1).toLowerCase();
        List<WeatherOpenweather> openweatherDisasters = new ArrayList<>();
        List<WeatherAccuweather> accuweatherDisasters = new ArrayList<>();
        List<WeatherWeatherstack> weatherstackDisasters = new ArrayList<>();
        boolean isExtracted =  WeatherApplicationController.getDisastersByDate(openweatherDisasters, date, accuweatherDisasters, weatherstackDisasters);

        if(isExtracted) {
            displayDisasters(displayString, openweatherDisasters, accuweatherDisasters, weatherstackDisasters);
            System.out.println("Weather data  fetched successfully!");
        } else {
            System.out.println("Failed to fetch disaster data by disaster name");
        }
    }

    private static void viewDisastersByName() {
        System.out.println("Please enter the name of the natural disaster you want to check: ");
        String disasterName = scanner.nextLine();
        List<WeatherOpenweather> openweatherDisasters = new ArrayList<>();
        List<WeatherAccuweather> accuweatherDisasters = new ArrayList<>();
        List<WeatherWeatherstack> weatherstackDisasters = new ArrayList<>();

        boolean isExtracted =  WeatherApplicationController.getDisastersByName(openweatherDisasters,
                disasterName, accuweatherDisasters, weatherstackDisasters);

        if (isExtracted) {
            String displayString = disasterName.substring(0, 1).toUpperCase() + disasterName.substring(1).toLowerCase();
            displayDisasters(displayString, openweatherDisasters, accuweatherDisasters, weatherstackDisasters);
            System.out.println("Weather data fetched successfully!");
        } else {
            System.out.println("Failed to fetch disaster data by disaster name");
        }

    }

    private static void viewDisastersByCityName() {

        System.out.println("Enter the city name to view disasters that have happened there: ");
        String cityName = scanner.nextLine();

        List<WeatherOpenweather> openweatherDisasters = new ArrayList<>();
        List<WeatherAccuweather> accuweatherDisasters = new ArrayList<>();
        List<WeatherWeatherstack> weatherstackDisasters = new ArrayList<>();

        boolean isExtracted =  WeatherApplicationController.getDisastersByCityName(openweatherDisasters,
                cityName, accuweatherDisasters, weatherstackDisasters);

        if (isExtracted) {
            String displayString = cityName.substring(0, 1).toUpperCase() + cityName.substring(1).toLowerCase();
            displayDisasters(displayString, openweatherDisasters, accuweatherDisasters, weatherstackDisasters);
            System.out.println("Weather data fetched successfully!");
        } else {
            System.out.println("Failed to fetch disaster data by city name");
        }
    }

    private static void viewDisastersByCountryName() {
        System.out.println("Enter the country name to view disasters that have happened there: ");
        String countryName = scanner.nextLine();

        List<WeatherOpenweather> openweatherDisasters = new ArrayList<>();
        List<WeatherAccuweather> accuweatherDisasters = new ArrayList<>();
        List<WeatherWeatherstack> weatherstackDisasters = new ArrayList<>();

        String[] words = countryName.split("\\s+");
        StringBuilder displayStringBuilder = new StringBuilder();

        boolean isExtracted =  WeatherApplicationController.getDisastersByCountryName(openweatherDisasters,
                countryName, accuweatherDisasters, weatherstackDisasters,words,displayStringBuilder);
        if (isExtracted) {
            String displayString = displayStringBuilder.toString().trim();
            displayDisasters(displayString, openweatherDisasters, accuweatherDisasters, weatherstackDisasters);
            System.out.println("Weather data fetched successfully!");
        } else {
            System.out.println("Failed to fetch disaster data by country name");
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

    private static void displayDisasters(String displayString, List<WeatherOpenweather> openweatherDisasters,
                                         List<WeatherAccuweather> accuweatherDisasters,
                                         List<WeatherWeatherstack> weatherstackDisasters) {
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