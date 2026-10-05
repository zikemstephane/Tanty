package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.prediction.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "predictions_pph")
@Getter @Setter @NoArgsConstructor
public class PredictionPPHJpaEntity {

    @Id
    @Column(name = "reference_pph", length = 255)
    private String referencePPH;

    @Column(name = "nb_futs", nullable = false)
    private int nbFuts;

    @Column(name = "poudre_kg", nullable = false)
    private double poudreKg;

    @Column(name = "date_calcul", nullable = false)
    private LocalDateTime dateCalcul;

    @Column(name = "matricule", nullable = false, length = 255)
    private String matricule;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "predictions_pph_lignes", joinColumns = @JoinColumn(name = "reference_pph"))
    private List<LigneJpa> lignes = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "predictions_pph_exclusions", joinColumns = @JoinColumn(name = "reference_pph"))
    private List<ExclusionJpa> exclusions = new ArrayList<>();

    @Embeddable
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class LigneJpa {
        @Column(name = "code_produit", length = 50)
        private String codeProduit;
        @Column(name = "designation", length = 255)
        private String designation;
        @Column(name = "composition", length = 500)
        private String composition;
        @Column(name = "poudre_allouee_kg")
        private double poudreAlloueeKg;
        @Column(name = "quantite_unites_base")
        private int quantiteUnitesBase;
        @Column(name = "quantite_cartons")
        private int quantiteCartons;
        @Column(name = "poids_unitaire_grammes")
        private double poidsUnitaireGrammes;
        @Column(name = "unites_par_carton")
        private int unitesParCarton;
    }

    @Embeddable
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class ExclusionJpa {
        @Column(name = "code_produit", length = 50)
        private String codeProduit;
        @Column(name = "motif", length = 500)
        private String motif;
    }
}
