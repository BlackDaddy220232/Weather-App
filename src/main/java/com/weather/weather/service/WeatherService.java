package com.weather.weather.service;

import com.weather.weather.model.WeatherForecast;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

  @Value("${openweather.api.key}")
  private String apiKey;

  @Value("${api.url}")
  private String apiUrl;

  private final RestTemplate restTemplate = new RestTemplate();

  public WeatherForecast getWeatherByCity(String city) {
    String url = apiUrl + "?q=" + city + "&exclude=current,daily" + "&appid=" + apiKey;
    return restTemplate.getForObject(url, WeatherForecast.class);
  }
}
