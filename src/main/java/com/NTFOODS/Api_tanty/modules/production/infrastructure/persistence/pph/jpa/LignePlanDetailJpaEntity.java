package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.jpa;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lignes_plan_detail")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LignePlanDetailJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pph_id")
    private Long pphId;

    @Column(name = "code_produit", nullable = false, length = 50)
    private String codeProduit;

    @Column(name = "nom_produit", nullable = false, length = 255)
    private String nomProduit;

    @Column(name = "categorie_metier", nullable = false, length = 30)
    private String categorieMetier;

    @Column(name = "quantite_cartons", nullable = false)
    private int quantiteCartons;

    @Column(name = "nombre_unites", nullable = false)
    private int nombreUnites;

    @Column(name = "poids_unitaire_grammes", nullable = false)
    private double poidsUnitaireGrammes;

    @Column(name = "date_jour", nullable = false)
    private LocalDate dateJour;

    @Column(name = "quantite_jour", nullable = false)
    private int quantiteJour;

    @OneToMany(mappedBy = "ligneDetail", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    private List<BesoinMatierePremiereLigneJpaEntity> besoinsMatieresPremiere = new ArrayList<>();

    @Column(name = "formule_manquante", nullable = false)
    private boolean formuleManquante;

    @OneToMany(mappedBy = "ligneDetail", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    private List<BesoinEmballageLigneJpaEntity> besoinsEmballage = new ArrayList<>();

    @Column(name = "composition_emballage_manquante", nullable = false)
    private boolean compositionEmballageManquante;

    @Column(name = "futs_prevus")
    private double futsPrevus;

    @Column(name = "poids_total_grammes")
    private double poidsTotalGrammes;
}
