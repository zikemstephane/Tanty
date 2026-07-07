package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.decaissement.jpa;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.enums.StatutDecaissement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.enums.TypeDepense;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "decaissements")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DecaissementJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroDecaissement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeDepense typeDepense;

    @Column(nullable = false)
    private String matriculeProposant;

    private String matriculeValidateurCG;
    private String matriculeValidateurDG;
    private String matriculeExecuteur;

    @Column(nullable = false)
    private double montantFCFA;

    @Column(nullable = false)
    private String beneficiaire;

    @Column(nullable = false)
    private LocalDate dateDepense;

    @Column(columnDefinition = "TEXT")
    private String motif;

    @Column(columnDefinition = "TEXT")
    private String motifAnnulation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutDecaissement statut;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    @Version
    private Long version;
}
