package com.weather.weather.controller;

import com.weather.weather.model.dto.PasswordRequest;
import com.weather.weather.model.dto.SignInRequest;
import com.weather.weather.model.dto.SignUpRequest;
import com.weather.weather.service.SecurityService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class SecurityController {
    private SecurityService securityService;
    @Autowired
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }
    @PostMapping("/signup")
    ResponseEntity<String> signup(@RequestBody SignUpRequest signUpRequest) {
        try {
            return ResponseEntity.ok(securityService.register(signUpRequest));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/signin")
    ResponseEntity<String> signin(@RequestBody SignInRequest signInRequest) {
        return ResponseEntity.ok(securityService.login(signInRequest));
    }
    @PatchMapping("/editPassword")
    public ResponseEntity<String> editPassword(@RequestBody PasswordRequest passwordRequest, HttpServletRequest request) {
        securityService.changePas(passwordRequest,request);
        return  ResponseEntity.ok("Password was successfully updated");
    }

}


