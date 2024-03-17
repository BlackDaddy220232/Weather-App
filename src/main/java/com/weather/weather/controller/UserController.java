package com.weather.weather.controller;

import com.weather.weather.model.entity.City;
import com.weather.weather.model.entity.User;
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
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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
    public ResponseEntity<String> addPlayerToUser(
            @RequestParam String city,
            @RequestHeader("Authorization") String authorizationHeader) {

        String token;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        userService.addCityToUser(city, token);
        return ResponseEntity.ok("Player was successfully added");
    }
    @GetMapping("/getAllCities")
    public ResponseEntity<Set<City>> getAllPlayersByUserName(@RequestHeader("Authorization") String authorizationHeader) {
        String token;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(userService.getSavedCitiesByToken(token));
    }
    @DeleteMapping("/deleteCity")
    public ResponseEntity<String> deleteCity(
            @RequestParam String city,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userService.deleteCity(token,city);
        return ResponseEntity.ok("City was successfully delete");
    }
}
