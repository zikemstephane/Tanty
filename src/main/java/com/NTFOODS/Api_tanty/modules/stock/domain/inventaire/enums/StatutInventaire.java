package com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.enums;

/**
 * Cycle d'une session d'inventaire.
 * OUVERT (saisie des comptages) → COMPTE → VALIDE (par le DG, stock ajusté) | CLOTURE.
 */
public enum StatutInventaire {
    OUVERT("Ouvert"),
    COMPTE("Compté"),
    VALIDE("Validé"),
    CLOTURE("Clôturé");

    private final String libelle;

    StatutInventaire(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() { return libelle; }
}
