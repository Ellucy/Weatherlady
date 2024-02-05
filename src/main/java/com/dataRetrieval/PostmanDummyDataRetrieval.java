package com.dataRetrieval;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PostmanDummyDataRetrieval {
    public static void main(String[] args) {

        String postmanUrl = "http://dataservice.accuweather.com/locations/v1/cities/search?apikey=Kglm3glts0z5lc4mPvj961hA7C07uq8A&q=tartu";

        try {
            // Create an HTTP client
            HttpClient httpClient = HttpClient.newHttpClient();

            // Define the request URL
            URI uri = URI.create(postmanUrl);

            // Create an HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            // Send the request and receive the response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Print the response body (dummy data)
            System.out.println("Response Body:\n" + response.body());
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }
}
