package com.NTFOODS.Api_tanty.modules.production.domain.fiche.specification;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;

/** Specifications — règles d'accès aux fiches produit journalières. */
public final class FicheSpecification {

    private FicheSpecification() {}

    public static boolean peutEnregistrer(Role role) {
        return role == Role.RESPONSABLE_SALLE || role == Role.CHEF_PRODUCTION || role == Role.ADMIN;
    }

    public static boolean peutConsulter(Role role) {
        return role != null;
    }
}
