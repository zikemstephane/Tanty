package com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.specification;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;

/** Specifications — règles d'accès au journal de caisse. */
public final class CaisseSpecification {

    private CaisseSpecification() {}

    public static boolean peutOperer(Role role) {
        return role == Role.SECRETAIRE || role == Role.COMPTABLE || role == Role.ADMIN;
    }

    public static boolean peutConsulter(Role role) {
        return role != null;
    }
}
