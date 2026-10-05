package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.versement.jpa;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.enums.StatutVersement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.enums.TypeVersement;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "versements")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class VersementJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String referenceVersement;

    @Column(nullable = false)
    private String matriculeCommercial;

    @Column(nullable = false)
    private double montantAttendu;

    @Column(nullable = false)
    private double cashVerse;

    @Column(nullable = false)
    private double ecart;

    @Column(nullable = false)
    private boolean alerteRouge;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeVersement typeVersement;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutVersement statut;

    @Column(columnDefinition = "TEXT")
    private String justificationEcart;

    private String matriculeValidateur;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    @Version
    private Long version;
}
