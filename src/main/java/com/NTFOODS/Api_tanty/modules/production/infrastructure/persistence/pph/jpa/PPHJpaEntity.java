package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.jpa;

import com.NTFOODS.Api_tanty.modules.production.domain.pph.enums.StatutPPH;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "plans_production_hebdomadaires")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PPHJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String referencePPH;

    private String referenceBC;

    @Column(nullable = false)
    private String semaine;

    @Column(nullable = false)
    private LocalDate dateDebut;

    @Column(nullable = false)
    private LocalDate dateFin;

    @Column(nullable = false)
    private String matriculeChefProduction;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "pph_id")
    @Fetch(FetchMode.SUBSELECT)
    private List<LignePPHJpaEntity> lignes;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "pph_id")
    @Fetch(FetchMode.SUBSELECT)
    private List<LignePlanDetailJpaEntity> lignesDetaillees;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "pph_id")
    @Fetch(FetchMode.SUBSELECT)
    private List<RepartitionJourJpaEntity> repartitionJours;

    @Column(name = "date_soumission")
    private LocalDate dateSoumission;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPPH statut;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    @Version
    private Long version;
}
