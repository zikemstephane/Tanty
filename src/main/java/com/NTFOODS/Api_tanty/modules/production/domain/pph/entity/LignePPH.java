package com.NTFOODS.Api_tanty.modules.production.domain.pph.entity;

import java.util.Objects;

/** Ligne d'un Plan de Production Hebdomadaire — un produit avec ses objectifs et réalisés. */
public class LignePPH {

    private final String codeProduit;
    private final String nomProduit;
    private int objectifSemaine;
    private int productionRealisee;

    public LignePPH(String codeProduit, String nomProduit, int objectifSemaine) {
        this.codeProduit = Objects.requireNonNull(codeProduit);
        this.nomProduit = Objects.requireNonNull(nomProduit);
        if (objectifSemaine <= 0) throw new IllegalArgumentException("L'objectif doit être positif");
        this.objectifSemaine = objectifSemaine;
        this.productionRealisee = 0;
    }

    public void cumulerProduction(int quantite) {
        if (quantite < 0) throw new IllegalArgumentException("La quantité cumulée ne peut pas être négative");
        this.productionRealisee += quantite;
    }

    public void modifierObjectif(int nouvelObjectif) {
        if (nouvelObjectif <= 0) throw new IllegalArgumentException("L'objectif doit être positif");
        this.objectifSemaine = nouvelObjectif;
    }

    public int tauxAvancement() {
        if (objectifSemaine == 0) return 0;
        return Math.min(100, (int) ((productionRealisee * 100.0) / objectifSemaine));
    }

    public boolean estObjectifAtteint() { return productionRealisee >= objectifSemaine; }

    public String getCodeProduit() { return codeProduit; }
    public String getNomProduit() { return nomProduit; }
    public int getObjectifSemaine() { return objectifSemaine; }
    public int getProductionRealisee() { return productionRealisee; }
}
