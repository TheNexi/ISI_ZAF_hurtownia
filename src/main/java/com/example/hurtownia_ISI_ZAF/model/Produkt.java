package com.example.hurtownia_ISI_ZAF.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "produkt")
public class Produkt {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "produkt_seq")
    @SequenceGenerator(name = "produkt_seq", sequenceName = "produkt_seq", allocationSize = 1)
    @Column(name = "id_produkt")
    private Integer id;

    @Column(name = "nazwa")
    private String nazwa;

    @Column(name = "kategoria")
    private String kategoria;

    @Column(name = "jednostka_miary")
    private String jednostkaMiary;

    @Column(name = "cena")
    private Double cena;
}
