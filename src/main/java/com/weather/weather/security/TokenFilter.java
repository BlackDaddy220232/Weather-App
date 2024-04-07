package com.weather.weather.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
@Data
@Component
public class TokenFilter extends OncePerRequestFilter {
  private JwtCore jwtCore;
  private UserDetailsService userDetailsService;
  private HandlerExceptionResolver handlerExceptionResolver;

  @Autowired
  public void setUserDetailsService(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }
  @Autowired
  public void setJwtCore(JwtCore jwtCore) {
    this.jwtCore = jwtCore;
  }
  @Autowired
  public void setHandlerExceptionResolver(HandlerExceptionResolver handlerExceptionResolver) {
    this.handlerExceptionResolver = handlerExceptionResolver;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String jwt = null;
    String username = null;
    UserDetails userDetails = null;
    UsernamePasswordAuthenticationToken auth = null;
    if (!request.getRequestURI().equals("/auth/signin")
        && !(request.getRequestURI().equals("/auth/signup"))) {
      try {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
          jwt = headerAuth.substring(7);
        }
        if (jwt != null) {
          username = jwtCore.getNameFromJwt(jwt);
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
          userDetails = userDetailsService.loadUserByUsername(username);
          auth =
              new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities());
          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      } catch (ExpiredJwtException e) {

      }
    }
    filterChain.doFilter(request, response);
  }
}
