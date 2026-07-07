package com.NTFOODS.Api_tanty.modules.production.domain.affectation.enums;

public enum UniteProduction {
    KG("kg"),
    SACHET("sachet(s)"),
    CARTON("carton(s)"),
    SCEAU("sceau(x)"),
    ETUI("étui(s)"),
    ETIQUETTE("étiquette(s)"),
    REGIME("régime(s)"),
    FUT("fût(s)"),
    AUCUNE("—");

    private final String libelle;

    UniteProduction(String libelle) { this.libelle = libelle; }

    public String getLibelle() { return libelle; }
}
