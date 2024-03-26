package com.weather.weather.model.dto;

import lombok.Data;

@Data
public class SignUpRequest {
  private String username;
  private String password;
  private String country;
}
