package com.NTFOODS.Api_tanty.modules.production.domain.fiche.entity;

import java.util.Objects;

public class LigneFicheProduit {

    private final String codeProduit;
    private final String nomProduit;
    private final double quantiteProduite;

    public LigneFicheProduit(String codeProduit, String nomProduit, double quantiteProduite) {
        this.codeProduit = Objects.requireNonNull(codeProduit);
        this.nomProduit = Objects.requireNonNull(nomProduit);
        if (quantiteProduite < 0) throw new IllegalArgumentException("La quantité produite ne peut pas être négative");
        this.quantiteProduite = quantiteProduite;
    }

    public String getCodeProduit() { return codeProduit; }
    public String getNomProduit() { return nomProduit; }
    public double getQuantiteProduite() { return quantiteProduite; }
}
