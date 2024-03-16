package com.weather.weather.service;

import com.weather.weather.dao.CountryRepository;
import com.weather.weather.dao.UserRepository;
import com.weather.weather.entity.Country;
import com.weather.weather.entity.User;
import com.weather.weather.exception.CountryNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CountryService {
    private CountryRepository countryRepository;
    private UserRepository userRepository;
    @Autowired
    public void setCountryRepository(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getCountryUsers(String countryName) throws CountryNotFoundException
    {
        if (countryRepository.findCountryByCountryName(countryName)==null)
        {
            throw new CountryNotFoundException("Пользователь не найден");
        }
        Country country = countryRepository.findCountryByCountryName(countryName);
        return country.getUsers();
    }
    public List<Country> getCountries(){
        return countryRepository.findAll();
    }
    public void editCountryName(String countryName,String newCountryName) throws CountryNotFoundException{
        Country country= countryRepository.findCountryByCountryName(countryName);
        country.setCountryName(newCountryName);
    }
    public void deleteCountry(String countyName){
        Country country= countryRepository.findCountryByCountryName(countyName);
        countryRepository.delete(country);
    }

}
