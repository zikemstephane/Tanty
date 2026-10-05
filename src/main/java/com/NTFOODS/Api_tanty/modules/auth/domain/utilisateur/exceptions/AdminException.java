package com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.exceptions;

public abstract class AdminException extends RuntimeException {
    protected AdminException(String message) { super(message); }
}
