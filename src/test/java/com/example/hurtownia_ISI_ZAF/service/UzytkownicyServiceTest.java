package com.example.hurtownia_ISI_ZAF.service;

import com.example.hurtownia_ISI_ZAF.model.Uzytkownicy;
import com.example.hurtownia_ISI_ZAF.repository.UzytkownicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UzytkownicyServiceTest {

    @Mock
    private UzytkownicyRepository uzytkownicyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UzytkownicyService uzytkownicyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        String username = "testuser";
        Uzytkownicy user = new Uzytkownicy();
        user.setLogin(username);
        user.setHaslo("encodedPassword");
        user.setRole("USER");

        when(uzytkownicyRepository.findByLogin(username)).thenReturn(Optional.of(user));

        var userDetails = uzytkownicyService.loadUserByUsername(username);

        assertEquals(username, userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsException() {
        String username = "notfound";

        when(uzytkownicyRepository.findByLogin(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            uzytkownicyService.loadUserByUsername(username);
        });
    }

    @Test
    void registerUser_Success() {
        String username = "newuser";
        String password = "pass123";
        String email = "newuser@example.com";
        String encodedPassword = "encodedPass";

        when(uzytkownicyRepository.findByLogin(username)).thenReturn(Optional.empty());
        when(uzytkownicyRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        Uzytkownicy savedUser = new Uzytkownicy();
        savedUser.setLogin(username);
        savedUser.setHaslo(encodedPassword);
        savedUser.setEmail(email);
        savedUser.setRole("USER");

        when(uzytkownicyRepository.save(any(Uzytkownicy.class))).thenReturn(savedUser);

        Uzytkownicy result = uzytkownicyService.registerUser(username, password, email);

        assertEquals(username, result.getLogin());
        assertEquals(encodedPassword, result.getHaslo());
        assertEquals(email, result.getEmail());
        assertEquals("USER", result.getRole());
    }

    @Test
    void registerUser_UsernameExists_ThrowsException() {
        String username = "existinguser";
        String email = "newemail@example.com";

        when(uzytkownicyRepository.findByLogin(username)).thenReturn(Optional.of(new Uzytkownicy()));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            uzytkownicyService.registerUser(username, "anyPass", email);
        });

        assertEquals("Użytkownik o tej nazwie już istnieje", ex.getMessage());
    }

    @Test
    void registerUser_EmailExists_ThrowsException() {
        String username = "newuser";
        String email = "existingemail@example.com";

        when(uzytkownicyRepository.findByLogin(username)).thenReturn(Optional.empty());
        when(uzytkownicyRepository.findByEmail(email)).thenReturn(Optional.of(new Uzytkownicy()));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            uzytkownicyService.registerUser(username, "anyPass", email);
        });

        assertEquals("Użytkownik z tym emailem już istnieje", ex.getMessage());
    }

    @Test
    void findUserByLogin_ReturnsUser() {
        String username = "someuser";
        Uzytkownicy user = new Uzytkownicy();
        user.setLogin(username);

        when(uzytkownicyRepository.findByLogin(username)).thenReturn(Optional.of(user));

        Optional<Uzytkownicy> found = uzytkownicyService.findUserByLogin(username);

        assertTrue(found.isPresent());
        assertEquals(username, found.get().getLogin());
    }

    @Test
    void findUserByEmail_ReturnsUser() {
        String email = "email@example.com";
        Uzytkownicy user = new Uzytkownicy();
        user.setEmail(email);

        when(uzytkownicyRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<Uzytkownicy> found = uzytkownicyService.findUserByEmail(email);

        assertTrue(found.isPresent());
        assertEquals(email, found.get().getEmail());
    }
}
