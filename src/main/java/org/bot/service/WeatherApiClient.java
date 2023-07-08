package org.bot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bot.models.model1.*;
import org.bot.models.model2.WeatherForecastData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherApiClient {
//    private static final String API_KEY = "18573ab75a7ae2e50e88d7e488faa4f1";
    private final String API_KEY;

    public WeatherApiClient(String apiKey) {
        this.API_KEY = apiKey;
    }

    public WeatherData searchByCityName(String cityName) {
        cityName = String.valueOf(cityName.charAt(0)).toUpperCase()
                + cityName.substring(1).toLowerCase();
        WeatherData weatherData = new WeatherData();
        try {
            // API request
            String url = "https://api.openweathermap.org/data/2.5/weather" +
                    "?q=" + cityName +
                    "&appid=" + API_KEY;

            URL apiUrl = new URL(url);

            // Open conn
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            //request
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();
//            weatherData = new WeatherData();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

//                System.out.println(response.toString());

                // Weather data
                // Parse the JSON response
                ObjectMapper objectMapper = new ObjectMapper();
                weatherData = objectMapper.readValue(response.toString(), WeatherData.class);

                return weatherData;
            } else {
                System.out.println("API request failed with response code: " + responseCode);
            }

            // Close conn
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public WeatherForecastData searchByLongAndLatt(double longitude, double latitude) {
//        latitude = 51.5074;
//        longitude = -0.1278;

        WeatherForecastData weatherForecastData = new WeatherForecastData();
        System.out.println("longitude: " + longitude + " latitude: " + latitude);
        try {
            // URL for search lat and lon API request
            String url = "https://api.openweathermap.org/data/2.5/forecast" +
                    "?lat=" + latitude +
                    "&lon=" + longitude +
                    "&cnt=1&appid=" + API_KEY;

            URL apiUrl = new URL(url);

            // Open conn
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            // GET method
            connection.setRequestMethod("GET");
//            weatherForecastData = new WeatherForecastData();

            // Get response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Process the response
//                System.out.println(response.toString());
                ObjectMapper objectMapper = new ObjectMapper();
                weatherForecastData = objectMapper.readValue(response.toString(), WeatherForecastData.class);

                return weatherForecastData;
            } else {
                System.out.println("API request failed with response code: " + responseCode);
            }

            // Close conn
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

