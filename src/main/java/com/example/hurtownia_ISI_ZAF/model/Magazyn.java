package com.example.hurtownia_ISI_ZAF.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "magazyn")
public class Magazyn {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "magazyn_seq")
    @SequenceGenerator(name = "magazyn_seq", sequenceName = "magazyn_seq", allocationSize = 1)
    @Column(name = "id_magazyn")
    private Integer id;

    @Column(name = "nazwa")
    private String nazwa;

    @Column(name = "kraj")
    private String kraj;

    @Column(name = "miasto")
    private String miasto;

    @Column(name = "ulica")
    private String ulica;

    @Column(name = "pojemnosc")
    private Integer pojemnosc;
}
