package com.example.hurtownia_ISI_ZAF.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ZamowienieRequest {

    @Schema(example = "3")
    private Integer idKlient;

    @Schema(example = "2")
    private Integer idCzas;

    @Schema(example = "2")
    private Integer idDostawca;

    @Schema(example = "2")
    private Integer idMagazyn;

    @Schema(example = "1000.0")
    private Double wartoscCalkowita;

    private List<ProduktWZamowieniuRequest> produkty;
}
