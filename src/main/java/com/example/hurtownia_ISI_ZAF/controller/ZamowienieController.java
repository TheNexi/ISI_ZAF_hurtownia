package com.example.hurtownia_ISI_ZAF.controller;

import com.example.hurtownia_ISI_ZAF.request.ZamowienieRequest;
import com.example.hurtownia_ISI_ZAF.response.ZamowienieResponse;
import com.example.hurtownia_ISI_ZAF.service.ZamowienieService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zamowienie")
@Tag(name = "Zamówienie", description = "Operacje na zamówieniach")
public class ZamowienieController {

    private final ZamowienieService zamowienieService;

    @Autowired
    public ZamowienieController(ZamowienieService zamowienieService) {
        this.zamowienieService = zamowienieService;
    }

    @GetMapping
    public ResponseEntity<List<ZamowienieResponse>> getAllZamowienia() {
        return ResponseEntity.ok(zamowienieService.getAllZamowienia());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ZamowienieResponse> getZamowienieById(@PathVariable Integer id) {
        return ResponseEntity.ok(zamowienieService.getZamowienieById(id));
    }

    @PostMapping
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
}
