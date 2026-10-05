package com.NTFOODS.Api_tanty.modules.stock.domain.lot.enums;

/** États d'un lot de stock au fil de sa vie. */
public enum StatutLot {

    DISPONIBLE("Disponible"),
    RESERVE("Réservé"),
    QUARANTAINE("En quarantaine"),
    PERIME("Périmé"),
    EPUISE("Épuisé"),
    /** Retiré du suivi. Le lot reste en base : on ne supprime pas une trace. */
    RETIRE("Retiré");

    private final String libelle;

    StatutLot(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    /** Un lot périmé, épuisé, retiré ou en quarantaine ne peut plus être consommé. */
    public boolean permetConsommation() {
        return this == DISPONIBLE || this == RESERVE;
    }
}
