package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.carburant.jpa;

import com.NTFOODS.Api_tanty.modules.commercial.domain.carburant.enums.StatutCarburant;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "demandes_carburant")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CarburantJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String referenceCarburant;

    @Column(nullable = false)
    private String matriculeCommercial;

    @Column(nullable = false)
    private String immatriculation;

    @Column(length = 100)
    private String stationPartenaire;

    @Column(nullable = false)
    private int kilometrage;

    @Column(nullable = false)
    private double litresDemandes;

    @Column(nullable = false)
    private double montant;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutCarburant statut;

    @Column(columnDefinition = "TEXT")
    private String signatureCommercial;

    @Column(columnDefinition = "TEXT")
    private String signatureSuperviseur;

    private String motifRefus;

    @Column(length = 500)
    private String photoCompteur;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    @Version
    private Long version;
}
