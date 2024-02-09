package com.dataretrieval;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//public class OpenWeatherDataToDatabase {

//
//    public static void insertWeatherData(float longitude, float latitude, String city, float temperature, float humidity, float windSpeed) throws SQLException {
//        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
//            String sql = "INSERT INTO weather (longitude, latitude,city, temperature, humidity, windSpeed) VALUES (?,?,?, ?, ?, ?)";
//            try (PreparedStatement statement = conn.prepareStatement(sql)) {
//                statement.setFloat(1, longitude);
//                statement.setFloat(2, latitude);
//                statement.setString(3, city);
//                statement.setFloat(4, temperature);
//                statement.setFloat(5, humidity);
//                statement.setFloat(6, windSpeed);
//                statement.executeUpdate();
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//}