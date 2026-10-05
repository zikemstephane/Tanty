package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.affectation.jpa;

import com.NTFOODS.Api_tanty.modules.production.domain.affectation.enums.StatutAffectation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "affectations_journalieres",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_affectation_pph_date",
        columnNames = {"reference_pph", "date"}))
@Getter @Setter @NoArgsConstructor
public class AffectationJournaliereJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference_pph", nullable = false, length = 30)
    private String referencePPH;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "matricule_agent", nullable = false, length = 20)
    private String matriculeAgent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutAffectation statut;

    @OneToMany(mappedBy = "affectation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<LigneAffectationJpaEntity> lignes = new ArrayList<>();

    @OneToMany(mappedBy = "affectation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SaisieRealisationJpaEntity> saisies = new ArrayList<>();

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Version
    private Long version;
}
