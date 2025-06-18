package com.example.hurtownia_ISI_ZAF.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtTokenResponse {
    private String token;

    public JwtTokenResponse(String token) {
        this.token = token;
    }
}
