package com.Auth.controller;



import com.Auth.dto.AuthResponse;
import com.Auth.dto.LoginRequest;
import com.Auth.dto.RefreshTokenRequest;
import com.Auth.dto.RegisterRequest;
import com.Auth.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest req){
        return service.register(req);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req){
        return service.login(req);
    }
    
    
    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshTokenRequest request){
        return service.refreshToken(request.getRefreshToken());
    }

    

    @PostMapping("/logout")
    public String logout(HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        service.logout(token);
        return "Logged out successfully";
    }
}
