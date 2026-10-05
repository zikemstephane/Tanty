package com.NTFOODS.Api_tanty.modules.production.domain.pph.entity;

import com.NTFOODS.Api_tanty.modules.production.domain.formule.entity.BesoinMatierePremiere;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.TypeEmballage;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.BesoinEmballage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Ligne détaillée d'un plan de production hebdomadaire.
 *
 * Enrichie par rapport à LignePPH avec les prévisions de production :
 * - besoins en matières premières, calculés depuis la {@link com.NTFOODS.Api_tanty.modules.production.domain.formule.aggregate.FormuleProduction}
 *   du produit — jamais une recette figée à deux variantes maïs/soja/arachide
 * - besoins en composants d'emballage, calculés depuis la composition propre
 *   au conditionnement de la ligne — jamais une hypothèse sachet=couvercle=scellé
 * - répartition par jour ouvré
 *
 * Un produit sans formule ou sans composition d'emballage est un état valide :
 * {@link #isFormuleManquante()} / {@link #isCompositionEmballageManquante()}
 * le signalent explicitement plutôt que de laisser les listes de besoins
 * vides passer inaperçues pour un « zéro » calculé.
 */
public class LignePlanDetail {

    private final String codeProduit;
    private final String nomProduit;
    private final String categorieMetier;
    private final int quantiteCartons;
    private final int nombreUnitesParConditionnement;
    private final double poidsUnitaireGrammes;
    private final List<BesoinMatierePremiere> besoinsMatieresPremiere;
    private final boolean formuleManquante;
    private final List<BesoinEmballage> besoinsEmballage;
    private final boolean compositionEmballageManquante;
    private final LocalDate dateJour;
    private final int quantiteJour;

    public LignePlanDetail(String codeProduit, String nomProduit, String categorieMetier,
                           int quantiteCartons, int nombreUnitesParConditionnement,
                           double poidsUnitaireGrammes,
                           List<BesoinMatierePremiere> besoinsMatieresPremiere, boolean formuleManquante,
                           List<BesoinEmballage> besoinsEmballage, boolean compositionEmballageManquante,
                           LocalDate dateJour, int quantiteJour) {
        this.codeProduit = Objects.requireNonNull(codeProduit);
        this.nomProduit = Objects.requireNonNull(nomProduit);
        this.categorieMetier = Objects.requireNonNull(categorieMetier);
        this.quantiteCartons = quantiteCartons;
        this.nombreUnitesParConditionnement = nombreUnitesParConditionnement;
        this.poidsUnitaireGrammes = poidsUnitaireGrammes;
        this.besoinsMatieresPremiere = besoinsMatieresPremiere == null ? List.of() : List.copyOf(besoinsMatieresPremiere);
        this.formuleManquante = formuleManquante;
        this.besoinsEmballage = besoinsEmballage == null ? List.of() : List.copyOf(besoinsEmballage);
        this.compositionEmballageManquante = compositionEmballageManquante;
        this.dateJour = dateJour;
        this.quantiteJour = quantiteJour;
    }

    public int getTotalUnites() {
        return quantiteCartons * nombreUnitesParConditionnement;
    }

    public double getTotalPoidsGrammes() {
        return getTotalUnites() * poidsUnitaireGrammes;
    }

    public double getTotalPoidsKg() {
        return getTotalPoidsGrammes() / 1000.0;
    }

    /** Somme des besoins en matières premières de cette ligne, toutes matières confondues, en kg. */
    public BigDecimal getTotalMatieresPremieresKg() {
        return besoinsMatieresPremiere.stream()
            .map(BesoinMatierePremiere::quantiteKg)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** Besoin (kg) pour une matière première donnée, 0 si elle n'apparaît pas dans la formule. */
    public BigDecimal getBesoinMatierePremiere(String matierePremiere) {
        return besoinsMatieresPremiere.stream()
            .filter(b -> b.matierePremiere().equalsIgnoreCase(matierePremiere))
            .map(BesoinMatierePremiere::quantiteKg)
            .findFirst()
            .orElse(BigDecimal.ZERO);
    }

    /** Besoin (quantité) pour un composant d'emballage donné, 0 s'il n'apparaît pas dans la composition. */
    public long getBesoinEmballage(TypeEmballage type) {
        return besoinsEmballage.stream()
            .filter(b -> b.type() == type)
            .mapToLong(BesoinEmballage::quantite)
            .findFirst()
            .orElse(0L);
    }

    public double getNombreFuts50kg() {
        double totalKg = getTotalMatieresPremieresKg().doubleValue();
        return totalKg <= 0 ? 0.0 : totalKg / 50.0;
    }

    public String getCodeProduit() { return codeProduit; }
    public String getNomProduit() { return nomProduit; }
    public String getCategorieMetier() { return categorieMetier; }
    public int getQuantiteCartons() { return quantiteCartons; }
    public int getNombreUnitesParConditionnement() { return nombreUnitesParConditionnement; }
    public double getPoidsUnitaireGrammes() { return poidsUnitaireGrammes; }
    public List<BesoinMatierePremiere> getBesoinsMatieresPremiere() { return besoinsMatieresPremiere; }
    public boolean isFormuleManquante() { return formuleManquante; }
    public List<BesoinEmballage> getBesoinsEmballage() { return besoinsEmballage; }
    public boolean isCompositionEmballageManquante() { return compositionEmballageManquante; }
    public LocalDate getDateJour() { return dateJour; }
    public int getQuantiteJour() { return quantiteJour; }
}
