package com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums;

public enum TypeVente {
    COMPTANT,
    CREDIT,
    AVARIE,
    DON,
    DOTATION,
    /**
     * Reprise d'un produit au client contre remise d'un produit de même nature.
     * Sans encaissement : le stock mobile perd le produit remis et récupère le
     * produit repris.
     */
    ECHANGE
}
