package com.example.hurtownia_ISI_ZAF.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

public class JwtAuthentication extends AbstractAuthenticationToken {

    private final String username;

    public JwtAuthentication(String username) {
        super(null);
        this.username = username;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public UserDetails getDetails() {
        return new User(username, "", Collections.singletonList(new SimpleGrantedAuthority("USER")));
    }
}
