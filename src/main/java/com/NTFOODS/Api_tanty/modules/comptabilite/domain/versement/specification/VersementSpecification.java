package com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.specification;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;

/** Specifications — règles d'accès aux versements. */
public final class VersementSpecification {

    private VersementSpecification() {}

    public static boolean peutEnregistrer(Role role) {
        return role == Role.SECRETAIRE || role == Role.COMPTABLE || role == Role.ADMIN;
    }

    public static boolean peutValider(Role role) {
        return role == Role.COMPTABLE || role == Role.ADMIN;
    }

    public static boolean peutJustifier(Role role) {
        return role == Role.COMPTABLE || role == Role.CONTROLEUR_GENERAL || role == Role.ADMIN;
    }
}
