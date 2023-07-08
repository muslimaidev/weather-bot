package org.bot;


import org.bot.models.model1.WeatherData;
import org.bot.models.model2.WeatherForecast;
import org.bot.models.model2.WeatherForecastData;
import org.bot.service.IconService;
import org.bot.service.IconServiceImp;
import org.bot.service.WeatherApiClient;

import java.text.DecimalFormat;

public class BotTools {
    // get Weather API
    String myApiKey = "18573ab75a7ae2e50e88d7e488faa4f1";
    WeatherApiClient weatherApiClient = new WeatherApiClient(myApiKey);
    IconService iconService = new IconServiceImp();

    // get weather lat and long
    public String getWeatherInfo(double latitude, double longitude) {
        // Call weather API with latitude and longitude to fetch weather information
        // Implement your weather API logic here using API_KEY
        // Return the weather information as a string
//        return "Weather information for latitude: " + latitude + ", longitude: " + longitude;
        // Format latitude and longitude values
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        double formattedLatitude = Double.parseDouble(decimalFormat.format(latitude));
        double formattedLongitude = Double.parseDouble(decimalFormat.format(longitude));
        WeatherForecastData weatherData = weatherApiClient.searchByLongAndLatt(formattedLatitude, formattedLongitude);
        if (weatherData != null && weatherData.getCod().equals("200")) {
            WeatherForecast forecast = weatherData.getList().get(0);
            String weatherDescription = forecast.getWeather().get(0).getDescription();
            double temperature = forecast.getMain().getTemp();
            int humidity = forecast.getMain().getHumidity();
            int pressure = forecast.getMain().getPressure();
            String city = weatherData.getCity().getName();
            String date = weatherData.getList().get(weatherData.getList().size() - 1).getDt_txt();
            String iconCode = forecast.getWeather().get(0).getIcon();


            return buildTextFromData(city, iconCode, weatherDescription, temperature, humidity, pressure)
                    + "\n"
                    + "\uD83D\uDCC5 Date: " + date;
        }
        return "Failed to fetch weather information for latitude: " + latitude
                + ", longitude: " + longitude;

    }

    // get weather with city
    public String getWeatherInfoByCity(String city) {
        // Call weather API with city name to fetch weather information
        WeatherData weatherData = weatherApiClient.searchByCityName(city);

        if (weatherData != null && weatherData.getCod().equals("200")) {
            String weatherDescription = weatherData.getWeather().get(0).getDescription();
            double temperature = weatherData.getMain().getTemp();
            int humidity = weatherData.getMain().getHumidity();
            int pressure = weatherData.getMain().getPressure();
            String iconCode = weatherData.getWeather().get(0).getIcon();

            System.out.println(iconCode);
            return buildTextFromData(city, iconCode, weatherDescription, temperature, humidity, pressure);
        }
        return "Failed to fetch weather information for " + city;
    }

    public String buildTextFromData(String city, String iconCode, String weatherDescription,
                                    double temperature, int humidity, int pressure){
        // weather information string with emojis
        System.out.println(iconService.getEmoji(iconCode));
        return "Weather information for " + String.valueOf(city.charAt(0)).toUpperCase()
                + city.substring(1).toLowerCase() + ":\n"
                + iconService.getEmoji(iconCode) + " Description: " + weatherDescription + "\n"
                + "\uD83C\uDF21 Temperature: " + Math.round(temperature) + "°C\n"
                + "\uD83D\uDCA7 Humidity: " + humidity + "%\n"
                + "\u23F0 Pressure: " + pressure + " hPa";
    }
}
