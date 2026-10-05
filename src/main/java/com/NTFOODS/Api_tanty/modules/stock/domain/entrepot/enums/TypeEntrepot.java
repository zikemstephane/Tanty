package com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums;

/**
 * Type d'entrepôt — les 3 niveaux hiérarchiques strictement séparés.
 *
 * PRODUCTION → CENTRAL → TAMPON → MOBILE (commerciaux).
 * Aucune sortie directe du CENTRAL vers les commerciaux : tout passe par le TAMPON.
 */
public enum TypeEntrepot {

    CENTRAL("Stock central"),
    TAMPON("Stock tampon"),
    MOBILE("Stock mobile commercial");

    private final String libelle;

    TypeEntrepot(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() { return libelle; }

    public boolean estMobile() {
        return this == MOBILE;
    }
}
