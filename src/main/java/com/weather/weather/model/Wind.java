package com.weather.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Wind {
  @JsonProperty("speed")
  private double speed;

  @JsonProperty("deg")
  private int degree;
}
