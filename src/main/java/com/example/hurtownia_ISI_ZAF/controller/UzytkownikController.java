package com.example.hurtownia_ISI_ZAF.controller;

import com.example.hurtownia_ISI_ZAF.model.Klient;
import com.example.hurtownia_ISI_ZAF.model.Uzytkownicy;
import com.example.hurtownia_ISI_ZAF.repository.KlientRepository;
import com.example.hurtownia_ISI_ZAF.repository.UzytkownicyRepository;
import com.example.hurtownia_ISI_ZAF.service.JwtTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UzytkownikController {

    private final UzytkownicyRepository userRepository;
    private final KlientRepository klientRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    @PostMapping("/updateData")
    public ResponseEntity<?> updateClientData(@RequestBody Klient klientData, HttpServletRequest request) {
        String token = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null || !jwtTokenService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String email = jwtTokenService.getEmailFromToken(token);
        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        Optional<Uzytkownicy> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Uzytkownicy user = userOpt.get();

        Klient klient;
        if (user.getKlient() != null) {
            klient = user.getKlient();
            klient.setNazwa(klientData.getNazwa());
            klient.setNip(klientData.getNip());
            klient.setKraj(klientData.getKraj());
            klient.setMiasto(klientData.getMiasto());
            klient.setUlica(klientData.getUlica());
            klient.setKodPocztowy(klientData.getKodPocztowy());
        } else {
            klient = klientData;
            klient = klientRepository.save(klient);
            user.setKlient(klient);
        }

        klientRepository.save(klient);
        userRepository.save(user);

        return ResponseEntity.ok(klient);
    }

    @GetMapping("/client")
    public ResponseEntity<?> getClientData(HttpServletRequest request) {
        String token = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null || !jwtTokenService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String email = jwtTokenService.getEmailFromToken(token);
        Optional<Uzytkownicy> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Uzytkownicy user = userOpt.get();
        if (user.getKlient() == null) {
            return ResponseEntity.ok().body(null);
        }
        return ResponseEntity.ok(user.getKlient());
    }
}
