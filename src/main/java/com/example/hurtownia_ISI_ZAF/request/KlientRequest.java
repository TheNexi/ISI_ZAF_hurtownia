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
public class KlientRequest {

    @Schema(example = "Firma XYZ")
    private String nazwa;

    @Schema(example = "123-456-78-90")
    private String nip;

    @Schema(example = "Polska")
    private String kraj;

    @Schema(example = "Warszawa")
    private String miasto;

    @Schema(example = "ul. Przykładowa 10")
    private String ulica;

    @Schema(example = "00-001")
    private String kodPocztowy;
}
