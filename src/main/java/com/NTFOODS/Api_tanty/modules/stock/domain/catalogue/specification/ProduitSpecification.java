package com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.specification;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;

/**
 * Specifications — règles d'accès à la gestion du catalogue produit.
 *
 * Le catalogue est géré par le Gestionnaire de Stock, sous supervision du DG.
 * La consultation est ouverte à tous les rôles authentifiés.
 */
public final class ProduitSpecification {

    private ProduitSpecification() {}

    public static boolean peutGerer(Role role) {
        return role == Role.GESTIONNAIRE_STOCK
            || role == Role.DIRECTEUR_GENERAL
            || role == Role.ADMIN;
    }

    public static boolean peutConsulter(Role role) {
        return role != null;
    }
}
