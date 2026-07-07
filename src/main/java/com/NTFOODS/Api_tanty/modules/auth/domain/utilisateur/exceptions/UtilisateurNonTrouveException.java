package com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.exceptions;

public class UtilisateurNonTrouveException extends AdminException {
    public UtilisateurNonTrouveException(String matricule) {
        super("Utilisateur introuvable: " + matricule);
    }
}
