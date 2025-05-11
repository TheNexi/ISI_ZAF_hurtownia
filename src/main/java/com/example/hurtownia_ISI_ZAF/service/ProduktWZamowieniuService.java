package com.example.hurtownia_ISI_ZAF.service;

import com.example.hurtownia_ISI_ZAF.model.*;
import com.example.hurtownia_ISI_ZAF.repository.ProduktWZamowieniuRepository;
import com.example.hurtownia_ISI_ZAF.request.ProduktWZamowieniuRequest;
import com.example.hurtownia_ISI_ZAF.response.ProduktWZamowieniuResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProduktWZamowieniuService {

    private final ProduktWZamowieniuRepository repository;

    @Autowired
    public ProduktWZamowieniuService(ProduktWZamowieniuRepository repository) {
        this.repository = repository;
    }

    public List<ProduktWZamowieniuResponse> getAll() {
        return repository.findAll().stream()
                .map(ProduktWZamowieniuResponse::new)
                .collect(Collectors.toList());
    }

    public ProduktWZamowieniuResponse getById(Integer id) {
        return repository.findById(id)
                .map(ProduktWZamowieniuResponse::new)
                .orElseThrow(() -> new RuntimeException("Pozycja nie znaleziona"));
    }

    public ProduktWZamowieniuResponse add(ProduktWZamowieniuRequest request) {
        ProduktWZamowieniu entity = mapToEntity(request);
        return new ProduktWZamowieniuResponse(repository.save(entity));
    }

    public ProduktWZamowieniuResponse update(Integer id, ProduktWZamowieniuRequest request) {
        ProduktWZamowieniu entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pozycja nie znaleziona"));
        ProduktWZamowieniu updated = mapToEntity(request);
        updated.setId(id);
        return new ProduktWZamowieniuResponse(repository.save(updated));
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    private ProduktWZamowieniu mapToEntity(ProduktWZamowieniuRequest request) {
        ProduktWZamowieniu produkt = new ProduktWZamowieniu();

        Zamowienie zamowienie = new Zamowienie();
        zamowienie.setId(request.getIdZamowienie());

        Produkt produktModel = new Produkt();
        produktModel.setId(request.getIdProdukt());

        produkt.setZamowienie(zamowienie);
        produkt.setProdukt(produktModel);
        produkt.setIlosc(request.getIlosc());
        produkt.setCena(request.getCena());
        produkt.setRabat(request.getRabat());
        produkt.setWartoscLaczna(request.getWartoscLaczna());

        return produkt;
    }
}
