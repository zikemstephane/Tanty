package com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.enums;

/** Type d'inventaire. */
public enum TypeInventaire {
    COMPLET("Inventaire complet"),
    TOURNANT("Inventaire tournant (par gamme/zone)"),
    CIBLE("Inventaire ciblé (1 produit/famille)"),
    SURPRISE("Inventaire surprise (DG)");

    private final String libelle;

    TypeInventaire(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() { return libelle; }
}
