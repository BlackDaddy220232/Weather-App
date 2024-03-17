package com.weather.weather.dao;

import com.weather.weather.model.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    Country findCountryByCountryName(String country);
}