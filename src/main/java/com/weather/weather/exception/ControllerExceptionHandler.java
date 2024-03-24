package com.weather.weather.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler({HttpClientErrorException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(HttpClientErrorException ex, WebRequest request) {
        logger.error("Error 400");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error 400: Bad request");
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    public ResponseEntity<Object> handleNoResourceFoundException(NoHandlerFoundException ex, WebRequest request) {
        logger.error("Error 404");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error 404: Not Found");
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<Object> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        logger.error("Error 405: Method not supported");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Error 405: Method not supported");
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleAllExceptions(RuntimeException ex, WebRequest request) {
        logger.error("Error 500");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error 500: Internal server error");
    }
    @ExceptionHandler({HttpClientErrorException.Unauthorized.class})
    public ResponseEntity<Object> handleUnauthorizedException(HttpClientErrorException.Unauthorized ex,WebRequest request){
        logger.error("Error 401");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error 401: Unauthorized");
    }
    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<Object> usernameNotFoundException(UsernameNotFoundException ex,WebRequest request){
        logger.error("Error 404: User not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error 404: User Not Found");
    }
}
