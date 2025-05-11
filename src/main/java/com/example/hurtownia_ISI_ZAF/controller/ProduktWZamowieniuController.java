package com.example.hurtownia_ISI_ZAF.controller;

import com.example.hurtownia_ISI_ZAF.request.ProduktWZamowieniuRequest;
import com.example.hurtownia_ISI_ZAF.response.ProduktWZamowieniuResponse;
import com.example.hurtownia_ISI_ZAF.service.ProduktWZamowieniuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produkt-w-zamowieniu")
@Tag(name = "Produkt w Zamówieniu", description = "Operacje na pozycjach zamówień")
public class ProduktWZamowieniuController {

    private final ProduktWZamowieniuService service;

    @Autowired
    public ProduktWZamowieniuController(ProduktWZamowieniuService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProduktWZamowieniuResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProduktWZamowieniuResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<ProduktWZamowieniuResponse> add(@RequestBody ProduktWZamowieniuRequest request) {
        return ResponseEntity.status(201).body(service.add(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProduktWZamowieniuResponse> update(@PathVariable Integer id, @RequestBody ProduktWZamowieniuRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
