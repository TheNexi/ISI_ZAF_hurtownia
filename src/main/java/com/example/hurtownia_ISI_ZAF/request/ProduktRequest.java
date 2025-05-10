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
public class ProduktRequest {

    @Schema(example = "Produkt A")
    private String nazwa;

    @Schema(example = "Kategoria A")
    private String kategoria;

    @Schema(example = "szt.")
    private String jednostkaMiary;

    @Schema(example = "19.99")
    private Double cena;
}
