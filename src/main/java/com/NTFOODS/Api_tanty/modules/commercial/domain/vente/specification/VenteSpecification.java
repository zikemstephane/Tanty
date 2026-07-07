package com.NTFOODS.Api_tanty.modules.commercial.domain.vente.specification;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;

/** Specifications — règles d'accès aux ventes terrain. */
public final class VenteSpecification {

    private VenteSpecification() {}

    public static boolean peutVendre(Role role) {
        return role == Role.COMMERCIAL || role == Role.ADMIN;
    }

    public static boolean peutConsulter(Role role) {
        return role != null;
    }
}
