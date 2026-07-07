package com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums;

public enum OperationBroyage {

    ECRASAGE("Écrasage"),
    TRIAGE("Triage"),
    TAMISAGE("Tamisage"),
    DEPULPAGE("Dépulpage");

    private final String libelle;

    OperationBroyage(String libelle) { this.libelle = libelle; }

    public String getLibelle() { return libelle; }
}
