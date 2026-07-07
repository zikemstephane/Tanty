package com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums;

public enum TypeCampagne {
    LANCEMENT_PRODUIT("Lancement de produit"),
    PROMO_SAISONNIERE("Promotion saisonnière"),
    FIDELISATION("Fidélisation client"),
    PROSPECTION("Prospection nouvelle zone"),
    COMMUNICATION("Communication institutionnelle");

    private final String libelle;

    TypeCampagne(String libelle) { this.libelle = libelle; }
    public String getLibelle() { return libelle; }
}
