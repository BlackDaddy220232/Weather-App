package com.weather.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherForecast {
  private ArrayList<Weather> weather;
  private Wind wind;
  private Coordinates coordinates;
  private ClimateMetrics main;
  private int visibility;
  private String city;
  @JsonProperty("daily")
  private ArrayList<WeatherForecast> daily;
  @JsonProperty("sys")
  private Time time = new Time();

  private int timezone;

  public void convert() {
    main.metricConvert();
  }

  public String getWeatherIcon() {
    return weather.get(0).getIcon();
  }

  public void convertTime() {
    time.timeConvert(timezone);
  }
}
