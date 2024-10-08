package com.weather.weather.aspect;

import com.weather.weather.model.dto.SignInRequest;
import com.weather.weather.model.dto.SignUpRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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
  public void logAddCity(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    username = (String) args[1];
    String countryName = (String) args[0];
    log.info("City {" + countryName + "} was added to user {" + username + "}");
  }

  @AfterReturning(pointcut = "PointcutDefinitions.deleteCityPointcut()")
  public void logDeleteCity(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    username = (String) args[0];
    String countryName = (String) args[1];
    log.info("City {" + countryName + "} was deleted from list user's cities {" + username + "}");
  }

  @AfterReturning("PointcutDefinitions.deleteUserPointcut()")
  public void logDeleteUser(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    username = (String) args[0];
    log.info("User's account {" + username + "} was deleted");
  }

  @AfterReturning(value = "PointcutDefinitions.getPointcut()", returning = "object")
  public void logGetCrate(JoinPoint joinPoint, Object object) {
    log.info("Returned {}", object);
  }

  @Before("PointcutDefinitions.getPointcut()")
  public void logGetCall(JoinPoint joinPoint) {
    String signatureMethod = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();
    log.info("Method {} was called with arguments {}", signatureMethod, args);
  }

  @AfterReturning(pointcut = "PointcutDefinitions.incrementCounter()", returning = "result")
  public void logCounter(Object result) {
    log.info("Counter: {}", result);
  }
}
