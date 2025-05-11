package com.example.hurtownia_ISI_ZAF.controller;

import com.example.hurtownia_ISI_ZAF.request.CzasRequest;
import com.example.hurtownia_ISI_ZAF.response.CzasResponse;
import com.example.hurtownia_ISI_ZAF.service.CzasService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/czas")
@Tag(name = "Czas", description = "Operacje na czasie")
public class CzasController {

    private final CzasService czasService;

    @Autowired
    public CzasController(CzasService czasService) {
        this.czasService = czasService;
    }

    @GetMapping
    public ResponseEntity<List<CzasResponse>> getAllCzasy() {
        return ResponseEntity.ok(czasService.getAllCzasy());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CzasResponse> getCzasById(@PathVariable Integer id) {
        return ResponseEntity.ok(czasService.getCzasById(id));
    }

    @PostMapping
    public ResponseEntity<CzasResponse> addCzas(@RequestBody CzasRequest request) {
        return ResponseEntity.status(201).body(czasService.addCzas(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CzasResponse> updateCzas(@PathVariable Integer id, @RequestBody CzasRequest request) {
        return ResponseEntity.ok(czasService.updateCzas(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCzas(@PathVariable Integer id) {
        czasService.deleteCzas(id);
        return ResponseEntity.noContent().build();
    }
}
