package com.example.hurtownia_ISI_ZAF.service;

import com.example.hurtownia_ISI_ZAF.model.*;
import com.example.hurtownia_ISI_ZAF.repository.*;
import com.example.hurtownia_ISI_ZAF.request.*;
import com.example.hurtownia_ISI_ZAF.response.ZamowienieResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;

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
    @Mock
    private ProduktRepository produktRepository;
    @Mock
    private ProduktWZamowieniuRepository produktWZamowieniuRepository;
    @Mock
    private UzytkownicyService uzytkownicyService;
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ZamowienieService zamowienieService;

    private Klient createKlient(int id) {
        Klient k = new Klient();
        k.setId(id);
        return k;
    }

    private Czas createCzas(int id) {
        Czas c = new Czas();
        c.setId(id);
        return c;
    }

    private Dostawca createDostawca(int id) {
        Dostawca d = new Dostawca();
        d.setId(id);
        return d;
    }

    private Magazyn createMagazyn(int id) {
        Magazyn magazyn = new Magazyn();
        magazyn.setId(id);
        return magazyn;
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllZamowienia() {
        Zamowienie zam1 = new Zamowienie();
        zam1.setId(1);
        zam1.setKlient(createKlient(10));
        zam1.setCzas(createCzas(100));
        zam1.setDostawca(createDostawca(1000));
        zam1.setMagazyn(createMagazyn(5000));
        zam1.setStatusPlatnosci(Zamowienie.StatusPlatnosci.PENDING);

        Zamowienie zam2 = new Zamowienie();
        zam2.setId(2);
        zam2.setKlient(createKlient(20));
        zam2.setCzas(createCzas(200));
        zam2.setDostawca(createDostawca(2000));
        zam2.setMagazyn(createMagazyn(6000));
        zam2.setStatusPlatnosci(Zamowienie.StatusPlatnosci.SUCCESS);

        when(zamowienieRepository.findAll()).thenReturn(List.of(zam1, zam2));

        List<ZamowienieResponse> result = zamowienieService.getAllZamowienia();

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());

        verify(zamowienieRepository, times(1)).findAll();
    }


    @Test
    void testGetZamowienieById_Found() {
        Zamowienie zam = new Zamowienie();
        zam.setId(1);
        zam.setKlient(createKlient(10));
        zam.setCzas(createCzas(100));
        zam.setDostawca(createDostawca(1000));
        zam.setMagazyn(createMagazyn(5000));
        zam.setStatusPlatnosci(Zamowienie.StatusPlatnosci.PENDING);

        when(zamowienieRepository.findById(1)).thenReturn(Optional.of(zam));

        ZamowienieResponse response = zamowienieService.getZamowienieById(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
        verify(zamowienieRepository, times(1)).findById(1);
    }

    @Test
    void testGetZamowienieById_NotFound() {
        when(zamowienieRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            zamowienieService.getZamowienieById(1);
        });

        assertEquals("Zamowienie with ID 1 not found", exception.getMessage());
        verify(zamowienieRepository, times(1)).findById(1);
    }

    @Test
    void testGetZamowieniaByKlientId() {
        Klient klient = createKlient(5);

        Zamowienie zam1 = new Zamowienie();
        zam1.setId(1);
        zam1.setKlient(klient);
        zam1.setCzas(createCzas(100));
        zam1.setDostawca(createDostawca(5000));
        zam1.setMagazyn(createMagazyn(5000));
        zam1.setStatusPlatnosci(Zamowienie.StatusPlatnosci.PENDING);

        Zamowienie zam2 = new Zamowienie();
        zam2.setId(2);
        zam2.setKlient(klient);
        zam2.setCzas(createCzas(200));
        zam2.setDostawca(createDostawca(6000));
        zam2.setMagazyn(createMagazyn(6000));
        zam2.setStatusPlatnosci(Zamowienie.StatusPlatnosci.SUCCESS);

        when(zamowienieRepository.findByKlient_Id(5)).thenReturn(List.of(zam1, zam2));

        List<ZamowienieResponse> results = zamowienieService.getZamowieniaByKlientId(5);

        assertEquals(2, results.size());
        assertEquals(1, results.get(0).getId());
        assertEquals(2, results.get(1).getId());

        verify(zamowienieRepository, times(1)).findByKlient_Id(5);
    }

    @Test
    void testAddZamowienie_Success() {
        ZamowienieRequest request = new ZamowienieRequest();
        request.setIdKlient(1);
        request.setIdCzas(1);
        request.setIdDostawca(1);
        request.setIdMagazyn(1);
        request.setWartoscCalkowita(100.0);

        ProduktWZamowieniuRequest produktRequest = new ProduktWZamowieniuRequest();
        produktRequest.setIdProdukt(10);
        produktRequest.setIlosc(2);
        produktRequest.setCena(50.0);
        produktRequest.setRabat(0.0);
        produktRequest.setWartoscLaczna(100.0);
        request.setProdukty(List.of(produktRequest));

        Klient klient = new Klient();
        klient.setId(1);
        when(klientRepository.findById(1)).thenReturn(Optional.of(klient));

        Czas czas = new Czas();
        czas.setId(1);
        when(czasRepository.findById(1)).thenReturn(Optional.of(czas));

        Dostawca dostawca = new Dostawca();
        dostawca.setId(1);
        when(dostawcaRepository.findById(1)).thenReturn(Optional.of(dostawca));

        Magazyn magazyn = new Magazyn();
        magazyn.setId(1);
        when(magazynRepository.findById(1)).thenReturn(Optional.of(magazyn));

        Produkt produkt = new Produkt();
        produkt.setId(10);
        produkt.setCena(50.0);
        produkt.setNazwa("Test Produkt");
        when(produktRepository.findById(10)).thenReturn(Optional.of(produkt));

        ArgumentCaptor<Zamowienie> zamowienieCaptor = ArgumentCaptor.forClass(Zamowienie.class);
        when(zamowienieRepository.save(any(Zamowienie.class))).thenAnswer(invocation -> {
            Zamowienie z = invocation.getArgument(0);
            z.setId(123);
            return z;
        });

        when(produktWZamowieniuRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        ZamowienieResponse response = zamowienieService.addZamowienie(request);

        verify(zamowienieRepository, times(1)).save(any(Zamowienie.class));
        verify(produktWZamowieniuRepository, times(1)).saveAll(anyList());

        assertNotNull(response);
        assertEquals(123, response.getId());
        assertEquals(1, response.getIdKlient());
        assertEquals(100.0, response.getWartoscCalkowita());
        assertEquals("PENDING", response.getStatusPlatnosci());
        assertEquals(1, response.getProdukty().size());
        assertEquals(10, response.getProdukty().get(0).getIdProdukt());
        assertEquals(2, response.getProdukty().get(0).getIlosc());
    }

    @Test
    void testUpdateZamowienie_Success() {
        Integer zamowienieId = 1;

        Zamowienie existingZamowienie = new Zamowienie();
        existingZamowienie.setId(zamowienieId);
        existingZamowienie.setProduktyWZamowieniu(List.of());
        existingZamowienie.setStatusPlatnosci(Zamowienie.StatusPlatnosci.PENDING);

        Klient klient = new Klient();
        klient.setId(2);

        Czas czas = new Czas();
        czas.setId(3);

        Dostawca dostawca = new Dostawca();
        dostawca.setId(4);

        Magazyn magazyn = new Magazyn();
        magazyn.setId(5);

        Produkt produkt1 = new Produkt();
        produkt1.setId(10);
        produkt1.setCena(100.0);

        Produkt produkt2 = new Produkt();
        produkt2.setId(11);
        produkt2.setCena(50.0);

        ZamowienieRequest request = new ZamowienieRequest();
        request.setIdKlient(2);
        request.setIdCzas(3);
        request.setIdDostawca(4);
        request.setIdMagazyn(5);
        request.setWartoscCalkowita(300.0);
        ProduktWZamowieniuRequest p1 = new ProduktWZamowieniuRequest();
        p1.setIdProdukt(10);
        p1.setIlosc(2);

        ProduktWZamowieniuRequest p2 = new ProduktWZamowieniuRequest();
        p2.setIdProdukt(11);
        p2.setIlosc(1);

        request.setProdukty(List.of(p1, p2));

        when(zamowienieRepository.findById(zamowienieId)).thenReturn(Optional.of(existingZamowienie));
        when(klientRepository.findById(2)).thenReturn(Optional.of(klient));
        when(czasRepository.findById(3)).thenReturn(Optional.of(czas));
        when(dostawcaRepository.findById(4)).thenReturn(Optional.of(dostawca));
        when(magazynRepository.findById(5)).thenReturn(Optional.of(magazyn));
        when(produktRepository.findById(10)).thenReturn(Optional.of(produkt1));
        when(produktRepository.findById(11)).thenReturn(Optional.of(produkt2));

        doNothing().when(produktWZamowieniuRepository).deleteByZamowienie_Id(zamowienieId);
        when(produktWZamowieniuRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0)); // zwraca listę produktów

        when(zamowienieRepository.save(existingZamowienie)).thenReturn(existingZamowienie);

        ZamowienieResponse response = zamowienieService.updateZamowienie(zamowienieId, request);

        assertNotNull(response);
        assertEquals(zamowienieId, response.getId());
        assertEquals(300.0, response.getWartoscCalkowita());
        assertEquals(2, response.getProdukty().size());

        verify(zamowienieRepository).findById(zamowienieId);
        verify(klientRepository).findById(2);
        verify(czasRepository).findById(3);
        verify(dostawcaRepository).findById(4);
        verify(magazynRepository).findById(5);
        verify(produktRepository, times(2)).findById(anyInt());
        verify(produktWZamowieniuRepository).deleteByZamowienie_Id(zamowienieId);
        verify(produktWZamowieniuRepository).saveAll(anyList());
        verify(zamowienieRepository).save(existingZamowienie);
    }

    @Test
    void testDeleteZamowienie_Success() {
        Integer zamowienieId = 1;
        Zamowienie zamowienie = new Zamowienie();
        zamowienie.setId(zamowienieId);

        when(zamowienieRepository.findById(zamowienieId)).thenReturn(Optional.of(zamowienie));

        zamowienieService.deleteZamowienie(zamowienieId);

        verify(zamowienieRepository, times(1)).delete(zamowienie);
    }

    @Test
    void testDeleteZamowienie_NotFound() {
        Integer zamowienieId = 1;

        when(zamowienieRepository.findById(zamowienieId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            zamowienieService.deleteZamowienie(zamowienieId);
        });

        assertEquals("Zamowienie with ID " + zamowienieId + " not found", exception.getMessage());

        verify(zamowienieRepository, never()).delete(any());
    }

    @Test
    void testZatwierdzPlatnoscOffline_Success() {
        Integer zamowienieId = 1;

        Zamowienie zamowienie = new Zamowienie();
        zamowienie.setId(zamowienieId);
        zamowienie.setStatusPlatnosci(Zamowienie.StatusPlatnosci.PENDING);

        Klient klient = new Klient();
        klient.setId(123);
        zamowienie.setKlient(klient);

        Czas czas = new Czas();
        czas.setId(456);
        zamowienie.setCzas(czas);

        Dostawca dostawca = new Dostawca();
        dostawca.setId(789);
        zamowienie.setDostawca(dostawca);

        Magazyn magazyn = new Magazyn();
        magazyn.setId(1011);
        zamowienie.setMagazyn(magazyn);

        when(zamowienieRepository.findById(zamowienieId)).thenReturn(Optional.of(zamowienie));
        when(zamowienieRepository.save(any(Zamowienie.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ZamowienieResponse response = zamowienieService.zatwierdzPlatnoscOffline(zamowienieId);

        assertNotNull(response);
        assertEquals(zamowienieId, response.getId());
        assertEquals("OFFLINE", response.getStatusPlatnosci());

        verify(zamowienieRepository, times(1)).findById(zamowienieId);
        verify(zamowienieRepository, times(1)).save(zamowienie);
    }

    @Test
    void testZatwierdzPlatnoscOffline_NotFound() {
        Integer zamowienieId = 1;

        when(zamowienieRepository.findById(zamowienieId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            zamowienieService.zatwierdzPlatnoscOffline(zamowienieId);
        });

        assertEquals("Zamówienie o ID " + zamowienieId + " nie istnieje", exception.getMessage());

        verify(zamowienieRepository, times(1)).findById(zamowienieId);
        verify(zamowienieRepository, never()).save(any());
    }

    @Test
    void testMapToResponse() throws Exception {
        Zamowienie zamowienie = new Zamowienie();
        zamowienie.setId(1);

        Klient klient = new Klient();
        klient.setId(10);
        zamowienie.setKlient(klient);

        Czas czas = new Czas();
        czas.setId(20);
        zamowienie.setCzas(czas);

        Dostawca dostawca = new Dostawca();
        dostawca.setId(30);
        zamowienie.setDostawca(dostawca);

        Magazyn magazyn = new Magazyn();
        magazyn.setId(40);
        zamowienie.setMagazyn(magazyn);

        zamowienie.setWartoscCalkowita(100.0);
        zamowienie.setStatusPlatnosci(Zamowienie.StatusPlatnosci.SUCCESS);

        Produkt produkt = new Produkt();
        produkt.setId(100);

        ProduktWZamowieniu produktWZ = new ProduktWZamowieniu();
        produktWZ.setId(50);
        produktWZ.setZamowienie(zamowienie);
        produktWZ.setProdukt(produkt);

        zamowienie.setProduktyWZamowieniu(List.of(produktWZ));

        var method = ZamowienieService.class.getDeclaredMethod("mapToResponse", Zamowienie.class);
        method.setAccessible(true);

        ZamowienieResponse response = (ZamowienieResponse) method.invoke(zamowienieService, zamowienie);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(10, response.getIdKlient());
        assertEquals(20, response.getIdCzas());
        assertEquals(30, response.getIdDostawca());
        assertEquals(40, response.getIdMagazyn());
        assertEquals(100.0, response.getWartoscCalkowita());
        assertEquals("SUCCESS", response.getStatusPlatnosci());

        assertNotNull(response.getProdukty());
        assertEquals(1, response.getProdukty().size());
    }

    @Test
    void testInicjalizujPlatnosc_Success() throws Exception {
        Produkt produkt = new Produkt();
        produkt.setNazwa("Produkt A");
        produkt.setCena(123.45);

        ProduktWZamowieniu pwz = new ProduktWZamowieniu();
        pwz.setProdukt(produkt);
        pwz.setIlosc(2);

        Zamowienie zamowienie = new Zamowienie();
        zamowienie.setId(1);
        zamowienie.setWartoscCalkowita(246.90);
        zamowienie.setProduktyWZamowieniu(List.of(pwz));

        Mockito.when(zamowienieRepository.findById(1)).thenReturn(Optional.of(zamowienie));

        ZamowienieService spyService = Mockito.spy(zamowienieService);

        Field restTemplateField = ZamowienieService.class.getDeclaredField("restTemplate");
        restTemplateField.setAccessible(true);
        restTemplateField.set(spyService, restTemplate);

        Field clientIdField = ZamowienieService.class.getDeclaredField("clientId");
        clientIdField.setAccessible(true);
        clientIdField.set(spyService, "testClientId");

        Field clientSecretField = ZamowienieService.class.getDeclaredField("clientSecret");
        clientSecretField.setAccessible(true);
        clientSecretField.set(spyService, "testClientSecret");

        Field uzytkownicyServiceField = ZamowienieService.class.getDeclaredField("uzytkownicyService");
        uzytkownicyServiceField.setAccessible(true);
        uzytkownicyServiceField.set(spyService, uzytkownicyService);

        Map<String, Object> tokenBody = Map.of("access_token", "mocked-token");
        ResponseEntity<Map> tokenResponse = new ResponseEntity<>(tokenBody, HttpStatus.OK);

        Mockito.when(restTemplate.postForEntity(
                Mockito.eq("https://secure.snd.payu.com/pl/standard/user/oauth/authorize"),
                Mockito.any(HttpEntity.class),
                Mockito.eq(Map.class)
        )).thenReturn(tokenResponse);

        Uzytkownicy user = new Uzytkownicy();
        user.setEmail("test@example.com");
        Mockito.when(uzytkownicyService.findUserByLogin(Mockito.anyString())).thenReturn(Optional.of(user));

        Map<String, Object> body = new HashMap<>();
        body.put("redirectUri", "https://payu.redirect.url");
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(body, HttpStatus.OK);

        Mockito.when(restTemplate.postForEntity(
                Mockito.eq("https://secure.snd.payu.com/api/v2_1/orders"),
                Mockito.any(HttpEntity.class),
                Mockito.eq(Map.class)
        )).thenReturn(responseEntity);

        String redirectUri = spyService.inicjalizujPlatnosc(1);

        assertEquals("https://payu.redirect.url", redirectUri);

        Mockito.verify(restTemplate).postForEntity(
                Mockito.eq("https://secure.snd.payu.com/api/v2_1/orders"),
                Mockito.any(HttpEntity.class),
                Mockito.eq(Map.class)
        );
    }

    @Test
    void testUzyskajToken() throws Exception {
        Map<String, Object> responseBody = Map.of("access_token", "mocked-access-token");
        ResponseEntity<Map> response = new ResponseEntity<>(responseBody, HttpStatus.OK);

        Mockito.when(restTemplate.postForEntity(
                Mockito.anyString(),
                Mockito.any(HttpEntity.class),
                Mockito.eq(Map.class)
        )).thenReturn(response);

        Field restTemplateField = ZamowienieService.class.getDeclaredField("restTemplate");
        restTemplateField.setAccessible(true);
        restTemplateField.set(zamowienieService, restTemplate);

        Field clientIdField = ZamowienieService.class.getDeclaredField("clientId");
        clientIdField.setAccessible(true);
        clientIdField.set(zamowienieService, "mockClientId");

        Field clientSecretField = ZamowienieService.class.getDeclaredField("clientSecret");
        clientSecretField.setAccessible(true);
        clientSecretField.set(zamowienieService, "mockClientSecret");

        Method method = ZamowienieService.class.getDeclaredMethod("uzyskajToken");
        method.setAccessible(true);
        String token = (String) method.invoke(zamowienieService);

        assertEquals("mocked-access-token", token);
    }

    @Test
    void testExtractEmailFromAuthentication_WithPrincipalString() throws Exception {
        // Mock SecurityContext i Authentication
        Authentication auth = Mockito.mock(Authentication.class);
        Mockito.when(auth.getPrincipal()).thenReturn("userLogin");

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        Uzytkownicy user = new Uzytkownicy();
        user.setEmail("email@example.com");

        Mockito.when(uzytkownicyService.findUserByLogin("userLogin")).thenReturn(Optional.of(user));

        Field uzytkownicyServiceField = ZamowienieService.class.getDeclaredField("uzytkownicyService");
        uzytkownicyServiceField.setAccessible(true);
        uzytkownicyServiceField.set(zamowienieService, uzytkownicyService);
        Method method = ZamowienieService.class.getDeclaredMethod("extractEmailFromAuthentication");
        method.setAccessible(true);
        String email = (String) method.invoke(zamowienieService);

        assertEquals("email@example.com", email);
    }

    @Test
    void testExtractEmailFromAuthentication_WithOAuth2User() throws Exception {
        Authentication auth = Mockito.mock(Authentication.class);
        OAuth2User oAuth2User = Mockito.mock(OAuth2User.class);
        Mockito.when(oAuth2User.getAttribute("email")).thenReturn("oauth2@example.com");

        Mockito.when(auth.getPrincipal()).thenReturn(oAuth2User);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        Method method = ZamowienieService.class.getDeclaredMethod("extractEmailFromAuthentication");
        method.setAccessible(true);

        String email = (String) method.invoke(zamowienieService);

        assertEquals("oauth2@example.com", email);
    }

    @Test
    void testExtractEmailFromAuthentication_NoAuthentication() throws Exception {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        Method method = ZamowienieService.class.getDeclaredMethod("extractEmailFromAuthentication");
        method.setAccessible(true);

        String email = (String) method.invoke(zamowienieService);

        assertNull(email);
    }
}
