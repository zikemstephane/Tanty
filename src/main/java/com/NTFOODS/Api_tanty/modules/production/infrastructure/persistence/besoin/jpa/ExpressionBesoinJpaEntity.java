package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.besoin.jpa;

import com.NTFOODS.Api_tanty.modules.production.domain.besoin.enums.StatutBesoin;
import com.NTFOODS.Api_tanty.modules.production.domain.besoin.enums.TypeBesoin;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "expressions_besoin",
    uniqueConstraints = @UniqueConstraint(columnNames = {"numero"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ExpressionBesoinJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    private String referencePPH;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeBesoin type;

    @Column(nullable = false)
    private String codeProduit;

    @Column(nullable = false)
    private String designationProduit;

    @Column(nullable = false)
    private double quantiteDemandee;

    @Column(nullable = false)
    private String unite;

    @Column(nullable = false)
    private String matriculeDemandeur;

    @Column(nullable = false)
    private LocalDate dateBesoin;

    private String motif;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatutBesoin statut;

    private String matriculeValidateur;
    private String motifRejet;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    @Version
    private Long version;
}
