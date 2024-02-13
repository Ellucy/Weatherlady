package com.handlers;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

public class APIConnection {

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
}

