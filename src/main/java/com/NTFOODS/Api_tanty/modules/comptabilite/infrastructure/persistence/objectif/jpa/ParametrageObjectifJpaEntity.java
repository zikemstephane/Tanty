package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.objectif.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "parametrages_objectif")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParametrageObjectifJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 60)
    private String reference;

    @Column(nullable = false)
    private int nombreJoursOuvres;

    @Column(nullable = false)
    private LocalDate dateDebut;

    private LocalDate dateFin;

    @Column(nullable = false, length = 40)
    private String matriculeAuteur;

    @Column(length = 500)
    private String motif;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Version
    private Long version;
}
