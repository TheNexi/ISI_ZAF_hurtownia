package com.example.hurtownia_ISI_ZAF.controller;

import com.example.hurtownia_ISI_ZAF.request.DostawcaRequest;
import com.example.hurtownia_ISI_ZAF.response.DostawcaResponse;
import com.example.hurtownia_ISI_ZAF.service.DostawcaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dostawca")
@Tag(name = "Dostawcy", description = "Operacje na dostawcach")
public class DostawcaController {

    private final DostawcaService dostawcaService;

    @Autowired
    public DostawcaController(DostawcaService dostawcaService) {
        this.dostawcaService = dostawcaService;
    }

    @GetMapping
    public ResponseEntity<List<DostawcaResponse>> getAllDostawcy() {
        return ResponseEntity.ok(dostawcaService.getAllDostawcy());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DostawcaResponse> getDostawcaById(@PathVariable Integer id) {
        return ResponseEntity.ok(dostawcaService.getDostawcaById(id));
    }

    @PostMapping
    public ResponseEntity<DostawcaResponse> addDostawca(@RequestBody DostawcaRequest request) {
        return ResponseEntity.status(201).body(dostawcaService.addDostawca(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DostawcaResponse> updateDostawca(@PathVariable Integer id, @RequestBody DostawcaRequest request) {
        return ResponseEntity.ok(dostawcaService.updateDostawca(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDostawca(@PathVariable Integer id) {
        dostawcaService.deleteDostawca(id);
        return ResponseEntity.noContent().build();
    }
}
