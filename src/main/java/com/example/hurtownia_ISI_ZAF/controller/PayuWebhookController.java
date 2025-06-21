package com.example.hurtownia_ISI_ZAF.controller;

import com.example.hurtownia_ISI_ZAF.model.Zamowienie;
import com.example.hurtownia_ISI_ZAF.repository.ZamowienieRepository;
import com.example.hurtownia_ISI_ZAF.model.Zamowienie.StatusPlatnosci;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/payu")
public class PayuWebhookController {

    private final ZamowienieRepository zamowienieRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PayuWebhookController(ZamowienieRepository zamowienieRepository) {
        this.zamowienieRepository = zamowienieRepository;
    }

    @PostMapping("/notify")
    public ResponseEntity<String> handleNotify(@RequestBody String body) {
        System.out.println("PAYU NOTIFY BODY:\n" + body);

        try {
            JsonNode root = objectMapper.readTree(body);
            JsonNode order = root.path("order");

            if (order.isMissingNode()) {
                return ResponseEntity.badRequest().body("Brak danych o zamówieniu.");
            }

            String extOrderId = order.path("description").asText();
            String status = order.path("status").asText();

            if (extOrderId == null || status == null) {
                return ResponseEntity.badRequest().body("Nieprawidłowe dane.");
            }

            Integer idZamowienia;
            try {
                idZamowienia = Integer.parseInt(extOrderId.replaceAll("\\D+", ""));
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Nieprawidłowy format extOrderId");
            }

            Zamowienie zamowienie = zamowienieRepository.findById(idZamowienia)
                    .orElseThrow(() -> new RuntimeException("Nie znaleziono zamówienia o ID " + idZamowienia));

            switch (status) {
                case "COMPLETED" -> zamowienie.setStatusPlatnosci(StatusPlatnosci.SUCCESS);
                case "CANCELED" -> zamowienie.setStatusPlatnosci(StatusPlatnosci.FAILED);
                case "PENDING" -> zamowienie.setStatusPlatnosci(StatusPlatnosci.PENDING);
                default -> {
                    System.out.println("Nieobsługiwany status PayU: " + status);
                    return ResponseEntity.badRequest().body("Nieznany status: " + status);
                }
            }

            zamowienieRepository.save(zamowienie);
            return ResponseEntity.ok("Zaktualizowano status zamówienia.");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Błąd parsowania JSON-a");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Błąd wewnętrzny");
        }
    }

}
