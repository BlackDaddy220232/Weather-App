package com.weather.weather.controller;

import com.weather.weather.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/countries")
public class CountryController {
  private CountryService countryService;

  @Autowired
  public void setCountryService(CountryService countryService) {
    this.countryService = countryService;
  }

  @GetMapping("/getCountryUsers")
  public ResponseEntity<Object> getCountryUsers(@RequestParam String countryName) {
    try {
      return ResponseEntity.ok().body(countryService.getCountryUsers(countryName));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/getCountries")
  public ResponseEntity<Object> getCountries() {
    try {
      return ResponseEntity.ok().body(countryService.getCountries());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PatchMapping("/editCountryName")
  public ResponseEntity<Object> editCountryName(
      @RequestParam String countryName, String newCountryName) {
    try {
      countryService.editCountryName(countryName, newCountryName);
      return ResponseEntity.ok().body("Succes bitch");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/deleteCountry")
  public ResponseEntity<Object> deleteCountry(@RequestParam String countryName) {
    try {
      countryService.deleteCountry(countryName);
      return ResponseEntity.ok().body("Succes bitch");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
