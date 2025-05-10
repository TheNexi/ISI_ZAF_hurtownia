package com.example.hurtownia_ISI_ZAF.controller;

import com.example.hurtownia_ISI_ZAF.request.KlientRequest;
import com.example.hurtownia_ISI_ZAF.response.KlientResponse;
import com.example.hurtownia_ISI_ZAF.service.KlientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/klient")
@Tag(name = "Klienci", description = "Operacje na klientach")
public class KlientController {

    private final KlientService klientService;

    @Autowired
    public KlientController(KlientService klientService) {
        this.klientService = klientService;
    }

    @GetMapping
    public ResponseEntity<List<KlientResponse>> getAllKlienci() {
        return ResponseEntity.ok(klientService.getAllKlienci());
    }

    @GetMapping("/{id}")
    public ResponseEntity<KlientResponse> getKlientById(@PathVariable Integer id) {
        return ResponseEntity.ok(klientService.getKlientById(id));
    }

    @PostMapping
    public ResponseEntity<KlientResponse> addKlient(@RequestBody KlientRequest request) {
        return ResponseEntity.status(201).body(klientService.addKlient(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<KlientResponse> updateKlient(@PathVariable Integer id, @RequestBody KlientRequest request) {
        return ResponseEntity.ok(klientService.updateKlient(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKlient(@PathVariable Integer id) {
        klientService.deleteKlient(id);
        return ResponseEntity.noContent().build();
    }
}
