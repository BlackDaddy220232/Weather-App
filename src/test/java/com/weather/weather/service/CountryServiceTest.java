package com.weather.weather.service;

import com.weather.weather.dao.CountryRepository;
import com.weather.weather.exception.CountryNotFoundException;
import com.weather.weather.model.entity.Country;
import com.weather.weather.model.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;
    @Mock
    private Country country;
    @InjectMocks
    private CountryService countryService;


    @Test
    void testGetCountryUsers_WhenCountryExists() {
        String countryName = "TestCountry";
        country = new Country();
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        country.setUsers(users);

        when(countryRepository.findCountryByCountryName(countryName)).thenReturn(Optional.of(country));

        List<User> result = countryService.getCountryUsers(countryName);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetCountryUsers_WhenCountryDoesNotExist() {
        String countryName = "NonExistentCountry";

        when(countryRepository.findCountryByCountryName(countryName)).thenReturn(Optional.empty());

        assertThrows(CountryNotFoundException.class, () -> {
            countryService.getCountryUsers(countryName);
        });
    }

    @Test
    void testGetCountries() {
        List<Country> countries = new ArrayList<>();
        countries.add(new Country());
        countries.add(new Country());

        when(countryRepository.findAll()).thenReturn(countries);

        List<Country> result = countryService.getCountries();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testEditCountryName_WhenNewCountryExists() {
        String countryName = "TestCountry";
        String newCountryName = "NewTestCountry";
        Country currentCountry = new Country();
        Country newCountry = new Country();
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        currentCountry.setUsers(users);
        newCountry.setUsers(new ArrayList<>());
        when(countryRepository.findCountryByCountryName(countryName)).thenReturn(Optional.of(currentCountry));
        when(countryRepository.findCountryByCountryName(newCountryName)).thenReturn(Optional.of(newCountry));

        countryService.editCountryName(countryName, newCountryName);

        assertEquals(2, newCountry.getUsers().size());
        verify(countryRepository, times(1)).save(newCountry);
        verify(countryRepository, times(1)).delete(currentCountry);
    }

    @Test
    void testEditCountryName_WhenNewCountryDoesNotExist() {
        String countryName = "TestCountry";
        String newCountryName = "NewTestCountry";
        Country currentCountry = new Country();
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        currentCountry.setUsers(users);

        when(countryRepository.findCountryByCountryName(countryName)).thenReturn(Optional.of(currentCountry));
        when(countryRepository.findCountryByCountryName(newCountryName)).thenReturn(Optional.empty());

        countryService.editCountryName(countryName, newCountryName);

        assertEquals(newCountryName, currentCountry.getCountryName());
        verify(countryRepository, times(1)).save(currentCountry);
        verify(countryRepository, never()).delete(any());
    }
    @Test
    void testEditCountry_WhenCountryDoesNotExist() {

        String countryName = "TestCountry";
        String newCountryName = "NewTestCountry";

        when(countryRepository.findCountryByCountryName(countryName)).thenReturn(Optional.empty());

        assertThrows(CountryNotFoundException.class, () -> {
            countryService.editCountryName(countryName,newCountryName);
        });
        verify(countryRepository, never()).delete(any());
    }

    @Test
    void testDeleteCountry_WhenCountryExists() {
        String countryName = "TestCountry";
        country = new Country();

        when(countryRepository.findCountryByCountryName(countryName)).thenReturn(Optional.of(country));
        countryService.deleteCountry(countryName);

        verify(countryRepository, times(1)).delete(country);
    }

    @Test
    void testDeleteCountry_WhenCountryDoesNotExist() {
        String countryName = "NonExistentCountry";

        when(countryRepository.findCountryByCountryName(countryName)).thenReturn(Optional.empty());

        assertThrows(CountryNotFoundException.class, () -> {
            countryService.deleteCountry(countryName);
        });
        verify(countryRepository, never()).delete(any());
    }
}
