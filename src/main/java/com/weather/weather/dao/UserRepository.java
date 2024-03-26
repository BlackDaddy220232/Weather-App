package com.weather.weather.dao;

import com.weather.weather.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findUserByUsername(String username);

  Boolean existsUserByUsername(String username);

  @Query("SELECT u FROM User u JOIN u.savedCities c WHERE c.cityName = :cityName")
  List<User> findUsersByCity(@Param("cityName") String cityName);
}
