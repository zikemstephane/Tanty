package com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.entity;

import java.util.Objects;

public class LigneCommandeDistance {

    private final String codeProduit;
    private final String designation;
    private final String conditionnement;
    private final int quantite;
    private final double prixUnitaire;

    public LigneCommandeDistance(String codeProduit, String designation, String conditionnement,
                                  int quantite, double prixUnitaire) {
        this.codeProduit = Objects.requireNonNull(codeProduit);
        this.designation = Objects.requireNonNull(designation);
        this.conditionnement = Objects.requireNonNull(conditionnement);
        if (quantite <= 0) throw new IllegalArgumentException("La quantité doit être positive");
        this.quantite = quantite;
        if (prixUnitaire < 0) throw new IllegalArgumentException("Le prix ne peut pas être négatif");
        this.prixUnitaire = prixUnitaire;
    }

    public double montantHT() { return quantite * prixUnitaire; }

    public String getCodeProduit() { return codeProduit; }
    public String getDesignation() { return designation; }
    public String getConditionnement() { return conditionnement; }
    public int getQuantite() { return quantite; }
    public double getPrixUnitaire() { return prixUnitaire; }
}
