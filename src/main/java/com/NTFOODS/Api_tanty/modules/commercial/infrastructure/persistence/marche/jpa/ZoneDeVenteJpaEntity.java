package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.marche.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "zones_vente")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ZoneDeVenteJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @Column(nullable = false)
    private String type;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private int clientsPotentiels;

    @Column(nullable = false)
    private boolean actif;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    @Version
    private Long version;
}
