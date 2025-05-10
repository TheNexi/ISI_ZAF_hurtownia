package com.example.hurtownia_ISI_ZAF.service;

import com.example.hurtownia_ISI_ZAF.model.Klient;
import com.example.hurtownia_ISI_ZAF.repository.KlientRepository;
import com.example.hurtownia_ISI_ZAF.request.KlientRequest;
import com.example.hurtownia_ISI_ZAF.response.KlientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KlientService {

    private final KlientRepository klientRepository;

    @Autowired
    public KlientService(KlientRepository klientRepository) {
        this.klientRepository = klientRepository;
    }

    public List<KlientResponse> getAllKlienci() {
        return klientRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public KlientResponse getKlientById(Integer id) {
        return klientRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Klient not found"));
    }

    public KlientResponse addKlient(KlientRequest request) {
        Klient klient = new Klient();
        klient.setId(null);
        klient.setNazwa(request.getNazwa());
        klient.setNip(request.getNip());
        klient.setKraj(request.getKraj());
        klient.setMiasto(request.getMiasto());
        klient.setUlica(request.getUlica());
        klient.setKodPocztowy(request.getKodPocztowy());
        return mapToResponse(klientRepository.save(klient));
    }

    public KlientResponse updateKlient(Integer id, KlientRequest request) {
        Klient klient = klientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Klient o ID " + id + " nie istnieje"));

        klient.setNazwa(request.getNazwa());
        klient.setNip(request.getNip());
        klient.setKraj(request.getKraj());
        klient.setMiasto(request.getMiasto());
        klient.setUlica(request.getUlica());
        klient.setKodPocztowy(request.getKodPocztowy());

        return mapToResponse(klientRepository.save(klient));
    }

    public void deleteKlient(Integer id) {
        klientRepository.deleteById(id);
    }

    private KlientResponse mapToResponse(Klient klient) {
        return new KlientResponse(
                klient.getId(),
                klient.getNazwa(),
                klient.getNip(),
                klient.getKraj(),
                klient.getMiasto(),
                klient.getUlica(),
                klient.getKodPocztowy()
        );
    }
}
