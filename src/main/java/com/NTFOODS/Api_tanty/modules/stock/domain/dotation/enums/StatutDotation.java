package com.NTFOODS.Api_tanty.modules.stock.domain.dotation.enums;

/**
 * Cycle d'une demande de dotation commerciale.
 * SOUMISE (le soir, pour le lendemain) → APPROUVEE (tampon débité, mobile crédité) | REFUSEE.
 */
public enum StatutDotation {
    SOUMISE("Soumise"),
    APPROUVEE("Approuvée"),
    REFUSEE("Refusée");

    private final String libelle;

    StatutDotation(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() { return libelle; }
}
