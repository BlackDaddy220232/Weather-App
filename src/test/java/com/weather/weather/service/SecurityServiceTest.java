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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock private CountryRepository countryRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @Mock private AuthenticationManager authenticationManager;

    @Mock private JwtCore jwtCore;

    @InjectMocks
    private SecurityService securityService;

    @Test
    void testRegister_NewUser_Success() {
        // Arrange
        SignUpRequest signUpRequest =
                SignUpRequest.builder()
                        .username("username")
                        .password("password")
                        .country("CountryName")
                        .build();
        when(userRepository.existsUserByUsername(signUpRequest.getUsername())).thenReturn(false);
        when(countryRepository.findCountryByCountryName(signUpRequest.getCountry()))
                .thenReturn(Optional.empty());
        when(countryRepository.save(any())).thenReturn(new Country());
        when(passwordEncoder.encode(signUpRequest.getPassword())).thenReturn("encodedPassword");

        verify(userRepository, times(1)).save(any());
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtCore, times(1)).generateToken(any());
    }

    @Test
    void testRegister_ExistingUsername_ExceptionThrown() {
        SignUpRequest signUpRequest =
                SignUpRequest.builder()
                        .username("username")
                        .password("password")
                        .country("CountryName")
                        .build();
        when(userRepository.existsUserByUsername(signUpRequest.getUsername())).thenReturn(true);

        assertThrows(
                UnauthorizedException.class,
                () -> {
                    securityService.register(signUpRequest);
                });
        verify(userRepository, never()).save(any());
        verify(authenticationManager, never()).authenticate(any());
        verify(jwtCore, never()).generateToken(any());
    }

    @Test
    void testLogin_Success() {

        SignInRequest signInRequest =
                SignInRequest.builder().username("username").password("password").build();
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtCore.generateToken(authentication)).thenReturn("token");

        String token = securityService.login(signInRequest);

        assertNotNull(token);
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtCore, times(1)).generateToken(any());
    }

    @Test
    void testLogin_BadCredentials_ExceptionThrown() {
        SignInRequest signInRequest =
                SignInRequest.builder().username("username").password("password").build();
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException(""));

        assertThrows(
                UnauthorizedException.class,
                () -> {
                    securityService.login(signInRequest);
                });
        verify(jwtCore, never()).generateToken(any());
    }

    @Test
    void testChangePas_Success() {
        String username = "username";
        String newPassword = "newPassword";
        PasswordRequest passwordRequest = PasswordRequest.builder().password("newPassword").build();

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtCore.getNameFromJwt("token")).thenReturn(username);
        User user = new User();
        user.setUsername(username);
        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        String result = securityService.changePas(passwordRequest, request);

        assertEquals(username, result);
        assertEquals("encodedNewPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testChangePas_MissingAuthorizationHeader_ExceptionThrown() {
        PasswordRequest passwordRequest = PasswordRequest.builder().password("newPassword").build();

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);

        assertThrows(
                ResponseStatusException.class,
                () -> {
                    securityService.changePas(passwordRequest, request);
                });
        verify(userRepository, never()).findUserByUsername(any());
    }

    @Test
    void testChangePas_UserNotFound() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");

        String username = "nonExistingUser";
        PasswordRequest passwordRequest = PasswordRequest.builder()
                .password("newPassword")
                .build();

        when(jwtCore.getNameFromJwt("validToken")).thenReturn(username);
        when(userRepository.findUserByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            securityService.changePas(passwordRequest, request);
        });
    }
}
