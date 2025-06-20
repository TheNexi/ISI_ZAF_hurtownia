package com.example.hurtownia_ISI_ZAF.service;

import com.example.hurtownia_ISI_ZAF.config.JwtConfig;
import com.example.hurtownia_ISI_ZAF.model.Uzytkownicy;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtTokenService {

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private UzytkownicyService uzytkownicyService;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtConfig.getJwtSecret().getBytes());
    }

    public String generateToken(Authentication authentication) {
        String username = "";
        String email = "";
        String role = "";
        Integer idKlient = null;

        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            username = (String) principal;
            Optional<Uzytkownicy> userOpt = uzytkownicyService.findUserByLogin(username);
            if (userOpt.isPresent()) {
                Uzytkownicy user = userOpt.get();
                email = user.getEmail();
                role = user.getRole();
                if (user.getKlient() != null) {
                    idKlient = user.getKlient().getId();
                }

            }
        } else if (principal instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) principal;
            email = oAuth2User.getAttribute("email");
            username = oAuth2User.getAttribute("name");
            role = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .map(r -> r.replace("ROLE_", ""))
                    .orElse("USER");

            Optional<Uzytkownicy> userOpt = uzytkownicyService.findUserByEmail(email);
            if (userOpt.isPresent()) {
                Uzytkownicy user = userOpt.get();
                if (user.getKlient() != null) {
                    idKlient = user.getKlient().getId();
                }
            }
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getJwtExpirationMs());

        JwtBuilder builder = Jwts.builder()
                .setSubject(email)
                .claim("login", username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512);

        if (idKlient != null) {
            builder.claim("id_klient", idKlient);
        }

        return builder.compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }

    public String getLoginFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("login", String.class);
    }

    public Integer getIdKlientFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("id_klient", Integer.class);
    }
}
