package com.example.hurtownia_ISI_ZAF.service;

import com.example.hurtownia_ISI_ZAF.model.Czas;
import com.example.hurtownia_ISI_ZAF.repository.CzasRepository;
import com.example.hurtownia_ISI_ZAF.request.CzasRequest;
import com.example.hurtownia_ISI_ZAF.response.CzasResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CzasService {

    private final CzasRepository czasRepository;

    @Autowired
    public CzasService(CzasRepository czasRepository) {
        this.czasRepository = czasRepository;
    }

    public List<CzasResponse> getAllCzasy() {
        return czasRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CzasResponse getCzasById(Integer id) {
        return czasRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Czas not found"));
    }

    public CzasResponse addCzas(CzasRequest request) {
        Czas czas = new Czas();
        czas.setId(null);
        czas.setDzien(request.getDzien());
        czas.setMiesiac(request.getMiesiac());
        czas.setRok(request.getRok());
        czas.setKwartal(request.getKwartal());
        czas.setDzienTygodnia(request.getDzienTygodnia());
        return mapToResponse(czasRepository.save(czas));
    }

    public CzasResponse updateCzas(Integer id, CzasRequest request) {
        Czas czas = czasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Czas o ID " + id + " nie istnieje"));

        czas.setDzien(request.getDzien());
        czas.setMiesiac(request.getMiesiac());
        czas.setRok(request.getRok());
        czas.setKwartal(request.getKwartal());
        czas.setDzienTygodnia(request.getDzienTygodnia());

        return mapToResponse(czasRepository.save(czas));
    }

    public void deleteCzas(Integer id) {
        czasRepository.deleteById(id);
    }

    private CzasResponse mapToResponse(Czas czas) {
        return new CzasResponse(
                czas.getId(),
                czas.getDzien(),
                czas.getMiesiac(),
                czas.getRok(),
                czas.getKwartal(),
                czas.getDzienTygodnia()
        );
    }
}
