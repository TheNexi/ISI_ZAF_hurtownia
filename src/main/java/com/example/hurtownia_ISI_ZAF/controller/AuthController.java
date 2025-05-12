package com.example.hurtownia_ISI_ZAF.controller;

import com.example.hurtownia_ISI_ZAF.request.LoginRequest;
import com.example.hurtownia_ISI_ZAF.request.RegisterRequest;
import com.example.hurtownia_ISI_ZAF.model.Uzytkownicy;
import com.example.hurtownia_ISI_ZAF.service.JwtTokenService;
import com.example.hurtownia_ISI_ZAF.service.UzytkownicyService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UzytkownicyService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        String token = jwtTokenService.generateToken(authentication);
        return ResponseEntity.ok("Bearer " + token);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        Uzytkownicy user = userService.registerUser(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                registerRequest.getEmail(),
                registerRequest.getRole()
        );
        return ResponseEntity.status(201).body(user);
    }
}
