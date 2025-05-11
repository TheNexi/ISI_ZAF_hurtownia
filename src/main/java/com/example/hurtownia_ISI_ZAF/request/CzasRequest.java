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
public class CzasRequest {

    @Schema(example = "1")
    private Integer dzien;

    @Schema(example = "5")
    private Integer miesiac;

    @Schema(example = "2025")
    private Integer rok;

    @Schema(example = "I")
    private String kwartal;

    @Schema(example = "Poniedziałek")
    private String dzienTygodnia;
}
