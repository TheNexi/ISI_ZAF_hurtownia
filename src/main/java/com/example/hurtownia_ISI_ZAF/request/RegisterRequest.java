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
public class RegisterRequest {

    @Schema(example = "jan_kowalski")
    private String username;

    @Schema(example = "password123")
    private String password;

    @Schema(example = "jan.kowalski@example.com")
    private String email;

    @Schema(example = "USER")
    private String role;
}
