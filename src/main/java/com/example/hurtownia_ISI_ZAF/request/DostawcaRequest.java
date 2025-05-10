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
public class DostawcaRequest {

    @Schema(example = "Firma XYZ")
    private String nazwa;

    @Schema(example = "Polska")
    private String kraj;

    @Schema(example = "Warszawa")
    private String miasto;

    @Schema(example = "123-456-78-90")
    private String nip;

    @Schema(example = "123-456-789")
    private String telefon;
}
