package com.example.hurtownia_ISI_ZAF.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "produkt_w_zamowieniu")
public class ProduktWZamowieniu {

    @Id
    @Column(name = "id_pozycja")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_zamowienie")
    private Zamowienie zamowienie;

    @ManyToOne
    @JoinColumn(name = "id_produkt")
    private Produkt produkt;

    private Integer ilosc;
    private Double cena;
    private Double rabat;

    @Column(name = "wartosc_laczna")
    private Double wartoscLaczna;
}
