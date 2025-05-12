package com.example.hurtownia_ISI_ZAF.controller;

import com.example.hurtownia_ISI_ZAF.model.Uzytkownicy;
import com.example.hurtownia_ISI_ZAF.repository.UzytkownicyRepository;
import com.example.hurtownia_ISI_ZAF.request.LoginRequest;
import com.example.hurtownia_ISI_ZAF.response.JwtTokenResponse;
import com.example.hurtownia_ISI_ZAF.service.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class JwtTokenController {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UzytkownicyRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<Uzytkownicy> optionalUser = userRepository.findByLogin(loginRequest.getUsername());
        if (optionalUser.isPresent()) {
            Uzytkownicy user = optionalUser.get();
            if (passwordMatches(loginRequest.getPassword(), user.getHaslo())) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(user.getLogin(), loginRequest.getPassword());
                String token = jwtTokenService.generateToken(authentication);
                return ResponseEntity.ok(new JwtTokenResponse(token));
            }
        }
        return ResponseEntity.status(401).body("Błędne dane logowania");
    }

    private boolean passwordMatches(String inputPassword, String storedPassword) {
        return passwordEncoder.matches(inputPassword, storedPassword);
    }
}
