package com.weather.weather.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cities")
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cityName;
    @JsonIgnore
    @ManyToMany(mappedBy = "savedCities")
    private Set<User> savedUsers = new HashSet<>();

}
