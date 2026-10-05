package com.NTFOODS.Api_tanty.shared.infrastructure.security;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Utilitaire Infrastructure — Accès à l'utilisateur authentifié.
 *
 * Extrait l'agrégat Utilisateur depuis le SecurityContext Spring Security.
 * Le filtre JWT place l'agrégat dans le principal. Partagé par tous les modules.
 */
@Component
public class AuthenticatedUserResolver {

    /**
     * Retourne l'utilisateur authentifié courant.
     *
     * @throws IllegalStateException si aucun utilisateur n'est authentifié
     */
    public Utilisateur getUtilisateurCourant() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Aucun utilisateur authentifié dans le contexte");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof Utilisateur utilisateur)) {
            throw new IllegalStateException(
                "Le principal n'est pas un agrégat Utilisateur : " + principal.getClass()
            );
        }
        return utilisateur;
    }

    public String getMatriculeCourant() {
        return getUtilisateurCourant().getMatricule().getValeur();
    }

    public Role getRoleCourant() {
        return getUtilisateurCourant().getRole();
    }

    public boolean aRole(Role role) {
        return getRoleCourant() == role;
    }
}
