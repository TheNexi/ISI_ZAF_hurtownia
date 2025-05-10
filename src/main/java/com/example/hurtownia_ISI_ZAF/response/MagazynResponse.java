package com.example.hurtownia_ISI_ZAF.response;

import com.example.hurtownia_ISI_ZAF.model.Magazyn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MagazynResponse {

    private Integer id;
    private String nazwa;
    private String kraj;
    private String miasto;
    private String ulica;
    private Integer pojemnosc;

    public MagazynResponse(Magazyn magazyn) {
        if (magazyn != null) {
            this.id = magazyn.getId();
            this.nazwa = magazyn.getNazwa();
            this.kraj = magazyn.getKraj();
            this.miasto = magazyn.getMiasto();
            this.ulica = magazyn.getUlica();
            this.pojemnosc = magazyn.getPojemnosc();
        }
    }
}
