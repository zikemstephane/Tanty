package com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums;

/**
 * Les 4 gammes commerciales officielles de NTFoods (cf. formation des
 * nouveaux commerciaux). Vocabulaire unique partagé entre le catalogue
 * produit (Produit.gamme, stocké en texte via {@link #name()}), les
 * objectifs commerciaux (ObjectifCommercial.objectifsParGammeFCFA) et le
 * calcul des primes hebdomadaires (règle d'or 80/75).
 */
public enum GammeProduit {

    BOUILLIES("Bouillies de Soja"),
    A_GRIGNOTER("À Grignoter"),
    INGREDIENTS("Ingrédients Culinaires"),
    CHOCO("Choco");

    private final String libelle;

    GammeProduit(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() { return libelle; }
}
