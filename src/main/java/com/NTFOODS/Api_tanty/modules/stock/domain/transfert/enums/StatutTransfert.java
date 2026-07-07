package com.NTFOODS.Api_tanty.modules.stock.domain.transfert.enums;

/** Cycle d'un bon de transfert central → tampon. */
public enum StatutTransfert {
    CREE("Créé"),
    EXECUTE("Exécuté"),
    ANNULE("Annulé");

    private final String libelle;

    StatutTransfert(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() { return libelle; }
}
