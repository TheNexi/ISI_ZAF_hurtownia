package com.example.hurtownia_ISI_ZAF.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProduktWZamowieniuRequest {

    @Schema(example = "2")
    private Integer idZamowienie;

    @Schema(example = "2")
    private Integer idProdukt;

    @Schema(example = "5")
    private Integer ilosc;

    @Schema(example = "100.0")
    private Double cena;

    @Schema(example = "10.0")
    private Double rabat;

    @Schema(example = "450.0")
    private Double wartoscLaczna;
}
