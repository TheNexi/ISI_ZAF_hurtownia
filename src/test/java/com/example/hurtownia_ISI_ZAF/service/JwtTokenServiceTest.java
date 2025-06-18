package com.example.hurtownia_ISI_ZAF.service;

import com.example.hurtownia_ISI_ZAF.config.JwtConfig;
import com.example.hurtownia_ISI_ZAF.model.Uzytkownicy;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.crypto.SecretKey;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtTokenServiceTest {

    @Mock
    private JwtConfig jwtConfig;

    @Mock
    private UzytkownicyService uzytkownicyService;

    @Mock
    private Authentication authentication;

    @Mock
    private OAuth2User oAuth2User;

    @InjectMocks
    private JwtTokenService jwtTokenService;

    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        secretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512);
        String secret = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        when(jwtConfig.getJwtSecret()).thenReturn(secret);
        when(jwtConfig.getJwtExpirationMs()).thenReturn(3600000L);

        jwtTokenService.init();
    }

    @Test
    void testGenerateToken_withUsernamePrincipal() {
        String username = "testuser";
        String email = "testuser@example.com";
        String role = "ADMIN";

        Uzytkownicy user = new Uzytkownicy();
        user.setEmail(email);
        user.setRole(role);

        when(authentication.getPrincipal()).thenReturn(username);
        when(uzytkownicyService.findUserByLogin(username)).thenReturn(Optional.of(user));

        String token = jwtTokenService.generateToken(authentication);

        assertNotNull(token);
        assertTrue(jwtTokenService.validateToken(token));
        assertEquals(email, jwtTokenService.getEmailFromToken(token));
        assertEquals(role, jwtTokenService.getRoleFromToken(token));
        assertEquals(username, jwtTokenService.getLoginFromToken(token));
    }

    @Test
    void testGenerateToken_withOAuth2UserPrincipal() {
        String email = "oauthuser@example.com";
        String username = "oauthname";
        String role = "USER";

        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAttribute("email")).thenReturn(email);
        when(oAuth2User.getAttribute("name")).thenReturn(username);

        GrantedAuthority authority = () -> "ROLE_USER";
        when(authentication.getAuthorities()).thenAnswer(invocation -> Collections.singletonList(authority));

        String token = jwtTokenService.generateToken(authentication);

        assertNotNull(token);
        assertTrue(jwtTokenService.validateToken(token));
        assertEquals(email, jwtTokenService.getEmailFromToken(token));
        assertEquals(role, jwtTokenService.getRoleFromToken(token));
        assertEquals(username, jwtTokenService.getLoginFromToken(token));
    }

    @Test
    void testValidateToken_invalidToken() {
        String badToken = "this.is.not.a.valid.token";

        assertFalse(jwtTokenService.validateToken(badToken));
    }

    @Test
    void testGetEmailFromToken_invalidToken_throwsException() {
        String badToken = "invalid.token";

        assertThrows(io.jsonwebtoken.JwtException.class, () -> {
            jwtTokenService.getEmailFromToken(badToken);
        });
    }
}
