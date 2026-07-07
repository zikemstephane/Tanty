package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.dotation.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dotations",
    uniqueConstraints = @UniqueConstraint(name = "uq_dotation_numero", columnNames = "numero"),
    indexes = {
        @Index(name = "idx_dotation_statut", columnList = "statut"),
        @Index(name = "idx_dotation_commercial", columnList = "matricule_commercial"),
        @Index(name = "idx_dotation_effet", columnList = "date_effet")
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DotationJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", nullable = false, unique = true, length = 30)
    private String numero;

    @Column(name = "matricule_commercial", nullable = false, length = 20)
    private String matriculeCommercial;

    @Column(name = "date_dotation", nullable = false)
    private LocalDate dateDotation;

    @Column(name = "date_effet", nullable = false)
    private LocalDate dateEffet;

    @Column(name = "statut", nullable = false, length = 20)
    private String statut;

    @OneToMany(mappedBy = "dotation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<LigneDotationJpaEntity> lignes = new ArrayList<>();

    @Column(name = "matricule_gestionnaire", length = 20)
    private String matriculeGestionnaire;

    @Column(name = "motif_refus", length = 500)
    private String motifRefus;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_decision")
    private LocalDateTime dateDecision;

    @Version
    private Long version;
}
