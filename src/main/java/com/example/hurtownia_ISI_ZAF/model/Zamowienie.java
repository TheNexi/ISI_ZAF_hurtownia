package com.example.hurtownia_ISI_ZAF.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "zamowienie")
public class Zamowienie {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "zamowienie_seq")
    @SequenceGenerator(name = "zamowienie_seq", sequenceName = "zamowienie_seq", allocationSize = 1)
    @Column(name = "id_zamowienie")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_klient")
    private Klient klient;

    @ManyToOne
    @JoinColumn(name = "id_czas")
    private Czas czas;

    @ManyToOne
    @JoinColumn(name = "id_dostawca")
    private Dostawca dostawca;

    @ManyToOne
    @JoinColumn(name = "id_magazyn")
    private Magazyn magazyn;

    @Column(name = "wartosc_calkowita")
    private Double wartoscCalkowita;
}

