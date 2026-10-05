package com.NTFOODS.Api_tanty.modules.rp.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "rp_grands_comptes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GrandCompteJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String type;

    private String contact;
    private String telephone;

    @Column(nullable = false)
    private double caMensuel;

    private LocalDate derniereCommande;
    private LocalDate prochaineRelance;

    @Column(nullable = false)
    private String statut;
}
