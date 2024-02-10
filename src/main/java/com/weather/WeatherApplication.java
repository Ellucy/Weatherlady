package com.weather;

import com.entities.WeatherOpenweather;
import com.retrievedata.AccuweatherDataRetrieval;
import com.retrievedata.OpenweatherDataRetrieval;
import com.retrievedata.OpenweatherDatabaseConnection;

import java.io.IOException;
import java.util.Scanner;

public class WeatherApplication {

    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        try {

            while (true) {

                displayMenu();
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        addNewNaturalDisaster();
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

    private static void addNewNaturalDisaster() throws IOException {

        String awApiKey = System.getenv("AW_API_KEY");

        System.out.println("Enter a city where natural disaster occurred: ");
        String cityName = scanner.nextLine();

        System.out.println("Enter the name of natural disaster that occurred: ");
        String disaster = scanner.nextLine();

        System.out.println("Enter description to the disaster: ");
        String description = scanner.nextLine();

        try {
//            OpenweatherDataRetrieval.getJsonData(cityName);
//            OpenweatherDatabaseConnection.insertOpenweatherData(new WeatherOpenweather());

            AccuweatherDataRetrieval.downloadAndSetWeatherData(cityName, disaster, description, awApiKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
