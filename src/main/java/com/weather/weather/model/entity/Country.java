package com.weather.weather.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "countries")
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "country")
    private String countryName;

    @OneToMany(mappedBy = "country",fetch = FetchType.EAGER,cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnore
    private List<User> users;
}