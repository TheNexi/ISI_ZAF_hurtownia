package com.example.hurtownia_ISI_ZAF.response;

import com.example.hurtownia_ISI_ZAF.model.Produkt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProduktResponse {

    private Integer id;
    private String nazwa;
    private String kategoria;
    private String jednostkaMiary;
    private Double cena;

    public ProduktResponse(Produkt produkt) {
        if (produkt != null) {
            this.id = produkt.getId();
            this.nazwa = produkt.getNazwa();
            this.kategoria = produkt.getKategoria();
            this.jednostkaMiary = produkt.getJednostkaMiary();
            this.cena = produkt.getCena();
        }
    }
}
