package com.NTFOODS.Api_tanty.shared.kernel.exception;

/**
 * Exception de base du domaine.
 *
 * Toutes les exceptions métier (violations d'invariants, règles non respectées)
 * en héritent. Mappée en HTTP 409/400 par le GlobalExceptionHandler.
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }

    protected DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
