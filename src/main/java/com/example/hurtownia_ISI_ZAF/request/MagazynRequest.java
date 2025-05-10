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
public class MagazynRequest {

    @Schema(example = "Magazyn A")
    private String nazwa;

    @Schema(example = "Polska")
    private String kraj;

    @Schema(example = "Warszawa")
    private String miasto;

    @Schema(example = "ul. Przykładowa 10")
    private String ulica;

    @Schema(example = "1000")
    private Integer pojemnosc;
}
