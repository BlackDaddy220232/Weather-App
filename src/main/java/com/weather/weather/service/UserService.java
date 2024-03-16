package com.weather.weather.service;

import com.weather.weather.dao.UserRepository;
import com.weather.weather.entity.User;
import com.weather.weather.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.findUserByUsername(username).orElseThrow(()->new UsernameNotFoundException(
                String.format("User '%s' not found",username)));
        return UserDetailsImpl.build(user);
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public void deleteUser(String username) {
        System.out.println("sosat1");
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        System.out.println("sosat2");
        if (userOptional.isPresent()) {
            System.out.println("sosat3");
            userRepository.delete(userOptional.get());
            System.out.println(username);
        } else {
            throw new IllegalArgumentException("User with username " + username + " not found");
        }
    }
}
