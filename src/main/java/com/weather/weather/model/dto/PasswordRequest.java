package com.weather.weather.model.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PasswordRequest {
  private String password;

}
