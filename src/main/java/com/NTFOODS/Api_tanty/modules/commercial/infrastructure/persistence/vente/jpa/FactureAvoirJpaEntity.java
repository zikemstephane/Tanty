package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa;

import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.FactureAvoir;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "factures_avoir")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FactureAvoirJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroAvoir;

    @Column(nullable = false)
    private String numeroVenteOriginale;

    @Column(nullable = false)
    private String matriculeCommercial;

    @Column(nullable = false)
    private String codeClient;

    @Column(nullable = false)
    private String matriculeComptable;

    @Column(nullable = false)
    private double montantHT;

    @Column(nullable = false)
    private double montantTVA;

    @Column(nullable = false)
    private double montantTTC;

    @Column(nullable = false)
    private String motif;

    @Column(nullable = false)
    private LocalDate dateAvoir;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FactureAvoir.StatutAvoir statut;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateValidation;

    @Version
    private Long version;
}
