package com.example.hurtownia_ISI_ZAF.response;

import com.example.hurtownia_ISI_ZAF.model.Czas;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CzasResponse {

    private Integer id;
    private Integer dzien;
    private Integer miesiac;
    private Integer rok;
    private String kwartal;
    private String dzienTygodnia;

    public CzasResponse(Czas czas) {
        if (czas != null) {
            this.id = czas.getId();
            this.dzien = czas.getDzien();
            this.miesiac = czas.getMiesiac();
            this.rok = czas.getRok();
            this.kwartal = czas.getKwartal();
            this.dzienTygodnia = czas.getDzienTygodnia();
        }
    }
}
