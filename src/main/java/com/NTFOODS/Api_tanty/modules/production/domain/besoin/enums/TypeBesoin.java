package com.NTFOODS.Api_tanty.modules.production.domain.besoin.enums;

public enum TypeBesoin {
    MATIERE_PREMIERE("Matière première"),
    EMBALLAGE("Emballage"),
    CONSOMMABLE("Consommable");

    private final String libelle;

    TypeBesoin(String libelle) { this.libelle = libelle; }

    public String getLibelle() { return libelle; }
}
