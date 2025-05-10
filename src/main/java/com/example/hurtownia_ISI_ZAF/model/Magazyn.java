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
    @Column(name = "id_magazyn")
    private Integer id;

    private String nazwa;
    private String kraj;
    private String miasto;
    private String ulica;
    private Integer pojemnosc;
}
