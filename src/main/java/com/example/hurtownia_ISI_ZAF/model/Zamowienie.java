package com.example.hurtownia_ISI_ZAF.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "zamowienie")
public class Zamowienie {
    public enum StatusPlatnosci {
        PENDING,
        SUCCESS,
        FAILED,
        OFFLINE
    }
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "zamowienie_seq")
    @SequenceGenerator(name = "zamowienie_seq", sequenceName = "zamowienie_seq", allocationSize = 1)
    @Column(name = "id_zamowienie")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_klient")
    private Klient klient;

    @ManyToOne
    @JoinColumn(name = "id_czas")
    private Czas czas;

    @ManyToOne
    @JoinColumn(name = "id_dostawca")
    private Dostawca dostawca;

    @ManyToOne
    @JoinColumn(name = "id_magazyn")
    private Magazyn magazyn;

    @Column(name = "wartosc_calkowita")
    private Double wartoscCalkowita;

    @Column(name = "status_platnosci")
    @Enumerated(EnumType.STRING)
    private StatusPlatnosci statusPlatnosci;

    @OneToMany(mappedBy = "zamowienie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProduktWZamowieniu> produktyWZamowieniu;

}

