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
    @Column(name = "id_produkt")
    private Integer id;

    private String nazwa;
    private String kategoria;

    @Column(name = "jednostka_miary")
    private String jednostkaMiary;

    private Double cena;
}
