package com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.entity;

import java.util.Objects;

public class LignePreCommande {

    private final String codeProduit;
    private final String designationProduit;
    private final String conditionnement;
    private int quantiteDemandee;
    private int quantiteValidee;

    public LignePreCommande(String codeProduit, String designationProduit, String conditionnement,
                             int quantiteDemandee) {
        this.codeProduit = Objects.requireNonNull(codeProduit);
        this.designationProduit = Objects.requireNonNull(designationProduit);
        this.conditionnement = Objects.requireNonNull(conditionnement);
        if (quantiteDemandee <= 0) throw new IllegalArgumentException("La quantité demandée doit être positive");
        this.quantiteDemandee = quantiteDemandee;
        this.quantiteValidee = quantiteDemandee;
    }

    public void ajusterQuantiteValidee(int quantite) {
        if (quantite < 0) throw new IllegalArgumentException("La quantité validée ne peut pas être négative");
        this.quantiteValidee = quantite;
    }

    public String getCodeProduit() { return codeProduit; }
    public String getDesignationProduit() { return designationProduit; }
    public String getConditionnement() { return conditionnement; }
    public int getQuantiteDemandee() { return quantiteDemandee; }
    public int getQuantiteValidee() { return quantiteValidee; }
}
