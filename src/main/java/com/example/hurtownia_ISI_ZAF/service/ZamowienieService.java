package com.example.hurtownia_ISI_ZAF.service;

import com.example.hurtownia_ISI_ZAF.model.*;
import com.example.hurtownia_ISI_ZAF.repository.*;
import com.example.hurtownia_ISI_ZAF.request.*;
import com.example.hurtownia_ISI_ZAF.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ZamowienieService {

    private final ZamowienieRepository zamowienieRepository;
    private final KlientRepository klientRepository;
    private final CzasRepository czasRepository;
    private final DostawcaRepository dostawcaRepository;
    private final MagazynRepository magazynRepository;
    private final ProduktRepository produktRepository;
    private final ProduktWZamowieniuRepository produktWZamowieniuRepository;

    @Autowired
    public ZamowienieService(ZamowienieRepository zamowienieRepository,
                             KlientRepository klientRepository,
                             CzasRepository czasRepository,
                             DostawcaRepository dostawcaRepository,
                             MagazynRepository magazynRepository,
                             ProduktRepository produktRepository,
                             ProduktWZamowieniuRepository produktWZamowieniuRepository) {
        this.zamowienieRepository = zamowienieRepository;
        this.klientRepository = klientRepository;
        this.czasRepository = czasRepository;
        this.dostawcaRepository = dostawcaRepository;
        this.magazynRepository = magazynRepository;
        this.produktRepository = produktRepository;
        this.produktWZamowieniuRepository = produktWZamowieniuRepository;
    }

    public List<ZamowienieResponse> getAllZamowienia() {
        return zamowienieRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ZamowienieResponse getZamowienieById(Integer id) {
        Zamowienie zamowienie = zamowienieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zamowienie with ID " + id + " not found"));
        return mapToResponse(zamowienie);
    }

    public List<ZamowienieResponse> getZamowieniaByKlientId(Integer klientId) {
        return zamowienieRepository.findByKlient_Id(klientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ZamowienieResponse addZamowienie(ZamowienieRequest request) {
        var klient = klientRepository.findById(request.getIdKlient())
                .orElseThrow(() -> new RuntimeException("Klient with ID " + request.getIdKlient() + " not found"));

        var czas = czasRepository.findById(request.getIdCzas())
                .orElseThrow(() -> new RuntimeException("Czas with ID " + request.getIdCzas() + " not found"));

        var dostawca = dostawcaRepository.findById(request.getIdDostawca())
                .orElseThrow(() -> new RuntimeException("Dostawca with ID " + request.getIdDostawca() + " not found"));

        var magazyn = magazynRepository.findById(request.getIdMagazyn())
                .orElseThrow(() -> new RuntimeException("Magazyn with ID " + request.getIdMagazyn() + " not found"));

        Zamowienie zamowienie = new Zamowienie();
        zamowienie.setKlient(klient);
        zamowienie.setCzas(czas);
        zamowienie.setDostawca(dostawca);
        zamowienie.setMagazyn(magazyn);
        zamowienie.setWartoscCalkowita(request.getWartoscCalkowita());
        zamowienie.setStatusPlatnosci(Zamowienie.StatusPlatnosci.PENDING);

        Zamowienie zapisaneZamowienie = zamowienieRepository.save(zamowienie);

        if (request.getProdukty() != null && !request.getProdukty().isEmpty()) {
            List<ProduktWZamowieniu> produktyWZamowieniu = request.getProdukty().stream().map(p -> {
                Produkt produkt = produktRepository.findById(p.getIdProdukt())
                        .orElseThrow(() -> new RuntimeException("Produkt with ID " + p.getIdProdukt() + " not found"));

                ProduktWZamowieniu pwz = new ProduktWZamowieniu();
                pwz.setZamowienie(zapisaneZamowienie);
                pwz.setProdukt(produkt);
                pwz.setIlosc(p.getIlosc());
                pwz.setCena(produkt.getCena());
                pwz.setRabat(0.0);
                pwz.setWartoscLaczna(produkt.getCena() * p.getIlosc());
                return pwz;
            }).collect(Collectors.toList());

            produktWZamowieniuRepository.saveAll(produktyWZamowieniu);
            zapisaneZamowienie.setProduktyWZamowieniu(produktyWZamowieniu);
        }

        return mapToResponse(zapisaneZamowienie);
    }

    public ZamowienieResponse updateZamowienie(Integer id, ZamowienieRequest request) {
        Zamowienie zamowienie = zamowienieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zamowienie with ID " + id + " not found"));

        var klient = klientRepository.findById(request.getIdKlient())
                .orElseThrow(() -> new RuntimeException("Klient with ID " + request.getIdKlient() + " not found"));

        var czas = czasRepository.findById(request.getIdCzas())
                .orElseThrow(() -> new RuntimeException("Czas with ID " + request.getIdCzas() + " not found"));

        var dostawca = dostawcaRepository.findById(request.getIdDostawca())
                .orElseThrow(() -> new RuntimeException("Dostawca with ID " + request.getIdDostawca() + " not found"));

        var magazyn = magazynRepository.findById(request.getIdMagazyn())
                .orElseThrow(() -> new RuntimeException("Magazyn with ID " + request.getIdMagazyn() + " not found"));

        zamowienie.setKlient(klient);
        zamowienie.setCzas(czas);
        zamowienie.setDostawca(dostawca);
        zamowienie.setMagazyn(magazyn);
        zamowienie.setWartoscCalkowita(request.getWartoscCalkowita());

        zamowienieRepository.save(zamowienie);

        produktWZamowieniuRepository.deleteByZamowienie_Id(id);

        List<ProduktWZamowieniu> noweProduktyWZamowieniu = request.getProdukty().stream().map(p -> {
            Produkt produkt = produktRepository.findById(p.getIdProdukt())
                    .orElseThrow(() -> new RuntimeException("Produkt with ID " + p.getIdProdukt() + " not found"));

            ProduktWZamowieniu pwz = new ProduktWZamowieniu();
            pwz.setZamowienie(zamowienie);
            pwz.setProdukt(produkt);
            pwz.setIlosc(p.getIlosc());
            pwz.setCena(produkt.getCena());
            pwz.setRabat(0.0);
            pwz.setWartoscLaczna(produkt.getCena() * p.getIlosc());
            return pwz;
        }).collect(Collectors.toList());

        produktWZamowieniuRepository.saveAll(noweProduktyWZamowieniu);
        zamowienie.setProduktyWZamowieniu(noweProduktyWZamowieniu);

        return mapToResponse(zamowienie);
    }

    public void deleteZamowienie(Integer id) {
        Zamowienie zamowienie = zamowienieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zamowienie with ID " + id + " not found"));
        zamowienieRepository.delete(zamowienie);
    }

    public ZamowienieResponse zatwierdzPlatnoscOffline(Integer id) {
        Zamowienie zamowienie = zamowienieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zamówienie o ID " + id + " nie istnieje"));

        zamowienie.setStatusPlatnosci(Zamowienie.StatusPlatnosci.OFFLINE);
        zamowienieRepository.save(zamowienie);

        return mapToResponse(zamowienie);
    }

    private ZamowienieResponse mapToResponse(Zamowienie zamowienie) {
        List<ProduktWZamowieniuResponse> produktyResponse = zamowienie.getProduktyWZamowieniu() == null
                ? List.of()
                : zamowienie.getProduktyWZamowieniu().stream()
                .map(ProduktWZamowieniuResponse::new)
                .collect(Collectors.toList());

        return new ZamowienieResponse(
                zamowienie.getId(),
                zamowienie.getKlient().getId(),
                zamowienie.getCzas().getId(),
                zamowienie.getDostawca().getId(),
                zamowienie.getMagazyn().getId(),
                zamowienie.getWartoscCalkowita(),
                zamowienie.getStatusPlatnosci().name(),
                produktyResponse
        );
    }
}
