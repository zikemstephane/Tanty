package com.NTFOODS.Api_tanty.modules.production.domain.lot.specification;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;

/** Specifications — règles d'accès aux lots de produits finis. */
public final class LotSpecification {

    private LotSpecification() {}

    public static boolean peutDeclarer(Role role) {
        return role == Role.CHEF_PRODUCTION || role == Role.ADMIN;
    }

    public static boolean peutValiderParStock(Role role) {
        return role == Role.GESTIONNAIRE_STOCK || role == Role.ADMIN;
    }
}
