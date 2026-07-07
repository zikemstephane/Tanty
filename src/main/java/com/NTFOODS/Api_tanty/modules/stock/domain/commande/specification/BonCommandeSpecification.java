package com.NTFOODS.Api_tanty.modules.stock.domain.commande.specification;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;

/** Règles d'accès au Bon de Commande fournisseur. */
public final class BonCommandeSpecification {

    private BonCommandeSpecification() {}

    public static boolean peutGerer(Role role) {
        return role == Role.GESTIONNAIRE_STOCK || role == Role.CHEF_PRODUCTION
            || role == Role.DIRECTEUR_GENERAL || role == Role.ADMIN;
    }

    public static boolean peutCreer(Role role) {
        return role == Role.GESTIONNAIRE_STOCK || role == Role.CHEF_PRODUCTION || role == Role.ADMIN;
    }

    public static boolean peutValider(Role role) {
        return role == Role.CHEF_PRODUCTION || role == Role.ADMIN;
    }

    public static boolean peutModifierApresValidation(Role role) {
        return role == Role.CHEF_PRODUCTION || role == Role.RESPONSABLE_SALLE
            || role == Role.MACHINISTE || role == Role.ADMIN;
    }

    public static boolean peutSupprimerApresValidation(Role role) {
        return role == Role.CHEF_PRODUCTION || role == Role.ADMIN;
    }

    public static boolean peutConsulter(Role role) {
        return role != null;
    }
}
