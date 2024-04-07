package com.weather.weather.service;

import com.weather.weather.component.UserCache;
import com.weather.weather.dao.CityRepository;
import com.weather.weather.dao.UserRepository;
import com.weather.weather.exception.CountryNotFoundException;
import com.weather.weather.model.entity.City;
import com.weather.weather.model.entity.User;
import com.weather.weather.security.JwtCore;
import com.weather.weather.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService implements UserDetailsService {
  private CityRepository cityRepository;
  private UserRepository userRepository;
  private UserCache userCache;
  private JwtCore jwtCore;
  private static final String USER_NOT_FOUND_MESSAGE = "Пользователя с именем \"%s\" не существует";
  private static final String CITY_NOT_FOUND_MESSAGE = "Города \"%s\" не существует";

  @Autowired
  public void setJwtCore(JwtCore jwtCore) {
    this.jwtCore = jwtCore;
  }

  @Autowired
  public void setUserCache(UserCache userCache) {
    this.userCache = userCache;
  }

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Autowired
  public void setCityRepository(CityRepository cityRepository) {
    this.cityRepository = cityRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user =
        userRepository
            .findUserByUsername(username)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username)));
    return UserDetailsImpl.build(user);
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public void deleteUser(String username) {
    Optional<User> userOptional = userRepository.findUserByUsername(username);
    User user =
        userOptional.orElseThrow(
            () -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username)));
    deleteUser(user);
  }

  public void addCityToUser(String cityName, String token) {
    String username = jwtCore.getNameFromJwt(token);
    User user =
        userRepository
            .findUserByUsername(username)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username)));
    City city =
        cityRepository
            .findCitiesByCityName(cityName)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username)));
    if (city == null) {
      city = new City();
      city.setCityName(cityName);
      cityRepository.save(city);
    }
    if (user != null && city != null) {
      Set<City> savedCities = user.getSavedCities();
      if (savedCities == null) {
        savedCities = new HashSet<>();
      }
      savedCities.add(city);
      user.setSavedCities(savedCities);

      saveUser(user);
    }
  }

  public Set<City> getSavedCitiesByToken(String token) {
    String username = jwtCore.getNameFromJwt(token);
    User user =
        userRepository
            .findUserByUsername(username)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username)));
    return user.getSavedCities();
  }

  public void deleteCity(String token, String cityName) {
    String username = jwtCore.getNameFromJwt(token);
    User user =
        userRepository
            .findUserByUsername(username)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username)));
    City city =
        cityRepository
            .findCitiesByCityName(cityName)
            .orElseThrow(
                () ->
                    new CountryNotFoundException(String.format(CITY_NOT_FOUND_MESSAGE, cityName)));

    if (user != null && city != null) {
      user.getSavedCities().removeIf(savedCity -> savedCity.getCityName().equals(cityName));
      userRepository.save(user);

      city.getSavedUsers().removeIf(savedUser -> savedUser.getUsername().equals(username));
      cityRepository.save(city);

      if (city.getSavedUsers().isEmpty()) {
        cityRepository.delete(city);
      }
    }
  }

  public String getTokenFromRequest(String authorizationHeader) {
    String token;
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      token = authorizationHeader.substring(7);
    } else {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    return token;
  }

  public User getUserByUsername(String username) {
    User user = (User) userCache.getFromCache(username);
    if (user != null) {
      return user;
    }
    user =
        userRepository
            .findUserByUsername(username)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username)));
    userCache.addToCache(username, user);
    return user;
  }

  public void saveUser(User user) {
    userRepository.save(user);
    userCache.addToCache(user.getUsername(), user);
  }

  public void deleteUser(User user) {
    userRepository.delete(user);
    userCache.removeFromCache(user.getUsername());
  }

  public List<User> findUsersByCity(String cityName) {
    return userRepository.findUsersByCity(cityName);
  }
}
