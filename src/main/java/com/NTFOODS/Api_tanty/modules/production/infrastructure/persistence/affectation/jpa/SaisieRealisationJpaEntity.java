package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.affectation.jpa;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "saisies_realisation_poste")
@Getter @Setter @NoArgsConstructor
public class SaisieRealisationJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "affectation_id", nullable = false)
    private AffectationJournaliereJpaEntity affectation;

    @Column(nullable = false, length = 60)
    private String poste;

    @Column(name = "quantite_realisee", nullable = false)
    private double quantiteRealisee;

    @Column(name = "objectif_ajuste", nullable = false)
    private double objectifAjuste;

    @Column(name = "nb_regimes_epluches")
    private Integer nbRegimesEpluches;

    @Column(name = "nb_regimes_decoupes")
    private Integer nbRegimesDecoupes;

    @Column(name = "nb_sceaux_etiquetes")
    private Integer nbSceauxEtiquetes;

    @Column(name = "nb_etiquettes_datees")
    private Integer nbEtiquettesDatees;

    @Column(name = "nb_sachets_dates")
    private Integer nbSachetsDates;

    @Column(name = "nb_cartons_produits")
    private Integer nbCartonsProduits;

    @Column(name = "kg_chips_produits")
    private Double kgChipsProduits;

    @Column(name = "warnings", length = 1000)
    private String warningsJson;

    @Column(name = "date_enregistrement", nullable = false)
    private LocalDateTime dateEnregistrement;

    @OneToMany(mappedBy = "saisie", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ProductionIndividuelleJpaEntity> productionsIndividuelles = new ArrayList<>();
}
