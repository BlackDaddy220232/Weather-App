package com.weather.weather.dao;

import com.weather.weather.model.entity.City;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City,Long> {
    @EntityGraph(attributePaths = {"savedUsers"})
    Optional<City> findCitiesByCityName(String cityName);
}
