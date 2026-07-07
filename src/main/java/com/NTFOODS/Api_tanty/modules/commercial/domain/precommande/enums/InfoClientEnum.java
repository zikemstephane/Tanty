package com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.enums;

import java.util.Set;

/**
 * Codes terrain à reproduire exactement (§5.9).
 *
 * Les codes marqués REQUIRE_QUANTITY exigent un champ quantité associé
 * obligatoire côté validation serveur.
 */
public enum InfoClientEnum {
    Ab("Absence responsable commande", false),
    NC("Nouveau client", false),
    PA("Pas d'argent", false),
    VPC("Vend produits concurrents", false),
    NVP("Ne vend plus", false),
    M("Mévente", false),
    PR("Promotion", false),
    F("Fermé", false),
    EV("Emballage vide", true),
    AV("Avaries", true),
    AF("Affiche", true),
    S("Seau", false);

    private static final Set<InfoClientEnum> REQUIRING_QUANTITY = Set.of(EV, AV, AF);

    private final String libelle;
    private final boolean requireQuantity;

    InfoClientEnum(String libelle, boolean requireQuantity) {
        this.libelle = libelle;
        this.requireQuantity = requireQuantity;
    }

    public String getLibelle() { return libelle; }
    public boolean isRequireQuantity() { return requireQuantity; }

    public static boolean requiresQuantity(InfoClientEnum code) {
        return REQUIRING_QUANTITY.contains(code);
    }
}
