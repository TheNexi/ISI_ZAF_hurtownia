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
    @Column(name = "id_dostawca")
    private Integer id;

    private String nazwa;
    private String kraj;
    private String miasto;
    private String nip;
    private String telefon;
}
