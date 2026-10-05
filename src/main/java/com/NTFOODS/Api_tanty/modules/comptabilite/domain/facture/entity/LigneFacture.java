package com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.entity;

import java.util.Objects;

public class LigneFacture {

    private final String referenceArticle;
    private final String designation;
    private final int quantite;
    private final double prixUnitaireHT;
    private final double tauxTVA;
    private final double remise;

    public LigneFacture(String referenceArticle, String designation, int quantite,
                         double prixUnitaireHT, double tauxTVA, double remise) {
        if (quantite <= 0) throw new IllegalArgumentException("La quantité doit être positive");
        if (prixUnitaireHT < 0) throw new IllegalArgumentException("Le prix unitaire ne peut pas être négatif");
        if (remise < 0 || remise > 100) throw new IllegalArgumentException("La remise doit être entre 0 et 100");
        this.referenceArticle = Objects.requireNonNull(referenceArticle);
        this.designation = Objects.requireNonNull(designation);
        this.quantite = quantite;
        this.prixUnitaireHT = prixUnitaireHT;
        this.tauxTVA = tauxTVA;
        this.remise = remise;
    }

    public double getMontantHT() {
        return quantite * prixUnitaireHT * (1 - remise / 100.0);
    }

    public double getMontantTVA() {
        return getMontantHT() * tauxTVA / 100.0;
    }

    public double getMontantTTC() {
        return getMontantHT() + getMontantTVA();
    }

    public String getReferenceArticle() { return referenceArticle; }
    public String getDesignation() { return designation; }
    public int getQuantite() { return quantite; }
    public double getPrixUnitaireHT() { return prixUnitaireHT; }
    public double getTauxTVA() { return tauxTVA; }
    public double getRemise() { return remise; }
}
