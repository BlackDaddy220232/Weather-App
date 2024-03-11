package com.weather.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Coordinates {
    @JsonProperty("lon")
    private int lon;
    @JsonProperty("lat")
    private int lat;
}
