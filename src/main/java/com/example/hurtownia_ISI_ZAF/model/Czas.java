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
    @Column(name = "id_czas")
    private Integer id;

    private Integer dzien;
    private Integer miesiac;
    private Integer rok;
    private String kwartal;

    @Column(name = "dzien_tygodnia")
    private String dzienTygodnia;
}
