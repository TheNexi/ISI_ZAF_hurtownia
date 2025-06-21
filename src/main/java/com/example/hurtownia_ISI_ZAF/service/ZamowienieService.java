package com.example.hurtownia_ISI_ZAF.service;

import com.example.hurtownia_ISI_ZAF.model.*;
import com.example.hurtownia_ISI_ZAF.repository.*;
import com.example.hurtownia_ISI_ZAF.request.*;
import com.example.hurtownia_ISI_ZAF.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ZamowienieService {

    private final ZamowienieRepository zamowienieRepository;
    private final KlientRepository klientRepository;
    private final CzasRepository czasRepository;
    private final DostawcaRepository dostawcaRepository;
    private final MagazynRepository magazynRepository;
    private final ProduktRepository produktRepository;
    private final ProduktWZamowieniuRepository produktWZamowieniuRepository;

    @Value("${payu.clientId}")
    private String clientId;

    @Value("${payu.clientSecret}")
    private String clientSecret;

    @Value("${payu.posId}")
    private String posId;

    @Value("${payu.notifyUrl}")
    private String notifyUrl;

    @Value("${payu.continueUrl}")
    private String continueUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private UzytkownicyService uzytkownicyService;

    @Autowired
    public ZamowienieService(ZamowienieRepository zamowienieRepository,
                             KlientRepository klientRepository,
                             CzasRepository czasRepository,
                             DostawcaRepository dostawcaRepository,
                             MagazynRepository magazynRepository,
                             ProduktRepository produktRepository,
                             ProduktWZamowieniuRepository produktWZamowieniuRepository) {
        this.zamowienieRepository = zamowienieRepository;
        this.klientRepository = klientRepository;
        this.czasRepository = czasRepository;
        this.dostawcaRepository = dostawcaRepository;
        this.magazynRepository = magazynRepository;
        this.produktRepository = produktRepository;
        this.produktWZamowieniuRepository = produktWZamowieniuRepository;
    }

    public List<ZamowienieResponse> getAllZamowienia() {
        return zamowienieRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ZamowienieResponse getZamowienieById(Integer id) {
        Zamowienie zamowienie = zamowienieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zamowienie with ID " + id + " not found"));
        return mapToResponse(zamowienie);
    }

    public List<ZamowienieResponse> getZamowieniaByKlientId(Integer klientId) {
        return zamowienieRepository.findByKlient_Id(klientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ZamowienieResponse addZamowienie(ZamowienieRequest request) {
        var klient = klientRepository.findById(request.getIdKlient())
                .orElseThrow(() -> new RuntimeException("Klient with ID " + request.getIdKlient() + " not found"));

        var czas = czasRepository.findById(request.getIdCzas())
                .orElseThrow(() -> new RuntimeException("Czas with ID " + request.getIdCzas() + " not found"));

        var dostawca = dostawcaRepository.findById(request.getIdDostawca())
                .orElseThrow(() -> new RuntimeException("Dostawca with ID " + request.getIdDostawca() + " not found"));

        var magazyn = magazynRepository.findById(request.getIdMagazyn())
                .orElseThrow(() -> new RuntimeException("Magazyn with ID " + request.getIdMagazyn() + " not found"));

        Zamowienie zamowienie = new Zamowienie();
        zamowienie.setKlient(klient);
        zamowienie.setCzas(czas);
        zamowienie.setDostawca(dostawca);
        zamowienie.setMagazyn(magazyn);
        zamowienie.setWartoscCalkowita(request.getWartoscCalkowita());
        zamowienie.setStatusPlatnosci(Zamowienie.StatusPlatnosci.PENDING);

        Zamowienie zapisaneZamowienie = zamowienieRepository.save(zamowienie);

        if (request.getProdukty() != null && !request.getProdukty().isEmpty()) {
            List<ProduktWZamowieniu> produktyWZamowieniu = request.getProdukty().stream().map(p -> {
                Produkt produkt = produktRepository.findById(p.getIdProdukt())
                        .orElseThrow(() -> new RuntimeException("Produkt with ID " + p.getIdProdukt() + " not found"));

                ProduktWZamowieniu pwz = new ProduktWZamowieniu();
                pwz.setZamowienie(zapisaneZamowienie);
                pwz.setProdukt(produkt);
                pwz.setIlosc(p.getIlosc());
                pwz.setCena(produkt.getCena());
                pwz.setRabat(0.0);
                pwz.setWartoscLaczna(produkt.getCena() * p.getIlosc());
                return pwz;
            }).collect(Collectors.toList());

            produktWZamowieniuRepository.saveAll(produktyWZamowieniu);
            zapisaneZamowienie.setProduktyWZamowieniu(produktyWZamowieniu);
        }

        return mapToResponse(zapisaneZamowienie);
    }

    public ZamowienieResponse updateZamowienie(Integer id, ZamowienieRequest request) {
        Zamowienie zamowienie = zamowienieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zamowienie with ID " + id + " not found"));

        var klient = klientRepository.findById(request.getIdKlient())
                .orElseThrow(() -> new RuntimeException("Klient with ID " + request.getIdKlient() + " not found"));

        var czas = czasRepository.findById(request.getIdCzas())
                .orElseThrow(() -> new RuntimeException("Czas with ID " + request.getIdCzas() + " not found"));

        var dostawca = dostawcaRepository.findById(request.getIdDostawca())
                .orElseThrow(() -> new RuntimeException("Dostawca with ID " + request.getIdDostawca() + " not found"));

        var magazyn = magazynRepository.findById(request.getIdMagazyn())
                .orElseThrow(() -> new RuntimeException("Magazyn with ID " + request.getIdMagazyn() + " not found"));

        zamowienie.setKlient(klient);
        zamowienie.setCzas(czas);
        zamowienie.setDostawca(dostawca);
        zamowienie.setMagazyn(magazyn);
        zamowienie.setWartoscCalkowita(request.getWartoscCalkowita());

        zamowienieRepository.save(zamowienie);

        produktWZamowieniuRepository.deleteByZamowienie_Id(id);

        List<ProduktWZamowieniu> noweProduktyWZamowieniu = request.getProdukty().stream().map(p -> {
            Produkt produkt = produktRepository.findById(p.getIdProdukt())
                    .orElseThrow(() -> new RuntimeException("Produkt with ID " + p.getIdProdukt() + " not found"));

            ProduktWZamowieniu pwz = new ProduktWZamowieniu();
            pwz.setZamowienie(zamowienie);
            pwz.setProdukt(produkt);
            pwz.setIlosc(p.getIlosc());
            pwz.setCena(produkt.getCena());
            pwz.setRabat(0.0);
            pwz.setWartoscLaczna(produkt.getCena() * p.getIlosc());
            return pwz;
        }).collect(Collectors.toList());

        produktWZamowieniuRepository.saveAll(noweProduktyWZamowieniu);
        zamowienie.setProduktyWZamowieniu(noweProduktyWZamowieniu);

        return mapToResponse(zamowienie);
    }

    public void deleteZamowienie(Integer id) {
        Zamowienie zamowienie = zamowienieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zamowienie with ID " + id + " not found"));
        zamowienieRepository.delete(zamowienie);
    }

    public ZamowienieResponse zatwierdzPlatnoscOffline(Integer id) {
        Zamowienie zamowienie = zamowienieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zamówienie o ID " + id + " nie istnieje"));

        zamowienie.setStatusPlatnosci(Zamowienie.StatusPlatnosci.OFFLINE);
        zamowienieRepository.save(zamowienie);

        return mapToResponse(zamowienie);
    }

    private ZamowienieResponse mapToResponse(Zamowienie zamowienie) {
        List<ProduktWZamowieniuResponse> produktyResponse = zamowienie.getProduktyWZamowieniu() == null
                ? List.of()
                : zamowienie.getProduktyWZamowieniu().stream()
                .map(ProduktWZamowieniuResponse::new)
                .collect(Collectors.toList());

        return new ZamowienieResponse(
                zamowienie.getId(),
                zamowienie.getKlient().getId(),
                zamowienie.getCzas().getId(),
                zamowienie.getDostawca().getId(),
                zamowienie.getMagazyn().getId(),
                zamowienie.getWartoscCalkowita(),
                zamowienie.getStatusPlatnosci().name(),
                produktyResponse
        );
    }

    public String inicjalizujPlatnosc(Integer zamowienieId) {
        Zamowienie zamowienie = zamowienieRepository.findById(zamowienieId)
                .orElseThrow(() -> new RuntimeException("Zamówienie nie istnieje"));

        try {
            String token = uzyskajToken();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> payload = new HashMap<>();
            payload.put("notifyUrl", notifyUrl);
            payload.put("continueUrl", continueUrl);
            payload.put("customerIp", "127.0.0.1");
            payload.put("merchantPosId", posId);
            payload.put("description", "Zamówienie #" + zamowienieId);
            payload.put("currencyCode", "PLN");
            payload.put("totalAmount", String.valueOf((int)(zamowienie.getWartoscCalkowita() * 100)));

            String email = extractEmailFromAuthentication();

            Map<String, String> buyer = Map.of(
                    "email", email != null ? email : "unknown@example.com",
                    "language", "pl"
            );
            payload.put("buyer", buyer);

            List<Map<String, Object>> products = zamowienie.getProduktyWZamowieniu().stream()
                    .map(pwz -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", pwz.getProdukt().getNazwa());
                        map.put("unitPrice", (int)(pwz.getProdukt().getCena() * 100));
                        map.put("quantity", pwz.getIlosc());
                        return map;
                    })
                    .toList();


            payload.put("products", products);


            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://secure.snd.payu.com/api/v2_1/orders",
                    request,
                    Map.class
            );

            return (String) response.getBody().get("redirectUri");

        } catch (Exception e) {
            throw new RuntimeException("Błąd PayU: " + e.getMessage(), e);
        }
    }

    private String uzyskajToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");

        HttpEntity<?> request = new HttpEntity<>(form, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://secure.snd.payu.com/pl/standard/user/oauth/authorize",
                request,
                Map.class
        );

        return (String) response.getBody().get("access_token");
    }

    private String extractEmailFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof String) {
            String username = (String) principal;
            Optional<Uzytkownicy> userOpt = uzytkownicyService.findUserByLogin(username);
            if (userOpt.isPresent()) {
                return userOpt.get().getEmail();
            }
        } else if (principal instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) principal;
            return oAuth2User.getAttribute("email");
        }
        return null;
    }

}
