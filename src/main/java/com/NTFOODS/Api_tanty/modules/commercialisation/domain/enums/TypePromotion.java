package com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums;

public enum TypePromotion {
    POURCENTAGE("Réduction en pourcentage"),
    MONTANT_FIXE("Réduction en montant fixe (FCFA)"),
    GRATUITE("Produit offert (achat X, obtenez Y)"),
    REMISE_VOLUME("Remise sur volume (paliers)");

    private final String libelle;

    TypePromotion(String libelle) { this.libelle = libelle; }
    public String getLibelle() { return libelle; }
}
