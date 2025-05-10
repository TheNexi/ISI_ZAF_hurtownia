package com.example.hurtownia_ISI_ZAF.controller;

import com.example.hurtownia_ISI_ZAF.request.ProduktRequest;
import com.example.hurtownia_ISI_ZAF.response.ProduktResponse;
import com.example.hurtownia_ISI_ZAF.service.ProduktService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produkt")
@Tag(name = "Produkty", description = "Operacje na produktach")
public class ProduktController {

    private final ProduktService produktService;

    @Autowired
    public ProduktController(ProduktService produktService) {
        this.produktService = produktService;
    }

    @GetMapping
    public ResponseEntity<List<ProduktResponse>> getAllProdukty() {
        return ResponseEntity.ok(produktService.getAllProdukty());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProduktResponse> getProduktById(@PathVariable Integer id) {
        return ResponseEntity.ok(produktService.getProduktById(id));
    }

    @PostMapping
    public ResponseEntity<ProduktResponse> addProdukt(@RequestBody ProduktRequest request) {
        return ResponseEntity.status(201).body(produktService.addProdukt(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProduktResponse> updateProdukt(@PathVariable Integer id, @RequestBody ProduktRequest request) {
        return ResponseEntity.ok(produktService.updateProdukt(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProdukt(@PathVariable Integer id) {
        produktService.deleteProdukt(id);
        return ResponseEntity.noContent().build();
    }
}
