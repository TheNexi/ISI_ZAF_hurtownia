package com.example.hurtownia_ISI_ZAF.service;

import com.example.hurtownia_ISI_ZAF.model.*;
import com.example.hurtownia_ISI_ZAF.repository.ProduktWZamowieniuRepository;
import com.example.hurtownia_ISI_ZAF.request.ProduktWZamowieniuRequest;
import com.example.hurtownia_ISI_ZAF.response.ProduktWZamowieniuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProduktWZamowieniuServiceTest {

    @Mock
    private ProduktWZamowieniuRepository repository;

    @InjectMocks
    private ProduktWZamowieniuService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        ProduktWZamowieniu p1 = new ProduktWZamowieniu();
        p1.setId(1);
        Zamowienie zam1 = new Zamowienie();
        zam1.setId(10);
        p1.setZamowienie(zam1);
        Produkt prod1 = new Produkt();
        prod1.setId(100);
        p1.setProdukt(prod1);

        ProduktWZamowieniu p2 = new ProduktWZamowieniu();
        p2.setId(2);
        Zamowienie zam2 = new Zamowienie();
        zam2.setId(20);
        p2.setZamowienie(zam2);
        Produkt prod2 = new Produkt();
        prod2.setId(200);
        p2.setProdukt(prod2);

        when(repository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<ProduktWZamowieniuResponse> responses = service.getAll();

        assertEquals(2, responses.size());

        ProduktWZamowieniuResponse r1 = responses.get(0);
        assertEquals(1, r1.getId());
        assertEquals(10, r1.getIdZamowienie());
        assertEquals(100, r1.getIdProdukt());

        ProduktWZamowieniuResponse r2 = responses.get(1);
        assertEquals(2, r2.getId());
        assertEquals(20, r2.getIdZamowienie());
        assertEquals(200, r2.getIdProdukt());
    }

    @Test
    void testGetByIdFound() {
        ProduktWZamowieniu produkt = new ProduktWZamowieniu();
        produkt.setId(1);
        Zamowienie zamowienie = new Zamowienie();
        zamowienie.setId(10);
        produkt.setZamowienie(zamowienie);
        Produkt produktModel = new Produkt();
        produktModel.setId(20);
        produkt.setProdukt(produktModel);

        when(repository.findById(1)).thenReturn(Optional.of(produkt));

        ProduktWZamowieniuResponse response = service.getById(1);

        assertEquals(1, response.getId());
        assertEquals(10, response.getIdZamowienie());
        assertEquals(20, response.getIdProdukt());
    }

    @Test
    void testGetByIdNotFound() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.getById(99));
        assertEquals("Pozycja nie znaleziona", ex.getMessage());
    }

    @Test
    void testAdd() {
        ProduktWZamowieniuRequest request = new ProduktWZamowieniuRequest();
        request.setIdZamowienie(10);
        request.setIdProdukt(20);
        request.setIlosc(5);
        request.setCena(100.0);
        request.setRabat(0.1);
        request.setWartoscLaczna(450.0);

        ProduktWZamowieniu savedEntity = new ProduktWZamowieniu();
        savedEntity.setId(1);
        savedEntity.setIlosc(request.getIlosc());
        savedEntity.setCena(request.getCena());
        savedEntity.setRabat(request.getRabat());
        savedEntity.setWartoscLaczna(request.getWartoscLaczna());

        Zamowienie zamowienie = new Zamowienie();
        zamowienie.setId(request.getIdZamowienie());
        savedEntity.setZamowienie(zamowienie);

        Produkt produktModel = new Produkt();
        produktModel.setId(request.getIdProdukt());
        savedEntity.setProdukt(produktModel);

        when(repository.save(any(ProduktWZamowieniu.class))).thenReturn(savedEntity);

        ProduktWZamowieniuResponse response = service.add(request);

        assertEquals(1, response.getId());
        assertEquals(5, response.getIlosc());
        assertEquals(100.0, response.getCena());
        assertEquals(0.1, response.getRabat());
        assertEquals(450.0, response.getWartoscLaczna());
        assertEquals(10, response.getIdZamowienie());
        assertEquals(20, response.getIdProdukt());
    }

    @Test
    void testUpdateFound() {
        ProduktWZamowieniu existing = new ProduktWZamowieniu();
        existing.setId(1);
        existing.setIlosc(2);

        ProduktWZamowieniuRequest request = new ProduktWZamowieniuRequest();
        request.setIdZamowienie(10);
        request.setIdProdukt(20);
        request.setIlosc(10);
        request.setCena(200.0);
        request.setRabat(0.2);
        request.setWartoscLaczna(1600.0);

        when(repository.findById(1)).thenReturn(Optional.of(existing));
        when(repository.save(any(ProduktWZamowieniu.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProduktWZamowieniuResponse response = service.update(1, request);

        assertEquals(1, response.getId());
        assertEquals(10, response.getIlosc());
        assertEquals(200.0, response.getCena());
        assertEquals(0.2, response.getRabat());
        assertEquals(1600.0, response.getWartoscLaczna());
        assertEquals(10, response.getIdZamowienie());
        assertEquals(20, response.getIdProdukt());
    }

    @Test
    void testUpdateNotFound() {
        ProduktWZamowieniuRequest request = new ProduktWZamowieniuRequest();

        when(repository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.update(99, request));
        assertEquals("Pozycja nie znaleziona", ex.getMessage());
    }

    @Test
    void testDelete() {
        doNothing().when(repository).deleteById(1);

        service.delete(1);

        verify(repository, times(1)).deleteById(1);
    }
}
