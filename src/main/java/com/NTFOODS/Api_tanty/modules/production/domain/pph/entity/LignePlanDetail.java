package com.NTFOODS.Api_tanty.modules.production.domain.pph.entity;

import com.NTFOODS.Api_tanty.modules.production.domain.pph.enums.RecetteBouillie;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Ligne détaillée d'un plan de production hebdomadaire.
 *
 * Enrichie par rapport à LignePPH avec les prévisions de production :
 * - quantité de poudre (maïs, soja, arachide) pour les produits BOUILLIE
 * - nombre de fûts de 50 kg prévus
 * - emballages plastiques (sachets, couvercles, scellés)
 * - quantité totale en grammes pour les produits A GRIGNOTER
 * - répartition par jour ouvré
 */
public class LignePlanDetail {

    private final String codeProduit;
    private final String nomProduit;
    private final String categorieMetier;
    private final int quantiteCartons;
    private final int nombreUnitesParConditionnement;
    private final double poidsUnitaireGrammes;
    private final RecetteBouillie recette;
    private final LocalDate dateJour;
    private final int quantiteJour;

    public LignePlanDetail(String codeProduit, String nomProduit, String categorieMetier,
                           int quantiteCartons, int nombreUnitesParConditionnement,
                           double poidsUnitaireGrammes, RecetteBouillie recette,
                           LocalDate dateJour, int quantiteJour) {
        this.codeProduit = Objects.requireNonNull(codeProduit);
        this.nomProduit = Objects.requireNonNull(nomProduit);
        this.categorieMetier = Objects.requireNonNull(categorieMetier);
        this.quantiteCartons = quantiteCartons;
        this.nombreUnitesParConditionnement = nombreUnitesParConditionnement;
        this.poidsUnitaireGrammes = poidsUnitaireGrammes;
        this.recette = recette;
        this.dateJour = dateJour;
        this.quantiteJour = quantiteJour;
    }

    public int getTotalUnites() {
        return quantiteCartons * nombreUnitesParConditionnement;
    }

    public int getTotalSachets() {
        return getTotalUnites();
    }

    public int getTotalCouvercles() {
        return getTotalUnites();
    }

    public int getTotalScelles() {
        return getTotalUnites();
    }

    public double getTotalPoidsGrammes() {
        return getTotalUnites() * poidsUnitaireGrammes;
    }

    public double getTotalPoidsKg() {
        return getTotalPoidsGrammes() / 1000.0;
    }

    public double getPoudreMaisKg() {
        if (recette == null) return 0;
        return getTotalPoidsKg() * (recette.getProportionMais() / recette.getTotalProportions());
    }

    public double getPoudreSojaKg() {
        if (recette == null) return 0;
        return getTotalPoidsKg() * (recette.getProportionSoja() / recette.getTotalProportions());
    }

    public double getPoudreArachideKg() {
        if (recette == null) return 0;
        return getTotalPoidsKg() * (recette.getProportionArachide() / recette.getTotalProportions());
    }

    public int getNombreFuts50kg() {
        double totalKg = getTotalPoidsKg();
        if (totalKg <= 0) return 0;
        return (int) Math.ceil(totalKg / 50.0);
    }

    public String getCodeProduit() { return codeProduit; }
    public String getNomProduit() { return nomProduit; }
    public String getCategorieMetier() { return categorieMetier; }
    public int getQuantiteCartons() { return quantiteCartons; }
    public int getNombreUnitesParConditionnement() { return nombreUnitesParConditionnement; }
    public double getPoidsUnitaireGrammes() { return poidsUnitaireGrammes; }
    public RecetteBouillie getRecette() { return recette; }
    public LocalDate getDateJour() { return dateJour; }
    public int getQuantiteJour() { return quantiteJour; }
}
