package com.weather.weather.exception;

public class CustomJwtException extends RuntimeException {
    public CustomJwtException(String message){
        super(message);
    }
}
