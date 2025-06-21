package com.example.hurtownia_ISI_ZAF.controller;

import com.example.hurtownia_ISI_ZAF.model.Uzytkownicy;
import com.example.hurtownia_ISI_ZAF.request.ZamowienieRequest;
import com.example.hurtownia_ISI_ZAF.response.ZamowienieResponse;
import com.example.hurtownia_ISI_ZAF.service.UzytkownicyService;
import com.example.hurtownia_ISI_ZAF.service.ZamowienieService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/zamowienie")
@Tag(name = "Zamówienie", description = "Operacje na zamówieniach")
public class ZamowienieController {

    private final ZamowienieService zamowienieService;

    @Autowired
    private UzytkownicyService uzytkownicyService;

    @Autowired
    public ZamowienieController(ZamowienieService zamowienieService) {
        this.zamowienieService = zamowienieService;
    }

    @GetMapping("/my")
    public ResponseEntity<List<ZamowienieResponse>> getMyOrders(Authentication authentication) {
        String email = null;
        Object principal = authentication.getPrincipal();
        if (principal instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) principal;
            email = oAuth2User.getAttribute("email");
        } else if (principal instanceof String) {
            String username = (String) principal;
            Optional<Uzytkownicy> userOpt = uzytkownicyService.findUserByLogin(username);
            if (userOpt.isPresent()) {
                email = userOpt.get().getEmail();
            }
        }
        if (email == null) return ResponseEntity.status(401).build();

        Optional<Uzytkownicy> userOpt = uzytkownicyService.findUserByEmail(email);
        if (userOpt.isEmpty() || userOpt.get().getKlient() == null)
            return ResponseEntity.status(404).build();

        Integer klientId = userOpt.get().getKlient().getId();
        List<ZamowienieResponse> orders = zamowienieService.getZamowieniaByKlientId(klientId);

        return ResponseEntity.ok(orders);
    }

    @GetMapping
    public ResponseEntity<List<ZamowienieResponse>> getAllZamowienia() {
        return ResponseEntity.ok(zamowienieService.getAllZamowienia());
    }



    @GetMapping("/{id}")
    public ResponseEntity<ZamowienieResponse> getZamowienieById(@PathVariable Integer id) {
        return ResponseEntity.ok(zamowienieService.getZamowienieById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<ZamowienieResponse> addZamowienie(@RequestBody ZamowienieRequest request) {
        return ResponseEntity.status(201).body(zamowienieService.addZamowienie(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ZamowienieResponse> updateZamowienie(@PathVariable Integer id, @RequestBody ZamowienieRequest request) {
        return ResponseEntity.ok(zamowienieService.updateZamowienie(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZamowienie(@PathVariable Integer id) {
        zamowienieService.deleteZamowienie(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/zatwierdz-offline")
    public ResponseEntity<ZamowienieResponse> zatwierdzOffline(@PathVariable Integer id) {
        ZamowienieResponse response = zamowienieService.zatwierdzPlatnoscOffline(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/payu")
    public ResponseEntity<Map<String, String>> initPayment(@PathVariable Integer id) {
        String redirectUrl = zamowienieService.inicjalizujPlatnosc(id);
        return ResponseEntity.ok(Map.of("redirectUrl", redirectUrl));
    }


}
