package com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.specification;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;

/** Specifications — règles d'accès aux primes commerciales. */
public final class PrimeSpecification {

    private PrimeSpecification() {}

    public static boolean peutCalculer(Role role) {
        return role == Role.COMPTABLE || role == Role.DIRECTEUR_GENERAL || role == Role.ADMIN;
    }

    public static boolean peutValider(Role role) {
        return role == Role.COMPTABLE || role == Role.DIRECTEUR_GENERAL || role == Role.ADMIN;
    }

    public static boolean peutVerser(Role role) {
        return role == Role.COMPTABLE || role == Role.ADMIN;
    }
}
