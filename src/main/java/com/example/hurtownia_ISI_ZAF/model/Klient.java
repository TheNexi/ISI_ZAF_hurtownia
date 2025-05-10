package com.example.hurtownia_ISI_ZAF.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "klient")
public class Klient {

    @Id
    @Column(name = "id_klient")
    private Integer id;

    private String nazwa;
    private String nip;
    private String kraj;
    private String miasto;
    private String ulica;

    @Column(name = "kod_pocztowy")
    private String kodPocztowy;
}

