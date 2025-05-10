package com.example.hurtownia_ISI_ZAF.response;

import com.example.hurtownia_ISI_ZAF.model.Klient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class KlientResponse {

    private Integer id;
    private String nazwa;
    private String nip;
    private String kraj;
    private String miasto;
    private String ulica;
    private String kodPocztowy;

    public KlientResponse(Klient klient) {
        if (klient != null) {
            this.id = klient.getId();
            this.nazwa = klient.getNazwa();
            this.nip = klient.getNip();
            this.kraj = klient.getKraj();
            this.miasto = klient.getMiasto();
            this.ulica = klient.getUlica();
            this.kodPocztowy = klient.getKodPocztowy();
        }
    }
}
