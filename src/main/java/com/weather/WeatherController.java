package com.weather;

import com.entities.WeatherAccuweather;
import com.entities.WeatherOpenweather;
import com.entities.WeatherWeatherstack;
import com.retrievedata.AccuweatherDataRetrieval;
import com.retrievedata.OpenweatherDataRetrieval;
import com.retrievedata.WeatherData;
import com.retrievedata.WeatherstackDataRetrieval;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherController {
    private static final String awApiKey = System.getenv("AW_API_KEY");
    private static final String wsApiKey = System.getenv("WS_API_KEY");
    private static final String owApiKey = System.getenv("OW_API_KEY");

    protected static String menu = """
            Enter your choice:
                1. Add new natural disaster
                2. View disasters by date
                3. View disasters by disaster name
                4. View disasters by city name
                5. View disasters by country name
                6. Exit program
            Choice:\t""";

    private static SessionFactory sessionFactory = getSessionFactory();

    public static SessionFactory getSessionFactory() {

        sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(WeatherOpenweather.class)
                .addAnnotatedClass(WeatherAccuweather.class)
                .addAnnotatedClass(WeatherWeatherstack.class)
                .buildSessionFactory();
        return sessionFactory;
    }

    public static JSONObject downloadWeatherData(String url) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet getRequest = new HttpGet(url);
        HttpResponse response = httpClient.execute(getRequest);

        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            return new JSONObject(result);
        } else {
            System.err.println("Failed to retrieve data. HTTP Error Code: " + response.getStatusLine().getStatusCode());
            return null;
        }
    }

    public static <T extends WeatherData> void saveWeatherData(T weatherData) {

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            T managedWeatherData = session.merge(weatherData);
            session.merge(managedWeatherData);
            transaction.commit();
            System.out.println("Weather data saved successfully!");
        } catch (Exception e) {
            System.out.println("Failed to save weather data. Error: " + e.getMessage());
        }
    }

    protected static void addingNaturalDisaster(String cityName, String disaster, String description) {
        try {
            OpenweatherDataRetrieval.downloadAndSetWeatherData(cityName, disaster, description, owApiKey);
            AccuweatherDataRetrieval.downloadAndSetWeatherData(cityName, disaster, description, awApiKey);
            WeatherstackDataRetrieval.downloadAndSetWeatherData(cityName, disaster, description, wsApiKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static boolean getDisastersByDate(List<WeatherOpenweather> openweatherDisasters, Timestamp date,
                                                List<WeatherAccuweather> accuweatherDisasters,
                                                List<WeatherWeatherstack> weatherstackDisasters) {
        try (Session session = sessionFactory.openSession()) {

            openweatherDisasters.addAll(session.createQuery("FROM WeatherOpenweather WHERE date= :date ", WeatherOpenweather.class)
                    .setParameter("date", date)
                    .getResultList());
            accuweatherDisasters.addAll(session.createQuery("FROM WeatherAccuweather WHERE date= :date ", WeatherAccuweather.class)
                    .setParameter("date", date)
                    .getResultList());
            weatherstackDisasters.addAll(session.createQuery("FROM WeatherWeatherstack WHERE date= :date ", WeatherWeatherstack.class)
                    .setParameter("date", date)
                    .getResultList());
            return true;

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            return false;
        }
    }

    protected static Timestamp convertStringToTimestamp(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(dateString);
            return new Timestamp(parsedDate.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected static boolean getDisastersByName(List<WeatherOpenweather> openweatherDisasters, String disasterName,
                                                List<WeatherAccuweather> accuweatherDisasters,
                                                List<WeatherWeatherstack> weatherstackDisasters) {
        try (Session session = sessionFactory.openSession()) {

            openweatherDisasters.addAll(session.createQuery("FROM WeatherOpenweather WHERE naturalDisaster= :naturalDisaster", WeatherOpenweather.class)
                    .setParameter("naturalDisaster", disasterName)
                    .getResultList());
            accuweatherDisasters.addAll(session.createQuery("FROM WeatherAccuweather WHERE naturalDisaster= :naturalDisaster", WeatherAccuweather.class)
                    .setParameter("naturalDisaster", disasterName)
                    .getResultList());
            weatherstackDisasters.addAll(session.createQuery("FROM WeatherWeatherstack WHERE naturalDisaster= :naturalDisaster", WeatherWeatherstack.class)
                    .setParameter("naturalDisaster", disasterName)
                    .getResultList());

            return true;

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            return false;
        }
    }

    protected static boolean getDisastersByCityName(List<WeatherOpenweather> openweatherDisasters, String cityName,
                                                    List<WeatherAccuweather> accuweatherDisasters,
                                                    List<WeatherWeatherstack> weatherstackDisasters) {
        try (Session session = sessionFactory.openSession()) {

            openweatherDisasters.addAll(session.createQuery("FROM WeatherOpenweather WHERE cityName = :cityName", WeatherOpenweather.class)
                    .setParameter("cityName", cityName)
                    .getResultList());

            accuweatherDisasters.addAll(session.createQuery("FROM WeatherAccuweather WHERE cityName = :cityName", WeatherAccuweather.class)
                    .setParameter("cityName", cityName)
                    .getResultList());

            weatherstackDisasters.addAll(session.createQuery("FROM WeatherWeatherstack WHERE cityName = :cityName", WeatherWeatherstack.class)
                    .setParameter("cityName", cityName)
                    .getResultList());

            return true;
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            return false;
        }
    }

    protected static boolean getDisastersByCountryName(List<WeatherOpenweather> openweatherDisasters, String countryName,
                                                       List<WeatherAccuweather> accuweatherDisasters,
                                                       List<WeatherWeatherstack> weatherstackDisasters, String[] words, StringBuilder displayStringBuilder) {
        try (Session session = sessionFactory.openSession()) {
            for (String word : words) {
                if (!word.isEmpty()) {
                    String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
                    displayStringBuilder.append(capitalizedWord).append(" ");
                }
            }

            String countryCode = convertCountryNameToCode(countryName);
            System.out.println(countryCode);

            openweatherDisasters.addAll(session.createQuery("FROM WeatherOpenweather WHERE countryName = :countryCode", WeatherOpenweather.class)
                    .setParameter("countryCode", countryCode)
                    .getResultList());

            accuweatherDisasters.addAll(session.createQuery("FROM WeatherAccuweather WHERE countryName = :countryName", WeatherAccuweather.class)
                    .setParameter("countryName", countryName)
                    .getResultList());

            weatherstackDisasters.addAll(session.createQuery("FROM WeatherWeatherstack WHERE countryName = :countryName", WeatherWeatherstack.class)
                    .setParameter("countryName", countryName)
                    .getResultList());
            return true;

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            return false;
        }
    }

    public static String convertCountryNameToCode(String countryName) {

        if (countryName == null || countryName.isEmpty()) {
            return null;
        }

        Locale[] locales = Locale.getAvailableLocales();

        for (Locale locale : locales) {
            String displayName = locale.getDisplayCountry();
            String countryCode = locale.getCountry();

            if (countryNameEquals(countryName, displayName)) {
                return countryCode;
            }
        }
        return null;
    }

    private static boolean countryNameEquals(String inputName, String displayName) {

        if (inputName == null) {
            return false;
        }

        String[] input = inputName.split(" ");

        StringBuilder normalizedInput = new StringBuilder();
        for (String word : input) {
            if (!word.isEmpty()) {
                if (!normalizedInput.isEmpty()) {
                    normalizedInput.append(" ");
                }
                normalizedInput.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase());
            }
        }
        return normalizedInput.toString().equalsIgnoreCase(displayName);
    }
}
