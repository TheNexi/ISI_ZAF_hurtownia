package com.example.hurtownia_ISI_ZAF.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "klient")
public class Klient {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "klient_seq")
    @SequenceGenerator(name = "klient_seq", sequenceName = "klient_seq", allocationSize = 1)
    @Column(name = "id_klient")
    private Integer id;

    @Column(name = "nazwa")
    private String nazwa;

    @Column(name = "nip")
    private String nip;

    @Column(name = "kraj")
    private String kraj;

    @Column(name = "miasto")
    private String miasto;

    @Column(name = "ulica")
    private String ulica;

    @Column(name = "kod_pocztowy")
    private String kodPocztowy;
}
