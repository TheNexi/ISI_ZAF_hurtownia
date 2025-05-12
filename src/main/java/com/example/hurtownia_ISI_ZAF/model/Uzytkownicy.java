package com.example.hurtownia_ISI_ZAF.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "uzytkownicy")
public class Uzytkownicy {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uzytkownicy_seq")
    @SequenceGenerator(name = "uzytkownicy_seq", sequenceName = "uzytkownicy_seq", allocationSize = 1)
    @Column(name = "id_uzytkownik")
    private Integer id;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "haslo", nullable = false)
    private String haslo;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "role")
    private String role;


}
