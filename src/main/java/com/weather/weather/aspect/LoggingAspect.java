package com.weather.weather.aspect;

import com.weather.weather.model.dto.SignInRequest;
import com.weather.weather.model.dto.SignUpRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import static java.rmi.server.LogStream.log;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    String username=null;
    @AfterReturning("PointcutDefinitions.signupPointcut()")
    public void logSignup(JoinPoint joinPoint) {
        Object[] args= joinPoint.getArgs();
        for (Object obj:args){
            if(obj instanceof SignUpRequest){
                username=((SignUpRequest) obj).getUsername();
            }
        }
        log.info(String.format("User {%s} signed up",username));
    }

    @AfterReturning("PointcutDefinitions.signinPointcut()")
    public void logSignin(JoinPoint joinPoint) {
        Object[] args= joinPoint.getArgs();
        for (Object obj:args){
            if(obj instanceof SignInRequest){
                username=((SignInRequest) obj).getUsername();
            }
        }
        log.info(String.format("User {%s} signed in",username));
    }

    @AfterReturning(value = "PointcutDefinitions.editPasswordPointcut()",returning = "usernameValue")
    public void logEditPassword(JoinPoint joinPoint,String usernameValue) {
        log.info(String.format("User {%s} changed his password",usernameValue));
    }
    @AfterReturning("PointcutDefinitions.editCountryNamePointcut()")
    public void logEditCountry(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String countryName = (String) args[0];
        String newCountryName = (String) args[1];
        log.info("Country Name {" + countryName + "} has changed to {" + newCountryName + "}");
    }
}
