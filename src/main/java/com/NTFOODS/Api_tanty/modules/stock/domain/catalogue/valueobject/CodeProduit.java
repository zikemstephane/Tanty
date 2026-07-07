package com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject;

import java.util.Objects;

/**
 * Value Object — Code métier unique d'un produit (ex : "EPI-POIVRE-BLANC").
 * Immuable, normalisé en majuscules.
 */
public final class CodeProduit {

    private final String valeur;

    private CodeProduit(String valeur) {
        this.valeur = valeur;
    }

    public static CodeProduit de(String valeur) {
        Objects.requireNonNull(valeur, "Le code produit est obligatoire");
        String normalise = valeur.trim().toUpperCase();
        if (normalise.isBlank()) {
            throw new IllegalArgumentException("Le code produit ne peut pas être vide");
        }
        if (normalise.length() > 50) {
            throw new IllegalArgumentException("Le code produit ne peut pas dépasser 50 caractères");
        }
        return new CodeProduit(normalise);
    }

    public String getValeur() { return valeur; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CodeProduit that)) return false;
        return valeur.equals(that.valeur);
    }

    @Override
    public int hashCode() { return Objects.hash(valeur); }

    @Override
    public String toString() { return valeur; }
}
