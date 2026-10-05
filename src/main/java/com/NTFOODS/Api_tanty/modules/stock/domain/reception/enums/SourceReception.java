package com.NTFOODS.Api_tanty.modules.stock.domain.reception.enums;

/** Origine d'une réception de marchandise. */
public enum SourceReception {
    FOURNISSEUR("Réception fournisseur"),
    PRODUCTION("Réception production (produits finis)");

    private final String libelle;

    SourceReception(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() { return libelle; }
}
