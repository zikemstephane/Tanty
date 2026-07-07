package com.NTFOODS.Api_tanty.modules.production.domain.pph.enums;

/**
 * Recette de bouillie utilisée pour le calcul des quantités de poudre.
 */
public enum RecetteBouillie {

    SIMPLE_PORRIDGE("Simple Porridge", 40.0, 9.0, 1.0),
    PEANUT_PORRIDGE("Peanut Porridge", 40.0, 1.0, 9.0);

    private final String libelle;
    private final double proportionMais;
    private final double proportionSoja;
    private final double proportionArachide;

    RecetteBouillie(String libelle, double proportionMais, double proportionSoja, double proportionArachide) {
        this.libelle = libelle;
        this.proportionMais = proportionMais;
        this.proportionSoja = proportionSoja;
        this.proportionArachide = proportionArachide;
    }

    public String getLibelle() { return libelle; }
    public double getProportionMais() { return proportionMais; }
    public double getProportionSoja() { return proportionSoja; }
    public double getProportionArachide() { return proportionArachide; }

    public double getTotalProportions() {
        return proportionMais + proportionSoja + proportionArachide;
    }

    /**
     * Détermine la recette en fonction du nom du produit.
     * Peanut Porridge si le produit contient explicitement "arachide".
     * Simple Porridge par défaut.
     */
    public static RecetteBouillie depuisDesignation(String designation) {
        if (designation == null) return SIMPLE_PORRIDGE;
        String normalise = designation.toUpperCase();
        return normalise.contains("ARACHIDE") ? PEANUT_PORRIDGE : SIMPLE_PORRIDGE;
    }
}
