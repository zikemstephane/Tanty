package com.NTFOODS.Api_tanty.modules.commercial.domain.vente.entity;

import java.util.Objects;

public class LigneVente {

    private final String codeProduit;
    private final String designationProduit;
    private final String conditionnement;
    private final int quantite;
    private final double prixUnitaire;
    private double remise;

    public LigneVente(String codeProduit, String designationProduit, String conditionnement,
                      int quantite, double prixUnitaire, double remise) {
        this.codeProduit = Objects.requireNonNull(codeProduit);
        this.designationProduit = Objects.requireNonNull(designationProduit);
        this.conditionnement = Objects.requireNonNull(conditionnement);
        if (quantite <= 0) throw new IllegalArgumentException("La quantité doit être positive");
        this.quantite = quantite;
        if (prixUnitaire < 0) throw new IllegalArgumentException("Le prix ne peut pas être négatif");
        this.prixUnitaire = prixUnitaire;
        this.remise = (remise < 0 || remise > 100) ? 0 : remise;
    }

    public double montantHT() {
        return quantite * prixUnitaire * (1 - remise / 100);
    }

    public String getCodeProduit() { return codeProduit; }
    public String getDesignationProduit() { return designationProduit; }
    public String getConditionnement() { return conditionnement; }
    public int getQuantite() { return quantite; }
    public double getPrixUnitaire() { return prixUnitaire; }
    public double getRemise() { return remise; }
}
