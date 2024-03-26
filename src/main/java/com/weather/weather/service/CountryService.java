package com.weather.weather.service;

import com.weather.weather.dao.CountryRepository;
import com.weather.weather.model.entity.Country;
import com.weather.weather.model.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CountryService {
  private CountryRepository countryRepository;

  @Autowired
  public void setCountryRepository(CountryRepository countryRepository) {
    this.countryRepository = countryRepository;
  }

  public List<User> getCountryUsers(String countryName) throws IllegalAccessException {
    if (countryRepository.findCountryByCountryName(countryName) == null) {
      throw new IllegalAccessException("Country doesn't exist");
    }
    Country country = countryRepository.findCountryByCountryName(countryName);
    return country.getUsers();
  }

  public List<Country> getCountries() {
    return countryRepository.findAll();
  }

  public void editCountryName(String countryName, String newCountryName) {
    Country country = countryRepository.findCountryByCountryName(countryName);
    country.setCountryName(newCountryName);
  }

  public void deleteCountry(String countyName) {
    Country country = countryRepository.findCountryByCountryName(countyName);
    countryRepository.delete(country);
  }
}
