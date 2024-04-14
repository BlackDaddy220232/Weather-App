package com.weather.weather.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PointcutDefinitions {
  @Pointcut("execution(* com.weather.weather.controller.SecurityController.signup(..))")
  public void signupPointcut() { }

  @Pointcut("execution(* com.weather.weather.controller.SecurityController.signin(..))")
  public void signinPointcut() { }

  @Pointcut("execution(* com.weather.weather.service.SecurityService.changePas(..))")
  public void editPasswordPointcut() { }

  @Pointcut("execution(* com.weather.weather.controller.CountryController.editCountryName(..))")
  public void editCountryNamePointcut() { }

  @Pointcut("execution(* com.weather.weather.controller.CountryController.deleteCountry(..))")
  public void deleteCountryPointcut() { }

  @Pointcut("execution(* com.weather.weather.service.UserService.addCityToUser(..))")
  public void addCityToUserPointcut() { }

  @Pointcut("execution(* com.weather.weather.service.UserService.deleteCity(..))")
  public void deleteCityPointcut() { }

  @Pointcut("execution(* com.weather.weather.service.UserService.deleteUser(..))")
  public void deleteUserPointcut() { }

  @Pointcut("execution(* com.weather.weather.service.*.get*(..))")
  public void getPointcut() { }
}
