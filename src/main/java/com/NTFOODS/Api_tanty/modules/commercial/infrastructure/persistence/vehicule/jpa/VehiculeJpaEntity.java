package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vehicule.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicules")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class VehiculeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String immatriculation;

    private String marque;
    private String modele;
    private String matriculeCommercial;

    @Column(nullable = false)
    private boolean actif;

    @Column(nullable = false)
    private double consommationMoyenneL100;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    @Version
    private Long version;
}
