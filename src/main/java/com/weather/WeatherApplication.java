package com.weather;

import com.retrievedata.AccuweatherDataRetrieval;
import com.retrievedata.OpenweatherDataRetrieval;
import com.retrievedata.WeatherstackDataRetrieval;

import java.io.IOException;
import java.util.Scanner;

public class WeatherApplication {

    public static Scanner scanner = new Scanner(System.in);

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

        String awApiKey = System.getenv("AW_API_KEY");
        String wsApiKey = System.getenv("WS_API_KEY");
        String owApiKey = System.getenv("OW_API_KEY");

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
    }

    private static void viewDisastersByCityName() {
        System.out.println("Displaying disasters from three different db tables");
    }

    private static void exitProgram() {
        System.out.println("Goodbye!");
        System.exit(0);
    }
}
