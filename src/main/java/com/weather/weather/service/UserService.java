package com.weather.weather.service;

import com.weather.weather.dao.CityRepository;
import com.weather.weather.dao.UserRepository;
import com.weather.weather.exception.CityNotFoundException;
import com.weather.weather.model.entity.City;
import com.weather.weather.model.entity.User;
import com.weather.weather.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
@CacheConfig(cacheNames = "dataUser")
public class UserService implements UserDetailsService {
  private final CityRepository cityRepository;
  private final UserRepository userRepository;
  private final CacheManager cacheManager;
  private static final String USER_NOT_FOUND_MESSAGE = "Пользователя с именем \"%s\" не существует";
  private static final String CITY_NOT_FOUND_MESSAGE = "Города \"%s\" не существует";

  @Autowired
  public UserService(
      CityRepository cityRepository, UserRepository userRepository, CacheManager cacheManager) {
    this.cityRepository = cityRepository;
    this.userRepository = userRepository;
    this.cacheManager = cacheManager;
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
    userRepository.delete(user);
  }

  public String addCityToUser(String cityName, String username) {
    User user =
        userRepository
            .findUserByUsername(username)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username)));
    City city =
        cityRepository
            .findCitiesByCityName(cityName)
            .orElseGet(
                () -> {
                  City newCity = new City();
                  newCity.setCityName(cityName);
                  return cityRepository.save(newCity);
                });

      Set<City> savedCities = user.getSavedCities();
      if (savedCities == null) {
        savedCities = new HashSet<>();
      } else {
        if (savedCities.contains(city)) {
          return String.format("Город %s был добавлен пользователем ранее", cityName);
        }
      }
      savedCities.add(city);
      user.setSavedCities(savedCities);
      userRepository.save(user);
    return String.format("Город %s был успешно добавлен", cityName);
  }

  public Set<City> getSavedCitiesByToken(String username) {
    User user =
        userRepository
            .findUserByUsername(username)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username)));
    return user.getSavedCities();
  }

  public void deleteCity(String username, String cityName) {
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
                () -> new CityNotFoundException(String.format(CITY_NOT_FOUND_MESSAGE, cityName)));

      user.getSavedCities().removeIf(savedCity -> savedCity.getCityName().equals(cityName));
      userRepository.save(user);
      saveUserToCache(user);
      city.getSavedUsers().removeIf(savedUser -> savedUser.getUsername().equals(username));
      cityRepository.save(city);
      if (city.getSavedUsers().isEmpty()) {
        cityRepository.delete(city);
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

  @Cacheable
  public User getUserByUsername(String username) {
    return userRepository
        .findUserByUsername(username)
        .orElseThrow(
            () -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username)));
  }

  public List<User> findUsersByCity(String cityName) {
    return userRepository
        .findUsersByCity(cityName)
        .filter(users -> !users.isEmpty())
        .orElseThrow(
            () -> new CityNotFoundException(String.format(CITY_NOT_FOUND_MESSAGE, cityName)));
  }

  @CachePut
  public void saveUserToCache(User user) {
    Cache cache = cacheManager.getCache("dataUser");
    if (cache != null) {
      cache.put(user.getUsername(), user);
    }
  }
}
