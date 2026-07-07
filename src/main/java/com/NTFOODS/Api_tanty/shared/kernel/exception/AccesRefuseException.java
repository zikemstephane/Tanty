package com.NTFOODS.Api_tanty.shared.kernel.exception;

/**
 * Levée lorsqu'un acteur n'a pas le droit d'effectuer une action. Mappée en HTTP 403.
 */
public class AccesRefuseException extends DomainException {

    public AccesRefuseException(String message) {
        super(message);
    }
}
