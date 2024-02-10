package com.weather;

import com.entities.WeatherOpenweather;
import com.retrievedata.OpenweatherDataRetrieval;
import com.retrievedata.OpenweatherDatabaseConnection;

import java.io.IOException;
import java.util.Scanner;

public class WeatherApplication {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a city name");
        String cityName = scanner.nextLine();

        try  {

            while (true) {
                displayMenu();
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        addLocation(cityName);
                        break;
                    case 2:
                        displayLocations();
                        break;
                    case 3:
                        downloadWeatherValues();
                        break;
                    case 4:
                        exitProgram();
                        break;
                    default:
                        System.out.println("Please enter a correct number.");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void displayMenu() {
        System.out.println("Enter your choice (1/2/3/4): ");
    }

    private static void downloadWeatherValues() {
        System.out.println("Downloading weather values");
    }

    private static void exitProgram() {
        System.out.println("Goodbye!");
        System.exit(0);
    }

    private static void displayLocations() {
        System.out.println("Displaying locations");
    }

    private static void addLocation(String cityName) throws IOException {
        OpenweatherDataRetrieval.getJsonData(cityName);
        OpenweatherDatabaseConnection.insertOpenweatherData(new WeatherOpenweather());

    }

}
