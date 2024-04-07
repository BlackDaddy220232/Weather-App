package com.weather.weather.aspect;

import com.weather.weather.model.dto.SignInRequest;
import com.weather.weather.model.dto.SignUpRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Slf4j
public class LoggingAspect {
  String username = null;

  @AfterReturning("PointcutDefinitions.signupPointcut()")
  public void logSignup(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    for (Object obj : args) {
      if (obj instanceof SignUpRequest signupRequest) {
        username = signupRequest.getUsername();
      }
    }
    log.info(String.format("User {%s} signed up", username));
  }

  @AfterReturning("PointcutDefinitions.signinPointcut()")
  public void logSignin(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    for (Object obj : args) {
      if (obj instanceof SignInRequest signInRequest) {
        username = signInRequest.getUsername();
      }
    }
    log.info(String.format("User {%s} signed in", username));
  }

  @AfterReturning(value = "PointcutDefinitions.editPasswordPointcut()", returning = "usernameValue")
  public void logEditPassword(JoinPoint joinPoint, String usernameValue) {
    log.info(String.format("User {%s} changed his password", usernameValue));
  }

  @AfterReturning("PointcutDefinitions.editCountryNamePointcut()")
  public void logEditCountry(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    String countryName = (String) args[0];
    String newCountryName = (String) args[1];
    log.info("Country Name {" + countryName + "} has changed to {" + newCountryName + "}");
  }

  @AfterReturning(pointcut = "PointcutDefinitions.addCityToUserPointcut()")
  public void logAddPlayer(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    username = (String) args[0];
    String countryName = (String) args[1];
    log.info("Город {" + countryName + "} был добавлен пользователю {" + username + "}");
  }

  @AfterReturning(pointcut = "PointcutDefinitions.deleteCityPointcut()")
  public void logDeleteCity(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    username = (String) args[0];
    String countryName = (String) args[1];
    log.info(
        "Город {"
            + countryName
            + "} был удален из списка любимых городов пользователя {"
            + username
            + "}");
  }

  @AfterReturning("PointcutDefinitions.deleteUserPointcut()")
  public void logDeleteUser(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    username = (String) args[0];
    log.info("Аккаунт пользователя {" + username + "} был удален");
  }
}
