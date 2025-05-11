package com.example.hurtownia_ISI_ZAF.response;

import com.example.hurtownia_ISI_ZAF.model.ProduktWZamowieniu;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProduktWZamowieniuResponse {

    private Integer id;
    private Integer idZamowienie;
    private Integer idProdukt;
    private Integer ilosc;
    private Double cena;
    private Double rabat;
    private Double wartoscLaczna;

    public ProduktWZamowieniuResponse(ProduktWZamowieniu produktWZamowieniu) {
        this.id = produktWZamowieniu.getId();
        this.idZamowienie = produktWZamowieniu.getZamowienie().getId();
        this.idProdukt = produktWZamowieniu.getProdukt().getId();
        this.ilosc = produktWZamowieniu.getIlosc();
        this.cena = produktWZamowieniu.getCena();
        this.rabat = produktWZamowieniu.getRabat();
        this.wartoscLaczna = produktWZamowieniu.getWartoscLaczna();
    }
}
