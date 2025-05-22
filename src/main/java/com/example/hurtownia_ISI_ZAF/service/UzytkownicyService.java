package com.example.hurtownia_ISI_ZAF.service;

import com.example.hurtownia_ISI_ZAF.model.Uzytkownicy;
import com.example.hurtownia_ISI_ZAF.repository.UzytkownicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UzytkownicyService implements UserDetailsService {

    private final UzytkownicyRepository uzytkownicyRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UzytkownicyService(UzytkownicyRepository uzytkownicyRepository, PasswordEncoder passwordEncoder) {
        this.uzytkownicyRepository = uzytkownicyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Uzytkownicy user = uzytkownicyRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.builder()
                .username(user.getLogin())
                .password(user.getHaslo())
                .roles(user.getRole())
                .build();
    }

    public Uzytkownicy registerUser(String username, String password, String email) {
        if (uzytkownicyRepository.findByLogin(username).isPresent()) {
            throw new RuntimeException("Użytkownik o tej nazwie już istnieje");
        }

        if (uzytkownicyRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Użytkownik z tym emailem już istnieje");
        }

        Uzytkownicy user = new Uzytkownicy();
        user.setLogin(username);
        user.setHaslo(passwordEncoder.encode(password));  // Szyfrowanie hasła
        user.setEmail(email);
        user.setRole("USER");

        return uzytkownicyRepository.save(user);
    }

    public Optional<Uzytkownicy> findUserByLogin(String username) {
        return uzytkownicyRepository.findByLogin(username);
    }

    public Optional<Uzytkownicy> findUserByEmail(String email) {
        return uzytkownicyRepository.findByEmail(email);
    }
}
