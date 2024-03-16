package com.weather.weather.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String username;
    @Column
    private String password;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id")
    private Country country;
    private String role;
    @PreRemove
    public void removeUser() {
        country.getUsers().removeAll(Collections.singleton(this));
    }
}
