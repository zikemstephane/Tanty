package com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.exceptions;

public class CompteDesactiveException extends AdminException {
    public CompteDesactiveException(String matricule) {
        super("Le compte " + matricule + " est désactivé. Contacter l'administrateur.");
    }
}
