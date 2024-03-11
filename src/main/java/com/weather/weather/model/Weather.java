package com.weather.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Weather {
    @JsonProperty("main")
    private String weatherStatus;
    @JsonProperty("icon")
    private String icon;
}
