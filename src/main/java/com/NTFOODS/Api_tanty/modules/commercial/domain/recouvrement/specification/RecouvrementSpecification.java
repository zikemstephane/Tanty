package com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.specification;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;

/** Specifications — règles d'accès aux recouvrements. */
public final class RecouvrementSpecification {

    private RecouvrementSpecification() {}

    public static boolean peutCreer(Role role) {
        return role == Role.COMMERCIAL || role == Role.SECRETAIRE || role == Role.ADMIN;
    }

    public static boolean peutEnregistrerRemboursement(Role role) {
        return role == Role.SECRETAIRE || role == Role.COMPTABLE || role == Role.ADMIN;
    }

    public static boolean peutPasserEnContentieux(Role role) {
        return role == Role.COMPTABLE || role == Role.DIRECTEUR_GENERAL || role == Role.ADMIN;
    }
}
