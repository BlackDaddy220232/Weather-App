package com.weather.weather.dao;

import com.weather.weather.model.entity.City;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

/** Репозиторий для доступа к данным о городах. */
public interface CityRepository extends JpaRepository<City, Long> {

  /**
   * Найти города по имени города.
   *
   * @param cityName имя города
   * @return Optional с найденным городом, если существует
   */
  @EntityGraph(attributePaths = {"savedUsers"})
  Optional<City> findCitiesByCityName(String cityName);
}
