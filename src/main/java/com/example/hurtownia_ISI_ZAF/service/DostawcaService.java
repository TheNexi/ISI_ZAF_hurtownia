package com.example.hurtownia_ISI_ZAF.service;

import com.example.hurtownia_ISI_ZAF.model.Dostawca;
import com.example.hurtownia_ISI_ZAF.repository.DostawcaRepository;
import com.example.hurtownia_ISI_ZAF.request.DostawcaRequest;
import com.example.hurtownia_ISI_ZAF.response.DostawcaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DostawcaService {

    private final DostawcaRepository dostawcaRepository;

    @Autowired
    public DostawcaService(DostawcaRepository dostawcaRepository) {
        this.dostawcaRepository = dostawcaRepository;
    }

    public List<DostawcaResponse> getAllDostawcy() {
        return dostawcaRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public DostawcaResponse getDostawcaById(Integer id) {
        return dostawcaRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Dostawca not found"));
    }

    public DostawcaResponse addDostawca(DostawcaRequest request) {
        Dostawca dostawca = new Dostawca();
        dostawca.setId(null);
        dostawca.setNazwa(request.getNazwa());
        dostawca.setKraj(request.getKraj());
        dostawca.setMiasto(request.getMiasto());
        dostawca.setNip(request.getNip());
        dostawca.setTelefon(request.getTelefon());
        return mapToResponse(dostawcaRepository.save(dostawca));
    }

    public DostawcaResponse updateDostawca(Integer id, DostawcaRequest request) {
        Dostawca dostawca = dostawcaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dostawca o ID " + id + " nie istnieje"));

        dostawca.setNazwa(request.getNazwa());
        dostawca.setKraj(request.getKraj());
        dostawca.setMiasto(request.getMiasto());
        dostawca.setNip(request.getNip());
        dostawca.setTelefon(request.getTelefon());

        return mapToResponse(dostawcaRepository.save(dostawca));
    }

    public void deleteDostawca(Integer id) {
        dostawcaRepository.deleteById(id);
    }

    private DostawcaResponse mapToResponse(Dostawca dostawca) {
        return new DostawcaResponse(
                dostawca.getId(),
                dostawca.getNazwa(),
                dostawca.getKraj(),
                dostawca.getMiasto(),
                dostawca.getNip(),
                dostawca.getTelefon()
        );
    }
}
