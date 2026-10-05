package com.NTFOODS.Api_tanty.modules.stock.domain.retour.enums;

/**
 * État d'un constat d'invendus.
 *
 * <p>Le constat se fait en deux temps : le commercial déclare ce qu'il rapporte,
 * le gestionnaire compte ensuite ce qu'il constate réellement. Les deux quantités
 * sont conservées séparément, l'écart entre elles étant précisément l'information
 * recherchée.</p>
 */
public enum StatutBonRetour {

    /** Le commercial a annoncé ses invendus ; rien n'a encore été vérifié. */
    DECLARE("Déclaré par le commercial"),

    /** Le gestionnaire a compté ; l'écart avec la déclaration est arrêté. */
    COMPTE("Compté par le gestionnaire");

    private final String libelle;

    StatutBonRetour(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
