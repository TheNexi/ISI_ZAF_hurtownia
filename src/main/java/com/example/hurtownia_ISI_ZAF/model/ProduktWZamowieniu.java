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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "produkt_w_zamowieniu_seq")
    @SequenceGenerator(name = "produkt_w_zamowieniu_seq", sequenceName = "produkt_w_zamowieniu_seq", allocationSize = 1)
    @Column(name = "id_pozycja")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_zamowienie")
    private Zamowienie zamowienie;

    @ManyToOne
    @JoinColumn(name = "id_produkt")
    private Produkt produkt;

    @Column(name = "ilosc")
    private Integer ilosc;

    @Column(name = "cena")
    private Double cena;

    @Column(name = "rabat")
    private Double rabat;

    @Column(name = "wartosc_laczna")
    private Double wartoscLaczna;
}
