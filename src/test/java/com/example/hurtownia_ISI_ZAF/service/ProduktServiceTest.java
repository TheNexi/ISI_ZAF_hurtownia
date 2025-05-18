package com.example.hurtownia_ISI_ZAF.service;

import com.example.hurtownia_ISI_ZAF.model.Produkt;
import com.example.hurtownia_ISI_ZAF.repository.ProduktRepository;
import com.example.hurtownia_ISI_ZAF.request.ProduktRequest;
import com.example.hurtownia_ISI_ZAF.response.ProduktResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProduktServiceTest {

    @Mock
    private ProduktRepository produktRepository;

    @InjectMocks
    private ProduktService produktService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProdukty() {
        Produkt p1 = new Produkt();
        p1.setId(1);
        p1.setNazwa("Produkt A");
        p1.setKategoria("Kategoria 1");
        p1.setJednostkaMiary("szt.");
        p1.setCena(10.50);

        Produkt p2 = new Produkt();
        p2.setId(2);
        p2.setNazwa("Produkt B");
        p2.setKategoria("Kategoria 2");
        p2.setJednostkaMiary("kg");
        p2.setCena(25.00);

        when(produktRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<ProduktResponse> responses = produktService.getAllProdukty();

        assertEquals(2, responses.size());
        assertEquals("Produkt A", responses.get(0).getNazwa());
        assertEquals("kg", responses.get(1).getJednostkaMiary());
        assertEquals(10.50, responses.get(0).getCena());
    }

    @Test
    void testGetProduktByIdFound() {
        Produkt produkt = new Produkt();
        produkt.setId(1);
        produkt.setNazwa("Produkt A");
        produkt.setKategoria("Kategoria 1");
        produkt.setJednostkaMiary("szt.");
        produkt.setCena(10.50);

        when(produktRepository.findById(1)).thenReturn(Optional.of(produkt));

        ProduktResponse response = produktService.getProduktById(1);

        assertEquals(1, response.getId());
        assertEquals("Produkt A", response.getNazwa());
        assertEquals("Kategoria 1", response.getKategoria());
        assertEquals("szt.", response.getJednostkaMiary());
        assertEquals(10.50, response.getCena());
    }

    @Test
    void testGetProduktByIdNotFound() {
        when(produktRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> produktService.getProduktById(99));
        assertEquals("Produkt not found", ex.getMessage());
    }

    @Test
    void testAddProdukt() {
        ProduktRequest request = new ProduktRequest(
                "Produkt C", "Kategoria 3", "litr", 15.00
        );

        Produkt savedProdukt = new Produkt();
        savedProdukt.setId(3);
        savedProdukt.setNazwa(request.getNazwa());
        savedProdukt.setKategoria(request.getKategoria());
        savedProdukt.setJednostkaMiary(request.getJednostkaMiary());
        savedProdukt.setCena(request.getCena());

        when(produktRepository.save(any(Produkt.class))).thenReturn(savedProdukt);

        ProduktResponse response = produktService.addProdukt(request);

        assertEquals(3, response.getId());
        assertEquals(request.getNazwa(), response.getNazwa());
        assertEquals(request.getCena(), response.getCena());
    }

    @Test
    void testUpdateProduktFound() {
        Produkt existingProdukt = new Produkt();
        existingProdukt.setId(1);
        existingProdukt.setNazwa("Produkt A");
        existingProdukt.setKategoria("Kategoria 1");
        existingProdukt.setJednostkaMiary("szt.");
        existingProdukt.setCena(10.50);

        ProduktRequest updateRequest = new ProduktRequest(
                "Produkt Z", "Kategoria X", "kg", 20.00
        );

        when(produktRepository.findById(1)).thenReturn(Optional.of(existingProdukt));
        when(produktRepository.save(any(Produkt.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProduktResponse response = produktService.updateProdukt(1, updateRequest);

        assertEquals(1, response.getId());
        assertEquals(updateRequest.getNazwa(), response.getNazwa());
        assertEquals(updateRequest.getKategoria(), response.getKategoria());
        assertEquals(updateRequest.getJednostkaMiary(), response.getJednostkaMiary());
        assertEquals(updateRequest.getCena(), response.getCena());
    }

    @Test
    void testUpdateProduktNotFound() {
        when(produktRepository.findById(99)).thenReturn(Optional.empty());

        ProduktRequest updateRequest = new ProduktRequest(
                "Produkt Z", "Kategoria X", "kg", 20.00
        );

        RuntimeException ex = assertThrows(RuntimeException.class, () -> produktService.updateProdukt(99, updateRequest));
        assertEquals("Produkt o ID 99 nie istnieje", ex.getMessage());
    }

    @Test
    void testDeleteProdukt() {
        doNothing().when(produktRepository).deleteById(1);

        produktService.deleteProdukt(1);

        verify(produktRepository, times(1)).deleteById(1);
    }
}
