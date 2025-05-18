package com.example.hurtownia_ISI_ZAF.service;

import com.example.hurtownia_ISI_ZAF.model.Magazyn;
import com.example.hurtownia_ISI_ZAF.repository.MagazynRepository;
import com.example.hurtownia_ISI_ZAF.request.MagazynRequest;
import com.example.hurtownia_ISI_ZAF.response.MagazynResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MagazynServiceTest {

    @Mock
    private MagazynRepository magazynRepository;

    @InjectMocks
    private MagazynService magazynService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllMagazyny() {
        Magazyn m1 = new Magazyn();
        m1.setId(1);
        m1.setNazwa("Magazyn A");
        m1.setKraj("Polska");
        m1.setMiasto("Warszawa");
        m1.setUlica("Ulica 1");
        m1.setPojemnosc(1000);

        Magazyn m2 = new Magazyn();
        m2.setId(2);
        m2.setNazwa("Magazyn B");
        m2.setKraj("Niemcy");
        m2.setMiasto("Berlin");
        m2.setUlica("Strasse 5");
        m2.setPojemnosc(2000);

        when(magazynRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

        List<MagazynResponse> responses = magazynService.getAllMagazyny();

        assertEquals(2, responses.size());
        assertEquals("Magazyn A", responses.get(0).getNazwa());
        assertEquals("Berlin", responses.get(1).getMiasto());
    }

    @Test
    void testGetMagazynByIdFound() {
        Magazyn magazyn = new Magazyn();
        magazyn.setId(1);
        magazyn.setNazwa("Magazyn A");
        magazyn.setKraj("Polska");
        magazyn.setMiasto("Warszawa");
        magazyn.setUlica("Ulica 1");
        magazyn.setPojemnosc(1000);

        when(magazynRepository.findById(1)).thenReturn(Optional.of(magazyn));

        MagazynResponse response = magazynService.getMagazynById(1);

        assertEquals(1, response.getId());
        assertEquals("Magazyn A", response.getNazwa());
        assertEquals("Polska", response.getKraj());
        assertEquals(1000, response.getPojemnosc());
    }

    @Test
    void testGetMagazynByIdNotFound() {
        when(magazynRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> magazynService.getMagazynById(99));
        assertEquals("Magazyn not found", ex.getMessage());
    }

    @Test
    void testAddMagazyn() {
        MagazynRequest request = new MagazynRequest(
                "Magazyn C", "Polska", "Kraków", "Ulica 3", 5000
        );

        Magazyn savedMagazyn = new Magazyn();
        savedMagazyn.setId(3);
        savedMagazyn.setNazwa(request.getNazwa());
        savedMagazyn.setKraj(request.getKraj());
        savedMagazyn.setMiasto(request.getMiasto());
        savedMagazyn.setUlica(request.getUlica());
        savedMagazyn.setPojemnosc(request.getPojemnosc());

        when(magazynRepository.save(any(Magazyn.class))).thenReturn(savedMagazyn);

        MagazynResponse response = magazynService.addMagazyn(request);

        assertEquals(3, response.getId());
        assertEquals(request.getNazwa(), response.getNazwa());
        assertEquals(request.getPojemnosc(), response.getPojemnosc());
    }

    @Test
    void testUpdateMagazynFound() {
        Magazyn existingMagazyn = new Magazyn();
        existingMagazyn.setId(1);
        existingMagazyn.setNazwa("Magazyn A");
        existingMagazyn.setKraj("Polska");
        existingMagazyn.setMiasto("Warszawa");
        existingMagazyn.setUlica("Ulica 1");
        existingMagazyn.setPojemnosc(1000);

        MagazynRequest updateRequest = new MagazynRequest(
                "Magazyn Z", "Niemcy", "Berlin", "Strasse 10", 3000
        );

        when(magazynRepository.findById(1)).thenReturn(Optional.of(existingMagazyn));
        when(magazynRepository.save(any(Magazyn.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MagazynResponse response = magazynService.updateMagazyn(1, updateRequest);

        assertEquals(1, response.getId());
        assertEquals(updateRequest.getNazwa(), response.getNazwa());
        assertEquals(updateRequest.getKraj(), response.getKraj());
        assertEquals(updateRequest.getMiasto(), response.getMiasto());
        assertEquals(updateRequest.getUlica(), response.getUlica());
        assertEquals(updateRequest.getPojemnosc(), response.getPojemnosc());
    }

    @Test
    void testUpdateMagazynNotFound() {
        when(magazynRepository.findById(99)).thenReturn(Optional.empty());

        MagazynRequest updateRequest = new MagazynRequest(
                "Magazyn Z", "Niemcy", "Berlin", "Strasse 10", 3000
        );

        RuntimeException ex = assertThrows(RuntimeException.class, () -> magazynService.updateMagazyn(99, updateRequest));
        assertEquals("Magazyn o ID 99 nie istnieje", ex.getMessage());
    }

    @Test
    void testDeleteMagazyn() {
        doNothing().when(magazynRepository).deleteById(1);

        magazynService.deleteMagazyn(1);

        verify(magazynRepository, times(1)).deleteById(1);
    }
}
