package com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.exceptions;

public class CredentialsInvalidesException extends AdminException {
    public CredentialsInvalidesException() {
        super("Matricule ou mot de passe incorrect");
    }
}
