package com.example.hurtownia_ISI_ZAF.service;

import com.example.hurtownia_ISI_ZAF.model.Czas;
import com.example.hurtownia_ISI_ZAF.repository.CzasRepository;
import com.example.hurtownia_ISI_ZAF.request.CzasRequest;
import com.example.hurtownia_ISI_ZAF.response.CzasResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CzasServiceTest {

    @Mock
    private CzasRepository czasRepository;

    @InjectMocks
    private CzasService czasService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCzasy() {
        Czas czas1 = new Czas();
        czas1.setId(1);
        czas1.setDzien(10);
        czas1.setMiesiac(5);
        czas1.setRok(2023);
        czas1.setKwartal("II");
        czas1.setDzienTygodnia("Wtorek");

        Czas czas2 = new Czas();
        czas2.setId(2);
        czas2.setDzien(15);
        czas2.setMiesiac(6);
        czas2.setRok(2024);
        czas2.setKwartal("III");
        czas2.setDzienTygodnia("Czwartek");

        when(czasRepository.findAll()).thenReturn(Arrays.asList(czas1, czas2));

        List<CzasResponse> responses = czasService.getAllCzasy();

        assertEquals(2, responses.size());

        assertEquals(1, responses.get(0).getId());
        assertEquals(10, responses.get(0).getDzien());
        assertEquals(5, responses.get(0).getMiesiac());
        assertEquals(2023, responses.get(0).getRok());
        assertEquals("II", responses.get(0).getKwartal());
        assertEquals("Wtorek", responses.get(0).getDzienTygodnia());

        assertEquals(2, responses.get(1).getId());
        assertEquals(15, responses.get(1).getDzien());
        assertEquals(6, responses.get(1).getMiesiac());
        assertEquals(2024, responses.get(1).getRok());
        assertEquals("III", responses.get(1).getKwartal());
        assertEquals("Czwartek", responses.get(1).getDzienTygodnia());
    }

    @Test
    void testGetCzasByIdFound() {
        Czas czas = new Czas();
        czas.setId(1);
        czas.setDzien(10);
        czas.setMiesiac(5);
        czas.setRok(2023);
        czas.setKwartal("II");
        czas.setDzienTygodnia("Wtorek");

        when(czasRepository.findById(1)).thenReturn(Optional.of(czas));

        CzasResponse response = czasService.getCzasById(1);

        assertEquals(1, response.getId());
        assertEquals(10, response.getDzien());
        assertEquals("II", response.getKwartal());
        assertEquals("Wtorek", response.getDzienTygodnia());
    }

    @Test
    void testGetCzasByIdNotFound() {
        when(czasRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> czasService.getCzasById(99));
        assertEquals("Czas not found", ex.getMessage());
    }

    @Test
    void testAddCzas() {
        CzasRequest request = new CzasRequest(10, 5, 2023, "II", "Wtorek");

        Czas czasToSave = new Czas();
        czasToSave.setDzien(request.getDzien());
        czasToSave.setMiesiac(request.getMiesiac());
        czasToSave.setRok(request.getRok());
        czasToSave.setKwartal(request.getKwartal());
        czasToSave.setDzienTygodnia(request.getDzienTygodnia());

        Czas savedCzas = new Czas();
        savedCzas.setId(1);
        savedCzas.setDzien(request.getDzien());
        savedCzas.setMiesiac(request.getMiesiac());
        savedCzas.setRok(request.getRok());
        savedCzas.setKwartal(request.getKwartal());
        savedCzas.setDzienTygodnia(request.getDzienTygodnia());

        when(czasRepository.save(any(Czas.class))).thenReturn(savedCzas);

        CzasResponse response = czasService.addCzas(request);

        assertEquals(1, response.getId());
        assertEquals(request.getDzien(), response.getDzien());
        assertEquals(request.getMiesiac(), response.getMiesiac());
        assertEquals(request.getRok(), response.getRok());
        assertEquals(request.getKwartal(), response.getKwartal());
        assertEquals(request.getDzienTygodnia(), response.getDzienTygodnia());
    }

    @Test
    void testUpdateCzasFound() {
        Czas existingCzas = new Czas();
        existingCzas.setId(1);
        existingCzas.setDzien(1);
        existingCzas.setMiesiac(1);
        existingCzas.setRok(2020);
        existingCzas.setKwartal("I");
        existingCzas.setDzienTygodnia("Poniedziałek");

        CzasRequest updateRequest = new CzasRequest(10, 5, 2023, "II", "Wtorek");

        when(czasRepository.findById(1)).thenReturn(Optional.of(existingCzas));
        when(czasRepository.save(any(Czas.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CzasResponse response = czasService.updateCzas(1, updateRequest);

        assertEquals(1, response.getId());
        assertEquals(updateRequest.getDzien(), response.getDzien());
        assertEquals(updateRequest.getMiesiac(), response.getMiesiac());
        assertEquals(updateRequest.getRok(), response.getRok());
        assertEquals(updateRequest.getKwartal(), response.getKwartal());
        assertEquals(updateRequest.getDzienTygodnia(), response.getDzienTygodnia());
    }

    @Test
    void testUpdateCzasNotFound() {
        when(czasRepository.findById(99)).thenReturn(Optional.empty());

        CzasRequest updateRequest = new CzasRequest(10, 5, 2023, "II", "Wtorek");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> czasService.updateCzas(99, updateRequest));
        assertEquals("Czas o ID 99 nie istnieje", ex.getMessage());
    }

    @Test
    void testDeleteCzas() {
        doNothing().when(czasRepository).deleteById(1);

        czasService.deleteCzas(1);

        verify(czasRepository, times(1)).deleteById(1);
    }
}
