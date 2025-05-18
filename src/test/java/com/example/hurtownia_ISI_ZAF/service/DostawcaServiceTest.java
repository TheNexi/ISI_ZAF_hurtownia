package com.example.hurtownia_ISI_ZAF.service;

import com.example.hurtownia_ISI_ZAF.model.Dostawca;
import com.example.hurtownia_ISI_ZAF.repository.DostawcaRepository;
import com.example.hurtownia_ISI_ZAF.request.DostawcaRequest;
import com.example.hurtownia_ISI_ZAF.response.DostawcaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DostawcaServiceTest {

    @Mock
    private DostawcaRepository dostawcaRepository;

    @InjectMocks
    private DostawcaService dostawcaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllDostawcy() {
        Dostawca dostawca1 = new Dostawca();
        dostawca1.setId(1);
        dostawca1.setNazwa("Dostawca A");
        dostawca1.setKraj("Polska");
        dostawca1.setMiasto("Warszawa");
        dostawca1.setNip("1234567890");
        dostawca1.setTelefon("111-222-333");

        Dostawca dostawca2 = new Dostawca();
        dostawca2.setId(2);
        dostawca2.setNazwa("Dostawca B");
        dostawca2.setKraj("Niemcy");
        dostawca2.setMiasto("Berlin");
        dostawca2.setNip("0987654321");
        dostawca2.setTelefon("444-555-666");

        when(dostawcaRepository.findAll()).thenReturn(Arrays.asList(dostawca1, dostawca2));

        List<DostawcaResponse> responses = dostawcaService.getAllDostawcy();

        assertEquals(2, responses.size());

        assertEquals(1, responses.get(0).getId());
        assertEquals("Dostawca A", responses.get(0).getNazwa());
        assertEquals("Polska", responses.get(0).getKraj());

        assertEquals(2, responses.get(1).getId());
        assertEquals("Dostawca B", responses.get(1).getNazwa());
    }

    @Test
    void testGetDostawcaByIdFound() {
        Dostawca dostawca = new Dostawca();
        dostawca.setId(1);
        dostawca.setNazwa("Dostawca A");
        dostawca.setKraj("Polska");
        dostawca.setMiasto("Warszawa");
        dostawca.setNip("1234567890");
        dostawca.setTelefon("111-222-333");

        when(dostawcaRepository.findById(1)).thenReturn(Optional.of(dostawca));

        DostawcaResponse response = dostawcaService.getDostawcaById(1);

        assertEquals(1, response.getId());
        assertEquals("Dostawca A", response.getNazwa());
        assertEquals("Polska", response.getKraj());
        assertEquals("111-222-333", response.getTelefon());
    }

    @Test
    void testGetDostawcaByIdNotFound() {
        when(dostawcaRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> dostawcaService.getDostawcaById(99));
        assertEquals("Dostawca not found", ex.getMessage());
    }

    @Test
    void testAddDostawca() {
        DostawcaRequest request = new DostawcaRequest(
                "Dostawca C", "Polska", "Kraków", "1112223334", "777-888-999"
        );

        Dostawca savedDostawca = new Dostawca();
        savedDostawca.setId(3);
        savedDostawca.setNazwa(request.getNazwa());
        savedDostawca.setKraj(request.getKraj());
        savedDostawca.setMiasto(request.getMiasto());
        savedDostawca.setNip(request.getNip());
        savedDostawca.setTelefon(request.getTelefon());

        when(dostawcaRepository.save(any(Dostawca.class))).thenReturn(savedDostawca);

        DostawcaResponse response = dostawcaService.addDostawca(request);

        assertEquals(3, response.getId());
        assertEquals(request.getNazwa(), response.getNazwa());
        assertEquals(request.getKraj(), response.getKraj());
        assertEquals(request.getMiasto(), response.getMiasto());
        assertEquals(request.getNip(), response.getNip());
        assertEquals(request.getTelefon(), response.getTelefon());
    }

    @Test
    void testUpdateDostawcaFound() {
        Dostawca existingDostawca = new Dostawca();
        existingDostawca.setId(1);
        existingDostawca.setNazwa("Dostawca A");
        existingDostawca.setKraj("Polska");
        existingDostawca.setMiasto("Warszawa");
        existingDostawca.setNip("1234567890");
        existingDostawca.setTelefon("111-222-333");

        DostawcaRequest updateRequest = new DostawcaRequest(
                "Dostawca Z", "Niemcy", "Berlin", "9998887776", "555-666-777"
        );

        when(dostawcaRepository.findById(1)).thenReturn(Optional.of(existingDostawca));
        when(dostawcaRepository.save(any(Dostawca.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DostawcaResponse response = dostawcaService.updateDostawca(1, updateRequest);

        assertEquals(1, response.getId());
        assertEquals(updateRequest.getNazwa(), response.getNazwa());
        assertEquals(updateRequest.getKraj(), response.getKraj());
        assertEquals(updateRequest.getMiasto(), response.getMiasto());
        assertEquals(updateRequest.getNip(), response.getNip());
        assertEquals(updateRequest.getTelefon(), response.getTelefon());
    }

    @Test
    void testUpdateDostawcaNotFound() {
        when(dostawcaRepository.findById(99)).thenReturn(Optional.empty());

        DostawcaRequest updateRequest = new DostawcaRequest(
                "Dostawca Z", "Niemcy", "Berlin", "9998887776", "555-666-777"
        );

        RuntimeException ex = assertThrows(RuntimeException.class, () -> dostawcaService.updateDostawca(99, updateRequest));
        assertEquals("Dostawca o ID 99 nie istnieje", ex.getMessage());
    }

    @Test
    void testDeleteDostawca() {
        doNothing().when(dostawcaRepository).deleteById(1);

        dostawcaService.deleteDostawca(1);

        verify(dostawcaRepository, times(1)).deleteById(1);
    }
}
