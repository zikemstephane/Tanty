package com.NTFOODS.Api_tanty.modules.production.domain.besoin.enums;

public enum StatutBesoin {
    EN_ATTENTE("En attente"),
    VALIDE("Validé"),
    REJETE("Rejeté");

    private final String libelle;

    StatutBesoin(String libelle) { this.libelle = libelle; }

    public String getLibelle() { return libelle; }
}
