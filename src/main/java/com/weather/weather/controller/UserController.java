package com.weather.weather.controller;

import com.weather.weather.model.entity.City;
import com.weather.weather.model.entity.User;
import com.weather.weather.security.JwtCore;
import com.weather.weather.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {
  private final UserService userService;
  private JwtCore jwtCore;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @Autowired
  public void setJwtCore(JwtCore jwtCore) {
    this.jwtCore = jwtCore;
  }

  @GetMapping("/getAllUsers")
  public ResponseEntity<List<User>> getAllUsers() {
    return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
  }

  @DeleteMapping("/deleteUser")
  public ResponseEntity<String> deleteUser(@RequestParam String username) {
    userService.deleteUser(username);
    return ResponseEntity.ok("User was successfully deleted");
  }

  @PostMapping("/addCity")
  public ResponseEntity<String> addCityToUser(
      @RequestParam String city, @RequestHeader("Authorization") String authorizationHeader) {

    String token = userService.getTokenFromRequest(authorizationHeader);
    String username = jwtCore.getNameFromJwt(token);
    userService.addCityToUser(city, username);
    return ResponseEntity.ok("City was successfully added");
  }

  @GetMapping("/getAllCities")
  public ResponseEntity<Set<City>> getAllPlayersByUserName(
      @RequestHeader("Authorization") String authorizationHeader) {
    String token = userService.getTokenFromRequest(authorizationHeader);
    return ResponseEntity.ok(userService.getSavedCitiesByToken(token));
  }

  @GetMapping("/getUserByUsername")
  public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
    return ResponseEntity.ok(userService.getUserByUsername(username));
  }

  @DeleteMapping("/deleteCity")
  public ResponseEntity<String> deleteCity(
      @RequestParam String city, @RequestHeader("Authorization") String authorizationHeader) {
    String token = userService.getTokenFromRequest(authorizationHeader);
    String username = jwtCore.getNameFromJwt(token);
    userService.deleteCity(username, city);
    return ResponseEntity.ok("City was successfully deleted");
  }

  @GetMapping("/byCity")
  public ResponseEntity<List<User>> getUsersByCity(@RequestParam String cityName) {
    return ResponseEntity.ok().body(userService.findUsersByCity(cityName));
  }
}
