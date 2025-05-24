package com.example.hurtownia_ISI_ZAF.controller;

import com.example.hurtownia_ISI_ZAF.service.JwtTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


import java.io.IOException;
import java.util.Collection;

public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenService jwtTokenService;

    public OAuth2LoginSuccessHandler(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String jwtToken = jwtTokenService.generateToken(authentication);
        Cookie cookie = new Cookie("jwt", jwtToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(1800);
        response.addCookie(cookie);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectUrl = "http://localhost:3000/";

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            if (role.equals("ROLE_ADMIN") || role.equals("ADMIN")) {
                redirectUrl = "http://localhost:3000/admin";
                break;
            } else if (role.equals("ROLE_USER") || role.equals("USER")) {
                redirectUrl = "http://localhost:3000";
                break;
            }
        }

        response.sendRedirect(redirectUrl);
    }
}

