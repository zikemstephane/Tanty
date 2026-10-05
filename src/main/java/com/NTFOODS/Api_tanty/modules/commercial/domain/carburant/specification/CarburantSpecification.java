package com.NTFOODS.Api_tanty.modules.commercial.domain.carburant.specification;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;

/** Specifications — règles de la demande de carburant (double signature). */
public final class CarburantSpecification {

    private CarburantSpecification() {}

    public static boolean peutDemander(Role role) {
        return role == Role.COMMERCIAL || role == Role.ADMIN;
    }

    public static boolean peutSigner(Role role) {
        return role == Role.DIRECTEUR_GENERAL || role == Role.GESTIONNAIRE_STOCK || role == Role.ADMIN;
    }
}
