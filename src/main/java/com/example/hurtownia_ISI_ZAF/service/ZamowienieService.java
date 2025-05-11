package com.example.hurtownia_ISI_ZAF.service;

import com.example.hurtownia_ISI_ZAF.model.Zamowienie;
import com.example.hurtownia_ISI_ZAF.repository.ZamowienieRepository;
import com.example.hurtownia_ISI_ZAF.repository.KlientRepository;
import com.example.hurtownia_ISI_ZAF.repository.CzasRepository;
import com.example.hurtownia_ISI_ZAF.repository.DostawcaRepository;
import com.example.hurtownia_ISI_ZAF.repository.MagazynRepository;
import com.example.hurtownia_ISI_ZAF.request.ZamowienieRequest;
import com.example.hurtownia_ISI_ZAF.response.ZamowienieResponse;
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

    @Autowired
    public ZamowienieService(ZamowienieRepository zamowienieRepository,
                             KlientRepository klientRepository,
                             CzasRepository czasRepository,
                             DostawcaRepository dostawcaRepository,
                             MagazynRepository magazynRepository) {
        this.zamowienieRepository = zamowienieRepository;
        this.klientRepository = klientRepository;
        this.czasRepository = czasRepository;
        this.dostawcaRepository = dostawcaRepository;
        this.magazynRepository = magazynRepository;
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

        zamowienieRepository.save(zamowienie);

        return mapToResponse(zamowienie);
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

        return mapToResponse(zamowienieRepository.save(zamowienie));
    }

    public void deleteZamowienie(Integer id) {
        Zamowienie zamowienie = zamowienieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zamowienie with ID " + id + " not found"));
        zamowienieRepository.delete(zamowienie);
    }

    private ZamowienieResponse mapToResponse(Zamowienie zamowienie) {
        return new ZamowienieResponse(
                zamowienie.getId(),
                zamowienie.getKlient().getId(),
                zamowienie.getCzas().getId(),
                zamowienie.getDostawca().getId(),
                zamowienie.getMagazyn().getId(),
                zamowienie.getWartoscCalkowita()
        );
    }
}
