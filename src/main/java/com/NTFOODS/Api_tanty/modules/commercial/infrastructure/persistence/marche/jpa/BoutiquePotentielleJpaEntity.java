package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.marche.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "boutiques_potentielles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BoutiquePotentielleJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nom;

    @Column(nullable = false, length = 100)
    private String secteur;

    @Column(length = 250)
    private String adresse;

    @Column(name = "matricule_recenseur", nullable = false, length = 20)
    private String matriculeRecenseur;

    @Column(nullable = false)
    private boolean actif;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Version
    private Long version;
}
