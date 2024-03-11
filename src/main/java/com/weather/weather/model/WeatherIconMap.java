package com.weather.weather.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
@Data
public class WeatherIconMap {
    private final Map<String, String> weatherMap = new HashMap<>();

    public WeatherIconMap(){
        weatherMap.put("01d", "/img/clear-day.svg");
        weatherMap.put("01n", "/img/clear-night.svg");
        weatherMap.put("02d", "/img/partly-cloudy-day.svg");
        weatherMap.put("02n", "/img/partly-cloudy-night.svg");
        weatherMap.put("03d","/img/cloudy.svg");
        weatherMap.put("03n","/img/cloudy.svg");
        weatherMap.put("04d","/img/overcast.svg");
        weatherMap.put("04n","/img/overcast.svg");
        weatherMap.put("09d","/img/heavy-showers.svg");
        weatherMap.put("09n","/img/heavy-showers.svg");
        weatherMap.put("10d","/img/showers.svg");
        weatherMap.put("10n","/img/showers.svg");
        weatherMap.put("11d","/img/thunderstorm-showers.svg");
        weatherMap.put("11n","/img/thunderstorm-showers.svg");
        weatherMap.put("13d","/img/snow.svg");
        weatherMap.put("13n","/img/snow.svg");
        weatherMap.put("50d","/img/fog.svg");
        weatherMap.put("50n","/img/fog.svg");
    }
    public String getIconForWeather(String weatherState) {
        return weatherMap.getOrDefault(weatherState, "default.png");
    }
}

