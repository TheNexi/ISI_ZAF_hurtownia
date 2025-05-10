package com.example.hurtownia_ISI_ZAF.service;

import com.example.hurtownia_ISI_ZAF.model.Magazyn;
import com.example.hurtownia_ISI_ZAF.repository.MagazynRepository;
import com.example.hurtownia_ISI_ZAF.request.MagazynRequest;
import com.example.hurtownia_ISI_ZAF.response.MagazynResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MagazynService {

    private final MagazynRepository magazynRepository;

    @Autowired
    public MagazynService(MagazynRepository magazynRepository) {
        this.magazynRepository = magazynRepository;
    }

    public List<MagazynResponse> getAllMagazyny() {
        return magazynRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public MagazynResponse getMagazynById(Integer id) {
        return magazynRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Magazyn not found"));
    }

    public MagazynResponse addMagazyn(MagazynRequest request) {
        Magazyn magazyn = new Magazyn();
        magazyn.setId(null);
        magazyn.setNazwa(request.getNazwa());
        magazyn.setKraj(request.getKraj());
        magazyn.setMiasto(request.getMiasto());
        magazyn.setUlica(request.getUlica());
        magazyn.setPojemnosc(request.getPojemnosc());
        return mapToResponse(magazynRepository.save(magazyn));
    }

    public MagazynResponse updateMagazyn(Integer id, MagazynRequest request) {
        Magazyn magazyn = magazynRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Magazyn o ID " + id + " nie istnieje"));

        magazyn.setNazwa(request.getNazwa());
        magazyn.setKraj(request.getKraj());
        magazyn.setMiasto(request.getMiasto());
        magazyn.setUlica(request.getUlica());
        magazyn.setPojemnosc(request.getPojemnosc());

        return mapToResponse(magazynRepository.save(magazyn));
    }

    public void deleteMagazyn(Integer id) {
        magazynRepository.deleteById(id);
    }

    private MagazynResponse mapToResponse(Magazyn magazyn) {
        return new MagazynResponse(
                magazyn.getId(),
                magazyn.getNazwa(),
                magazyn.getKraj(),
                magazyn.getMiasto(),
                magazyn.getUlica(),
                magazyn.getPojemnosc()
        );
    }
}
