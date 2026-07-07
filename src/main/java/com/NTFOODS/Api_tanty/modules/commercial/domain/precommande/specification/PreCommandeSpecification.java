package com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.specification;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;

/** Specifications — règles de la double validation des précommandes J+1. */
public final class PreCommandeSpecification {

    private PreCommandeSpecification() {}

    public static boolean peutSoumettre(Role role) {
        return role == Role.COMMERCIAL || role == Role.ADMIN;
    }

    public static boolean peutValiderSecretaire(Role role) {
        return role == Role.SECRETAIRE || role == Role.ADMIN;
    }

    public static boolean peutValiderComptable(Role role) {
        return role == Role.COMPTABLE || role == Role.ADMIN;
    }
}
