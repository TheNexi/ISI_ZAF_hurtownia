package com.example.hurtownia_ISI_ZAF.service;

import com.example.hurtownia_ISI_ZAF.model.Klient;
import com.example.hurtownia_ISI_ZAF.repository.KlientRepository;
import com.example.hurtownia_ISI_ZAF.request.KlientRequest;
import com.example.hurtownia_ISI_ZAF.response.KlientResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KlientServiceTest {

    @Mock
    private KlientRepository klientRepository;

    @InjectMocks
    private KlientService klientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllKlienci() {
        Klient klient1 = new Klient();
        klient1.setId(1);
        klient1.setNazwa("Firma A");
        klient1.setNip("1234567890");
        klient1.setKraj("Polska");
        klient1.setMiasto("Warszawa");
        klient1.setUlica("Ul. Kwiatowa 10");
        klient1.setKodPocztowy("00-001");

        Klient klient2 = new Klient();
        klient2.setId(2);
        klient2.setNazwa("Firma B");
        klient2.setNip("0987654321");
        klient2.setKraj("Polska");
        klient2.setMiasto("Kraków");
        klient2.setUlica("Ul. Leśna 5");
        klient2.setKodPocztowy("30-002");

        when(klientRepository.findAll()).thenReturn(Arrays.asList(klient1, klient2));

        List<KlientResponse> responses = klientService.getAllKlienci();

        assertEquals(2, responses.size());

        assertEquals(1, responses.get(0).getId());
        assertEquals("Firma A", responses.get(0).getNazwa());
        assertEquals("1234567890", responses.get(0).getNip());

        assertEquals(2, responses.get(1).getId());
        assertEquals("Firma B", responses.get(1).getNazwa());
    }

    @Test
    void testGetKlientByIdFound() {
        Klient klient = new Klient();
        klient.setId(1);
        klient.setNazwa("Firma A");
        klient.setNip("1234567890");
        klient.setKraj("Polska");
        klient.setMiasto("Warszawa");
        klient.setUlica("Ul. Kwiatowa 10");
        klient.setKodPocztowy("00-001");

        when(klientRepository.findById(1)).thenReturn(Optional.of(klient));

        KlientResponse response = klientService.getKlientById(1);

        assertEquals(1, response.getId());
        assertEquals("Firma A", response.getNazwa());
        assertEquals("1234567890", response.getNip());
    }

    @Test
    void testGetKlientByIdNotFound() {
        when(klientRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> klientService.getKlientById(99));
        assertEquals("Klient not found", ex.getMessage());
    }

    @Test
    void testAddKlient() {
        KlientRequest request = new KlientRequest(
                "Firma C", "1112223334", "Polska", "Gdańsk", "Ul. Morska 20", "80-001"
        );

        Klient klientToSave = new Klient();
        klientToSave.setNazwa(request.getNazwa());
        klientToSave.setNip(request.getNip());
        klientToSave.setKraj(request.getKraj());
        klientToSave.setMiasto(request.getMiasto());
        klientToSave.setUlica(request.getUlica());
        klientToSave.setKodPocztowy(request.getKodPocztowy());

        Klient savedKlient = new Klient();
        savedKlient.setId(3);
        savedKlient.setNazwa(request.getNazwa());
        savedKlient.setNip(request.getNip());
        savedKlient.setKraj(request.getKraj());
        savedKlient.setMiasto(request.getMiasto());
        savedKlient.setUlica(request.getUlica());
        savedKlient.setKodPocztowy(request.getKodPocztowy());

        when(klientRepository.save(any(Klient.class))).thenReturn(savedKlient);

        KlientResponse response = klientService.addKlient(request);

        assertEquals(3, response.getId());
        assertEquals(request.getNazwa(), response.getNazwa());
        assertEquals(request.getNip(), response.getNip());
        assertEquals(request.getKraj(), response.getKraj());
        assertEquals(request.getMiasto(), response.getMiasto());
        assertEquals(request.getUlica(), response.getUlica());
        assertEquals(request.getKodPocztowy(), response.getKodPocztowy());
    }

    @Test
    void testUpdateKlientFound() {
        Klient existingKlient = new Klient();
        existingKlient.setId(1);
        existingKlient.setNazwa("Firma A");
        existingKlient.setNip("1234567890");
        existingKlient.setKraj("Polska");
        existingKlient.setMiasto("Warszawa");
        existingKlient.setUlica("Ul. Kwiatowa 10");
        existingKlient.setKodPocztowy("00-001");

        KlientRequest updateRequest = new KlientRequest(
                "Firma Z", "9998887776", "Polska", "Poznań", "Ul. Zielona 12", "60-100"
        );

        when(klientRepository.findById(1)).thenReturn(Optional.of(existingKlient));
        when(klientRepository.save(any(Klient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        KlientResponse response = klientService.updateKlient(1, updateRequest);

        assertEquals(1, response.getId());
        assertEquals(updateRequest.getNazwa(), response.getNazwa());
        assertEquals(updateRequest.getNip(), response.getNip());
        assertEquals(updateRequest.getKraj(), response.getKraj());
        assertEquals(updateRequest.getMiasto(), response.getMiasto());
        assertEquals(updateRequest.getUlica(), response.getUlica());
        assertEquals(updateRequest.getKodPocztowy(), response.getKodPocztowy());
    }

    @Test
    void testUpdateKlientNotFound() {
        when(klientRepository.findById(99)).thenReturn(Optional.empty());

        KlientRequest updateRequest = new KlientRequest(
                "Firma Z", "9998887776", "Polska", "Poznań", "Ul. Zielona 12", "60-100"
        );

        RuntimeException ex = assertThrows(RuntimeException.class, () -> klientService.updateKlient(99, updateRequest));
        assertEquals("Klient o ID 99 nie istnieje", ex.getMessage());
    }

    @Test
    void testDeleteKlient() {
        doNothing().when(klientRepository).deleteById(1);

        klientService.deleteKlient(1);

        verify(klientRepository, times(1)).deleteById(1);
    }
}
