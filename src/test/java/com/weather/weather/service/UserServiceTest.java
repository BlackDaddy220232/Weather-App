package com.weather.weather.service;

import com.weather.weather.dao.CityRepository;
import com.weather.weather.dao.UserRepository;
import com.weather.weather.exception.CityNotFoundException;
import com.weather.weather.model.entity.City;
import com.weather.weather.model.entity.Country;
import com.weather.weather.model.entity.User;
import com.weather.weather.security.JwtCore;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  @InjectMocks private UserService userService;
  @Mock private CityRepository cityRepository;
  @Mock private UserRepository userRepository;
  @Mock private Cache cache;
  @Mock private CacheManager cacheManager;
  @Test
  void deleteUser() {
    User user = new User();
    String username = "User";
    when(userRepository.findUserByUsername(username)).thenReturn(java.util.Optional.of(user));
    userService.deleteUser(username);
    verify(userRepository, times(1)).delete(user);
  }

  @Test
  void testAddCityToUser() {
    String username = "testUser";
    String cityname = "testCity";

    User user = new User();
    user.setUsername(username);

    when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
    when(cityRepository.findCitiesByCityName(cityname)).thenReturn(Optional.empty());

    String result = userService.addCityToUser(cityname, username);

    assertEquals("City testCity was added", result);
  }
  @Test
  void testAddCityToUser_CityExists(){
    String username = "testUser";
    String cityname = "testCity";

    User user = new User();
    City city=new City();
    city.setCityName("testCity");
    Set<City> savedCities =new HashSet<>();
    savedCities.add(city);
    user.setSavedCities(savedCities);

    when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
    when(cityRepository.findCitiesByCityName(cityname)).thenReturn(Optional.of(city));
    String result=userService.addCityToUser(cityname,username);
    assertEquals("City testCity was added by user earlier",result);
  }

  @Test
  void testAddCityToUser_NoSuchUser(){
    String username="NonExistedTestUser";
    String cityname="testCity";
    when(userRepository.findUserByUsername(username)).thenReturn(Optional.empty());
    assertThrows(UsernameNotFoundException.class,()->userService.addCityToUser(cityname,username));
  }

  @Test
  void testLoadUserByUsername_ExistingUser() {
    String username = "testUser";
    User mockUser = new User();
    mockUser.setUsername(username);
    when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(mockUser));
    assertEquals(username, userService.loadUserByUsername(username).getUsername());
  }

  @Test
  void testLoadUserByUsername_UserNotFound() {
    String username = "nonExistentUser";
    when(userRepository.findUserByUsername(username)).thenReturn(Optional.empty());
    assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));
  }

  @Test
  void testGetUserByName_ExistingUser() {
    String username = "testUser";
    User mockUser = new User();
    mockUser.setUsername(username);

    when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(mockUser));
    User result = userService.getUserByUsername(username);
    assertEquals(mockUser, result);
  }

  @Test
  void testGetUserByName_UserNotFound() {
    String username = "nonExistentUser";
    when(userRepository.findUserByUsername(username)).thenReturn(Optional.empty());

    assertThrows(UsernameNotFoundException.class, () -> userService.getUserByUsername(username));
  }

  @Test
  void testGetAllUsers() {
    List<User> mockUsers = Collections.singletonList(new User());
    when(userRepository.findAll()).thenReturn(mockUsers);
    assertEquals(mockUsers, userService.getAllUsers());
  }

  @Test
  void testDeleteUser_WhenUserExists() {
    User user = new User();
    String username = "testUser";
    user.setUsername("testUser");
    when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
    userService.deleteUser(username);
    verify(userRepository, times(1)).delete(user);
  }

  @Test
  void testDeleteUser_WhenUserDoesNotExist() {
    String username = "nonExistentUser";
    when(userRepository.findUserByUsername(username)).thenReturn(Optional.empty());
    assertThrows(
        UsernameNotFoundException.class,
        () -> {
          userService.deleteUser(username);
        });
  }

  @Test
  void testGetUsersByCity_WhenPlayerExists() {
    // Arrange
    String cityName = "testCity";
    User user1 = new User();
    User user2 = new User();
    List<User> users = new ArrayList<>();
    users.add(user1);
    users.add(user2);

    when(userRepository.findUsersByCity(cityName)).thenReturn(Optional.of(users));

    List<User> result = userService.findUsersByCity(cityName);

    assertEquals(2, result.size());
    assertTrue(result.contains(user1));
    assertTrue(result.contains(user2));
  }

  @Test
  void testGetUsersByCity_WhenPlayerDoesNotExist() {
    // Arrange
    String cityName = "nonExistentCity";
    when(userRepository.findUsersByCity(cityName)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        CityNotFoundException.class,
        () -> {
          userService.findUsersByCity(cityName);
        });
  }
  @Test
  void testRemoveCity_AllExist() {
    String username = "testUser";
    String cityName = "testCity";

    User user = new User();
    user.setUsername(username);

    City city = new City();
    city.setCityName(cityName);

    user.getSavedCities().add(city);

    when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
    when(cityRepository.findCitiesByCityName(cityName)).thenReturn(Optional.of(city));

    userService.deleteCity(username, cityName);
    assertFalse(user.getSavedCities().contains(city));
    verify(cityRepository, times(1)).delete(city);
  }
  @Test
  void testRemoveCity_NoSuchUser() {
    String username = "nonExistentUser";
    String cityName = "testCity";

    when(userRepository.findUserByUsername(username)).thenReturn(Optional.empty());

    assertThrows(UsernameNotFoundException.class, () -> {
      userService.deleteCity(username, cityName);
    });
    verify(cityRepository, never()).findCitiesByCityName(any());
    verify(userRepository, never()).save(any());
  }
  @Test
  void testRemoveCity_NoSuchCity() {
    String username = "testUser";
    String cityName = "nonExistentCity";
    User user = new User();

    when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
    when(cityRepository.findCitiesByCityName(cityName)).thenReturn(Optional.empty());

    assertThrows(CityNotFoundException.class, () -> {
      userService.deleteCity(username, cityName);
    });
    verify(userRepository, never()).save(any());
  }
  @Test
  void testSaveUserToCache() {
    // Arrange
    User user = new User();
    when(cacheManager.getCache("dataUser")).thenReturn(cache);

    // Act
    userService.saveUserToCache(user);

    // Assert
    verify(cache, times(1)).put(user.getUsername(), user);
  }

  @Test
  void testSaveUserToCache_CacheNotFound() {
    // Arrange
    User user = new User();
    when(cacheManager.getCache("dataUser")).thenReturn(null);

    // Act
    userService.saveUserToCache(user);

    // Verify that no interaction with the cache occurred
    verifyNoInteractions(cache);
  }
  @Test
  void testGetSavedCitiesByUsername_WhenUserExists() {
    String username = "testUser";
    User user = new User();
    Set<City> favoritePlayers = new HashSet<>();
    favoritePlayers.add(new City());
    user.setSavedCities(favoritePlayers);
    when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
    assertEquals(favoritePlayers, userService.getSavedCitiesByUsername(username));
  }

  @Test
  void testGetSavedCitiesByUsername_WhenUserDoesNotExist() {

    String username = "nonExistentUser";
    when(userRepository.findUserByUsername(username)).thenReturn(Optional.empty());

    assertThrows(
            UsernameNotFoundException.class,
            () -> {
              userService.getSavedCitiesByUsername(username);
            });
  }
}
