package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    @Column(name = "recette", length = 30)
    private String recette;

    @Column(name = "date_jour", nullable = false)
    private LocalDate dateJour;

    @Column(name = "quantite_jour", nullable = false)
    private int quantiteJour;

    @Column(name = "poudre_mais_kg")
    private double poudreMaisKg;

    @Column(name = "poudre_soja_kg")
    private double poudreSojaKg;

    @Column(name = "poudre_arachide_kg")
    private double poudreArachideKg;

    @Column(name = "futs_prevus")
    private int futsPrevus;

    @Column(name = "sachets_prevus")
    private int sachetsPrevus;

    @Column(name = "couvercles_prevus")
    private int couverclesPrevus;

    @Column(name = "scelles_prevus")
    private int scellesPrevus;

    @Column(name = "poids_total_grammes")
    private double poidsTotalGrammes;
}
