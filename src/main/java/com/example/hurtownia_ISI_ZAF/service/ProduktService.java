package com.example.hurtownia_ISI_ZAF.service;

import com.example.hurtownia_ISI_ZAF.model.Produkt;
import com.example.hurtownia_ISI_ZAF.repository.ProduktRepository;
import com.example.hurtownia_ISI_ZAF.request.ProduktRequest;
import com.example.hurtownia_ISI_ZAF.response.ProduktResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProduktService {

    private final ProduktRepository produktRepository;

    @Autowired
    public ProduktService(ProduktRepository produktRepository) {
        this.produktRepository = produktRepository;
    }

    public List<ProduktResponse> getAllProdukty() {
        return produktRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public ProduktResponse getProduktById(Integer id) {
        return produktRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Produkt not found"));
    }

    public ProduktResponse addProdukt(ProduktRequest request) {
        Produkt produkt = new Produkt();
        produkt.setId(null);
        produkt.setNazwa(request.getNazwa());
        produkt.setKategoria(request.getKategoria());
        produkt.setJednostkaMiary(request.getJednostkaMiary());
        produkt.setCena(request.getCena());
        return mapToResponse(produktRepository.save(produkt));
    }

    public ProduktResponse updateProdukt(Integer id, ProduktRequest request) {
        Produkt produkt = produktRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produkt o ID " + id + " nie istnieje"));

        produkt.setNazwa(request.getNazwa());
        produkt.setKategoria(request.getKategoria());
        produkt.setJednostkaMiary(request.getJednostkaMiary());
        produkt.setCena(request.getCena());

        return mapToResponse(produktRepository.save(produkt));
    }

    public void deleteProdukt(Integer id) {
        produktRepository.deleteById(id);
    }

    private ProduktResponse mapToResponse(Produkt produkt) {
        return new ProduktResponse(
                produkt.getId(),
                produkt.getNazwa(),
                produkt.getKategoria(),
                produkt.getJednostkaMiary(),
                produkt.getCena()
        );
    }
}
