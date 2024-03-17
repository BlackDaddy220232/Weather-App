package com.weather.weather.service;

import com.weather.weather.dao.CityRepository;
import com.weather.weather.dao.UserRepository;
import com.weather.weather.model.entity.City;
import com.weather.weather.model.entity.User;
import com.weather.weather.security.JwtCore;
import com.weather.weather.security.UserDetailsImpl;
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
public class UserService implements UserDetailsService {
    private CityRepository cityRepository;
    private UserRepository userRepository;
    private JwtCore jwtCore;
    @Autowired
    public void setJwtCore(JwtCore jwtCore) {
        this.jwtCore = jwtCore;
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
        User user=userRepository.findUserByUsername(username).orElseThrow(()->new UsernameNotFoundException(
                String.format("User '%s' not found",username)));
        return UserDetailsImpl.build(user);
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public void deleteUser(String username) {
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
        } else {
            throw new IllegalArgumentException("User with username " + username + " not found");
        }
    }
    public void addCityToUser(String cityName,String token) {
        String username = jwtCore.getNameFromJwt(token);
        User user = userRepository.findUserByUsername(username).orElse(null);
        City city = cityRepository.findCitiesByCityName(cityName).orElse(null);
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
            userRepository.save(user);
        }
    }
    public Set<City> getSavedCitiesByToken(String token) {
        String username = jwtCore.getNameFromJwt(token);
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        if (userOptional.isPresent()) {
            return userOptional.get().getSavedCities();
        } else {
            throw new IllegalArgumentException("User with username " + username + " not found");
        }
    }
    public void deleteCity(String token, String cityName) {
        String username = jwtCore.getNameFromJwt(token);
        User user = userRepository.findUserByUsername(username).orElse(null);
        City city = cityRepository.findCitiesByCityName(cityName).orElse(null);

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
    public String getTokenFromRequest(String authorizationHeader){
        String token;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return token;
    }
}
