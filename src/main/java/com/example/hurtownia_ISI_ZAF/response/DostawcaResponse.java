package com.example.hurtownia_ISI_ZAF.response;

import com.example.hurtownia_ISI_ZAF.model.Dostawca;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DostawcaResponse {

    private Integer id;
    private String nazwa;
    private String kraj;
    private String miasto;
    private String nip;
    private String telefon;

    public DostawcaResponse(Dostawca dostawca) {
        if (dostawca != null) {
            this.id = dostawca.getId();
            this.nazwa = dostawca.getNazwa();
            this.kraj = dostawca.getKraj();
            this.miasto = dostawca.getMiasto();
            this.nip = dostawca.getNip();
            this.telefon = dostawca.getTelefon();
        }
    }
}
