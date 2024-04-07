package com.weather.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Метрики климата.
 */
@Data
@Builder
public class ClimateMetrics {
  @JsonProperty("temp")
  private double temperature;

  @JsonProperty("feels_like")
  private double feelingTemperature;

  @JsonProperty("pressure")
  private int pressure;

  @JsonProperty("humidity")
  private int humidity;

  /**
   * Конвертировать температуру в метрическую систему.
   */
  public void metricConvert() {
    temperature -= 273.15;
  }
}