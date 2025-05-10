package com.example.hurtownia_ISI_ZAF.controller;

import com.example.hurtownia_ISI_ZAF.request.MagazynRequest;
import com.example.hurtownia_ISI_ZAF.response.MagazynResponse;
import com.example.hurtownia_ISI_ZAF.service.MagazynService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/magazyn")
@Tag(name = "Magazyny", description = "Operacje na magazynach")
public class MagazynController {

    private final MagazynService magazynService;

    @Autowired
    public MagazynController(MagazynService magazynService) {
        this.magazynService = magazynService;
    }

    @GetMapping
    public ResponseEntity<List<MagazynResponse>> getAllMagazyny() {
        return ResponseEntity.ok(magazynService.getAllMagazyny());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MagazynResponse> getMagazynById(@PathVariable Integer id) {
        return ResponseEntity.ok(magazynService.getMagazynById(id));
    }

    @PostMapping
    public ResponseEntity<MagazynResponse> addMagazyn(@RequestBody MagazynRequest request) {
        return ResponseEntity.status(201).body(magazynService.addMagazyn(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MagazynResponse> updateMagazyn(@PathVariable Integer id, @RequestBody MagazynRequest request) {
        return ResponseEntity.ok(magazynService.updateMagazyn(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMagazyn(@PathVariable Integer id) {
        magazynService.deleteMagazyn(id);
        return ResponseEntity.noContent().build();
    }
}
