package com.weather.weather.controller;

import com.weather.weather.model.WeatherForecast;
import com.weather.weather.service.WeatherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RequestMapping("api/v1")
@RestController
public class WeatherController {
  String icon;
  private final WeatherService weatherService;

  public WeatherController(WeatherService weatherService) {
    this.weatherService = weatherService;
  }

  @ExceptionHandler(HttpClientErrorException.NotFound.class)
  public ResponseEntity<String> handleNotFoundException() {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("City not found");
  }

  @GetMapping(value = "/weather")
  public ResponseEntity<WeatherForecast> getCurrentWeather(@RequestParam String cityname, Model model) {
    WeatherForecast weatherForecast = weatherService.getWeatherByCity(cityname);
    weatherForecast.convert();
    weatherForecast.convertTime();
    weatherForecast.setCity(cityname);
    return ResponseEntity.ok(weatherForecast);
  }
}
