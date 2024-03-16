package com.weather.weather.controller;

import com.weather.weather.dao.CountryRepository;
import com.weather.weather.dao.UserRepository;
import com.weather.weather.entity.Country;
import com.weather.weather.security.JwtCore;
import com.weather.weather.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class SecurityController {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private CountryRepository countryRepository;
    private JwtCore jwtCore;

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
    public void setCountyRepository(CountryRepository countyRepository){
        this.countryRepository=countyRepository;
    }

    @Autowired
    public void setJwtCore(JwtCore jwtCore) {
        this.jwtCore = jwtCore;
    }

@PostMapping("/signup")
ResponseEntity<String> signup(@RequestBody SignUpRequest signUpRequest) {
    if (userRepository.existsUserByUsername(signUpRequest.getUsername()).booleanValue()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This nickname already exists");
    }

    String countryName = signUpRequest.getCountry();
    Country country = countryRepository.findCountryByCountryName(countryName);
    if (country == null) {
        // Если страна не существует, создаем новую страну
        country = new Country();
        country.setCountryName(countryName);
        countryRepository.save(country);
    }

    User user = new User();
    user.setUsername(signUpRequest.getUsername());
    user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
    user.setCountry(country);
    user.setRole("ROLE_USER");
    userRepository.save(user);

    return ResponseEntity.ok("Chinazes");
}
    @GetMapping("/signin")
        public String signinShow(){
            return "signin";
        }
    @PostMapping("/signin")
    ResponseEntity<String> signin(@RequestBody SignInRequest signInRequest) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtCore.generateToken(authentication);
        return ResponseEntity.ok(jwt);
    }
    @PatchMapping("/editPassword")
    public ResponseEntity<String> editPassword(@RequestBody PasswordRequest passwordRequest, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        String username = jwtCore.getNameFromJwt(token);
        User user=userRepository.findUserByUsername(username).orElseThrow(()->new UsernameNotFoundException(
                String.format("User '%s' not found",username)));
        user.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
        userRepository.save(user);

        return  ResponseEntity.ok("Password was successfully updated");
    }

}


