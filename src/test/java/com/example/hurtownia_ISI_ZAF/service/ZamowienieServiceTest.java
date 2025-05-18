package com.example.hurtownia_ISI_ZAF.service;

import com.example.hurtownia_ISI_ZAF.model.*;
import com.example.hurtownia_ISI_ZAF.repository.*;
import com.example.hurtownia_ISI_ZAF.request.ZamowienieRequest;
import com.example.hurtownia_ISI_ZAF.response.ZamowienieResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ZamowienieServiceTest {

    @Mock
    private ZamowienieRepository zamowienieRepository;
    @Mock
    private KlientRepository klientRepository;
    @Mock
    private CzasRepository czasRepository;
    @Mock
    private DostawcaRepository dostawcaRepository;
    @Mock
    private MagazynRepository magazynRepository;

    @InjectMocks
    private ZamowienieService zamowienieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllZamowienia() {
        Zamowienie z1 = new Zamowienie();
        z1.setId(1);

        Klient klient1 = new Klient();
        klient1.setId(1);
        z1.setKlient(klient1);

        Czas czas1 = new Czas();
        czas1.setId(1);
        z1.setCzas(czas1);

        Dostawca dostawca1 = new Dostawca();
        dostawca1.setId(1);
        z1.setDostawca(dostawca1);

        Magazyn magazyn1 = new Magazyn();
        magazyn1.setId(1);
        z1.setMagazyn(magazyn1);

        z1.setWartoscCalkowita(100.0);

        Zamowienie z2 = new Zamowienie();
        z2.setId(2);

        Klient klient2 = new Klient();
        klient2.setId(2);
        z2.setKlient(klient2);

        Czas czas2 = new Czas();
        czas2.setId(2);
        z2.setCzas(czas2);

        Dostawca dostawca2 = new Dostawca();
        dostawca2.setId(2);
        z2.setDostawca(dostawca2);

        Magazyn magazyn2 = new Magazyn();
        magazyn2.setId(2);
        z2.setMagazyn(magazyn2);

        z2.setWartoscCalkowita(200.0);

        when(zamowienieRepository.findAll()).thenReturn(Arrays.asList(z1, z2));

        List<ZamowienieResponse> responses = zamowienieService.getAllZamowienia();

        assertEquals(2, responses.size());
        assertEquals(1, responses.get(0).getId());
        assertEquals(100.0, responses.get(0).getWartoscCalkowita());
        assertEquals(2, responses.get(1).getId());
        assertEquals(200.0, responses.get(1).getWartoscCalkowita());
    }

    @Test
    void testGetZamowienieByIdFound() {
        Zamowienie zamowienie = new Zamowienie();
        zamowienie.setId(1);

        Klient klient = new Klient();
        klient.setId(1);
        zamowienie.setKlient(klient);

        Czas czas = new Czas();
        czas.setId(1);
        zamowienie.setCzas(czas);

        Dostawca dostawca = new Dostawca();
        dostawca.setId(1);
        zamowienie.setDostawca(dostawca);

        Magazyn magazyn = new Magazyn();
        magazyn.setId(1);
        zamowienie.setMagazyn(magazyn);

        zamowienie.setWartoscCalkowita(150.0);

        when(zamowienieRepository.findById(1)).thenReturn(Optional.of(zamowienie));

        ZamowienieResponse response = zamowienieService.getZamowienieById(1);

        assertEquals(1, response.getId());
        assertEquals(1, response.getIdKlient());
        assertEquals(150.0, response.getWartoscCalkowita());
    }

    @Test
    void testGetZamowienieByIdNotFound() {
        when(zamowienieRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> zamowienieService.getZamowienieById(99));

        assertEquals("Zamowienie with ID 99 not found", ex.getMessage());
    }

    @Test
    void testAddZamowienie() {
        ZamowienieRequest request = new ZamowienieRequest();
        request.setIdKlient(1);
        request.setIdCzas(1);
        request.setIdDostawca(1);
        request.setIdMagazyn(1);
        request.setWartoscCalkowita(250.0);

        Klient klient = new Klient();
        klient.setId(1);

        Czas czas = new Czas();
        czas.setId(1);

        Dostawca dostawca = new Dostawca();
        dostawca.setId(1);

        Magazyn magazyn = new Magazyn();
        magazyn.setId(1);

        when(klientRepository.findById(1)).thenReturn(Optional.of(klient));
        when(czasRepository.findById(1)).thenReturn(Optional.of(czas));
        when(dostawcaRepository.findById(1)).thenReturn(Optional.of(dostawca));
        when(magazynRepository.findById(1)).thenReturn(Optional.of(magazyn));

        when(zamowienieRepository.save(any(Zamowienie.class))).thenAnswer(invocation -> {
            Zamowienie z = invocation.getArgument(0);
            z.setId(10);
            return z;
        });

        ZamowienieResponse response = zamowienieService.addZamowienie(request);

        assertEquals(10, response.getId());
        assertEquals(1, response.getIdKlient());
        assertEquals(250.0, response.getWartoscCalkowita());
    }

    @Test
    void testUpdateZamowienieFound() {
        Zamowienie existing = new Zamowienie();
        existing.setId(5);

        Klient klientExisting = new Klient();
        klientExisting.setId(5);
        existing.setKlient(klientExisting);

        Czas czasExisting = new Czas();
        czasExisting.setId(5);
        existing.setCzas(czasExisting);

        Dostawca dostawcaExisting = new Dostawca();
        dostawcaExisting.setId(5);
        existing.setDostawca(dostawcaExisting);

        Magazyn magazynExisting = new Magazyn();
        magazynExisting.setId(5);
        existing.setMagazyn(magazynExisting);

        existing.setWartoscCalkowita(300.0);

        ZamowienieRequest request = new ZamowienieRequest();
        request.setIdKlient(6);
        request.setIdCzas(6);
        request.setIdDostawca(6);
        request.setIdMagazyn(6);
        request.setWartoscCalkowita(400.0);

        Klient klient = new Klient();
        klient.setId(6);

        Czas czas = new Czas();
        czas.setId(6);

        Dostawca dostawca = new Dostawca();
        dostawca.setId(6);

        Magazyn magazyn = new Magazyn();
        magazyn.setId(6);

        when(zamowienieRepository.findById(5)).thenReturn(Optional.of(existing));
        when(klientRepository.findById(6)).thenReturn(Optional.of(klient));
        when(czasRepository.findById(6)).thenReturn(Optional.of(czas));
        when(dostawcaRepository.findById(6)).thenReturn(Optional.of(dostawca));
        when(magazynRepository.findById(6)).thenReturn(Optional.of(magazyn));
        when(zamowienieRepository.save(any(Zamowienie.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ZamowienieResponse response = zamowienieService.updateZamowienie(5, request);

        assertEquals(5, response.getId());
        assertEquals(6, response.getIdKlient());
        assertEquals(400.0, response.getWartoscCalkowita());
    }

    @Test
    void testDeleteZamowienie() {
        Zamowienie zamowienie = new Zamowienie();
        zamowienie.setId(3);

        when(zamowienieRepository.findById(3)).thenReturn(Optional.of(zamowienie));
        doNothing().when(zamowienieRepository).delete(zamowienie);

        zamowienieService.deleteZamowienie(3);

        verify(zamowienieRepository, times(1)).delete(zamowienie);
    }

    @Test
    void testDeleteZamowienieNotFound() {
        when(zamowienieRepository.findById(100)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> zamowienieService.deleteZamowienie(100));
        assertEquals("Zamowienie with ID 100 not found", ex.getMessage());
    }
}
