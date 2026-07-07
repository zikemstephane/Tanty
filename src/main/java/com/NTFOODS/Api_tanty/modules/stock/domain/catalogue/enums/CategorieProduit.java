package com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums;

/**
 * Catégorie d'un produit dans le catalogue.
 *
 * Détermine le circuit de réception et de validation :
 * - MATIERE_PREMIERE : réception fournisseur, 2e validation par le Chef de Production
 * - CONSOMMABLE      : réception fournisseur, 2e validation par le Contrôleur Général
 * - PRODUIT_FINI     : réception depuis la Production (déclaration de lot)
 */
public enum CategorieProduit {

    MATIERE_PREMIERE("Matière première"),
    CONSOMMABLE("Consommable"),
    PRODUIT_FINI("Produit fini");

    private final String libelle;

    CategorieProduit(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public boolean estReceptionFournisseur() {
        return this == MATIERE_PREMIERE || this == CONSOMMABLE;
    }
}
