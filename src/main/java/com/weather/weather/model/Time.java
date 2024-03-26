package com.weather.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Time {
  @JsonProperty("sunrise")
  private long sunriseTime;

  @JsonProperty("sunset")
  private long sunsetTime;

  private String convertedSunsetTime;
  private String convertedSunriseTime;

  public void timeConvert(int timezone) {
    Instant instantSunrise = Instant.ofEpochSecond(sunriseTime + timezone);
    LocalDateTime utcSunriseTime = LocalDateTime.ofInstant(instantSunrise, ZoneId.of("UTC"));
    convertedSunriseTime = utcSunriseTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    Instant instantSunset = Instant.ofEpochSecond(sunsetTime + timezone);
    LocalDateTime utcSunsetTime = LocalDateTime.ofInstant(instantSunset, ZoneId.of("UTC"));
    convertedSunsetTime = utcSunsetTime.format(DateTimeFormatter.ofPattern("HH:mm"));
  }
}
