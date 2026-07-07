package com.NTFOODS.Api_tanty.modules.production.domain.affectation.enums;

public enum DureeAffectation {

    DEMI_JOURNEE("Demi-journée", 0.5),
    JOURNEE_COMPLETE("Journée complète", 1.0);

    private final String libelle;
    /** Coefficient multiplicateur par rapport à la production journalière de référence (8h). */
    private final double coefficient;

    DureeAffectation(String libelle, double coefficient) {
        this.libelle = libelle;
        this.coefficient = coefficient;
    }

    public String getLibelle() { return libelle; }
    public double getCoefficient() { return coefficient; }
}
