package com.weather.weather.service;

import com.weather.weather.dao.CountryRepository;
import com.weather.weather.dao.UserRepository;
import com.weather.weather.exception.UnauthorizedException;
import com.weather.weather.model.dto.PasswordRequest;
import com.weather.weather.model.dto.SignInRequest;
import com.weather.weather.model.dto.SignUpRequest;
import com.weather.weather.model.entity.Country;
import com.weather.weather.model.entity.User;
import com.weather.weather.security.JwtCore;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class SecurityService {
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;
  private AuthenticationManager authenticationManager;
  private JwtCore jwtCore;

  private CountryRepository countryRepository;

  @Autowired
  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Autowired
  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Autowired
  public void setAuthenticationManager(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Autowired
  public void setJwtCore(JwtCore jwtCore) {
    this.jwtCore = jwtCore;
  }

  @Autowired
  public void setCountyRepository(CountryRepository countyRepository) {
    this.countryRepository = countyRepository;
  }

  public String register(SignUpRequest signUpRequest) {
    if (userRepository.existsUserByUsername(signUpRequest.getUsername()).booleanValue()) {
      throw new UnauthorizedException(
              String.format("Имя \"%s\" уже занято (((", signUpRequest.getUsername()));
    }
    User user = new User();
    user.setUsername(signUpRequest.getUsername());
    user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
    Country country =
        countryRepository
            .findCountryByCountryName(signUpRequest.getCountry())
            .orElseGet(
                () -> {
                  Country newCountry = new Country();
                  newCountry.setCountryName(signUpRequest.getCountry());
                  return countryRepository.save(newCountry);
                });
    user.setCountry(country);
    user.setRole("ROLE_USER");
    userRepository.save(user);
    Authentication authentication = null;
    authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                signUpRequest.getUsername(), signUpRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return jwtCore.generateToken(authentication);
  }

  public String login(SignInRequest signInRequest) {
    Authentication authentication = null;
    try {
      authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  signInRequest.getUsername(), signInRequest.getPassword()));
    } catch (BadCredentialsException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return (jwtCore.generateToken(authentication));
  }

  public String changePas(PasswordRequest passwordRequest, HttpServletRequest request) {
    String authorizationHeader = request.getHeader("Authorization");
    String token;
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      token = authorizationHeader.substring(7);
    } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    String username = jwtCore.getNameFromJwt(token);
    User user =
        userRepository
            .findUserByUsername(username)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(String.format("User '%s' not found", username)));
    user.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
    userRepository.save(user);
    return username;
  }
}
