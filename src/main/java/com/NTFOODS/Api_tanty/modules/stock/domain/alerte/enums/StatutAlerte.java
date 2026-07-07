package com.NTFOODS.Api_tanty.modules.stock.domain.alerte.enums;

/** Statut de traitement d'une alerte. */
public enum StatutAlerte {
    NOUVELLE("Nouvelle"),
    LUE("Lue"),
    TRAITEE("Traitée");

    private final String libelle;

    StatutAlerte(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() { return libelle; }
}
