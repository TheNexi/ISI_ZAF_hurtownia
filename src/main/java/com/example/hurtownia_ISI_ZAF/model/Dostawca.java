package com.example.hurtownia_ISI_ZAF.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "dostawca")
public class Dostawca {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dostawca_seq")
    @SequenceGenerator(name = "dostawca_seq", sequenceName = "dostawca_seq", allocationSize = 1)
    @Column(name = "id_dostawca")
    private Integer id;

    @Column(name = "nazwa")
    private String nazwa;

    @Column(name = "kraj")
    private String kraj;

    @Column(name = "miasto")
    private String miasto;

    @Column(name = "nip")
    private String nip;

    @Column(name = "telefon")
    private String telefon;
}
