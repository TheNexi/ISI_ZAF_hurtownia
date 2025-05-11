package com.example.hurtownia_ISI_ZAF.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "czas")
public class Czas {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "czas_seq")
    @SequenceGenerator(name = "czas_seq", sequenceName = "czas_seq", allocationSize = 1)
    @Column(name = "id_czas")
    private Integer id;

    @Column(name = "dzien")
    private Integer dzien;

    @Column(name = "miesiac")
    private Integer miesiac;

    @Column(name = "rok")
    private Integer rok;

    @Column(name = "kwartal")
    private String kwartal;

    @Column(name = "dzien_tygodnia")
    private String dzienTygodnia;
}
