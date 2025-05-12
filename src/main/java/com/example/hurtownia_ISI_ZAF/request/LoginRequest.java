package com.example.hurtownia_ISI_ZAF.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @Schema(example = "jan_kowalski")
    private String username;

    @Schema(example = "password123")
    private String password;
}
