package com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.specification;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;

/** Specifications — règles d'accès aux factures. */
public final class FactureSpecification {

    private FactureSpecification() {}

    public static boolean peutEmettre(Role role) {
        return role == Role.SECRETAIRE || role == Role.COMPTABLE || role == Role.ADMIN;
    }

    public static boolean peutAnnuler(Role role) {
        return role == Role.COMPTABLE || role == Role.DIRECTEUR_GENERAL || role == Role.ADMIN;
    }
}
