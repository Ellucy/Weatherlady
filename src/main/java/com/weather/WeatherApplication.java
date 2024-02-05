package com.weather;

import java.util.Scanner;

public class WeatherApplication {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        while (true) {

            displayMenu();

            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> addLocation();
                case 2 -> displayLocations();
                case 3 -> downloadWeatherValues();
                case 4 -> exitProgram();
                default -> System.out.println("Please enter a correct number.");
            }
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

    private static void addLocation() {
        System.out.println("Adding location");
    }

}
