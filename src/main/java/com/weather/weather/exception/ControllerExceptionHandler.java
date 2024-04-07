package com.weather.weather.exception;

import com.weather.weather.model.dto.ResponseError;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
/**
Обработчик исключения контроллеров.
 */

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {
  @ExceptionHandler({InsufficientAuthenticationException.class})
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseError handleInsufflicientException(Exception ex, WebRequest request) {
    return new ResponseError(HttpStatus.UNAUTHORIZED, ex.getMessage());
  }

  @ExceptionHandler({HttpClientErrorException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseError handleIllegalArgumentException(
      HttpClientErrorException ex, WebRequest request) {
    log.error("Error 400: Bad Request");
    return new ResponseError(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler({
    NoHandlerFoundException.class,
    UsernameNotFoundException.class,
    CountryNotFoundException.class
  })
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseError handleNoResourceFoundException(RuntimeException ex, WebRequest request) {
    log.error("Error 404: Not Found");
    return new ResponseError(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  public ResponseError handleMethodNotSupportedException(
      HttpRequestMethodNotSupportedException ex, WebRequest request) {
    log.error("Error 405: Method not supported");
    return new ResponseError(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage());
  }

  @ExceptionHandler({ExpiredJwtException.class, UnauthorizedException.class})
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseError handleUnauthorizedException(RuntimeException ex, WebRequest request) {
    log.error("Error 401: Unauthorized");
    return new ResponseError(HttpStatus.UNAUTHORIZED, ex.getMessage());
  }

  @ExceptionHandler({RuntimeException.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseError handleAllExceptions(RuntimeException ex, WebRequest request) {
    log.error("Error 500: Internal Server Error");
    return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
  }
}
