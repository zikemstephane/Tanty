package com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.exceptions;

public class EmailDejaUtiliseException extends AdminException {
    public EmailDejaUtiliseException(String email) {
        super("L'email " + email + " est déjà utilisé");
    }
}
