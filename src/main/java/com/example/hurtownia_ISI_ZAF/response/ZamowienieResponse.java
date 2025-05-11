package com.example.hurtownia_ISI_ZAF.response;

import com.example.hurtownia_ISI_ZAF.model.Zamowienie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ZamowienieResponse {

    private Integer id;
    private Integer idKlient;
    private Integer idCzas;
    private Integer idDostawca;
    private Integer idMagazyn;
    private Double wartoscCalkowita;

    public ZamowienieResponse(Zamowienie zamowienie) {
        if (zamowienie != null) {
            this.id = zamowienie.getId();
            this.idKlient = zamowienie.getKlient().getId();
            this.idCzas = zamowienie.getCzas().getId();
            this.idDostawca = zamowienie.getDostawca().getId();
            this.idMagazyn = zamowienie.getMagazyn().getId();
            this.wartoscCalkowita = zamowienie.getWartoscCalkowita();
        }
    }
}
