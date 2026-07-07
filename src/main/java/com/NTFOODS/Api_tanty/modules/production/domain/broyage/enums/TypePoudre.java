package com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums;

public enum TypePoudre {

    MAIS("Maïs"),
    SOJA("Soja"),
    ARACHIDE("Arachide");

    private final String libelle;

    TypePoudre(String libelle) { this.libelle = libelle; }

    public String getLibelle() { return libelle; }
}
