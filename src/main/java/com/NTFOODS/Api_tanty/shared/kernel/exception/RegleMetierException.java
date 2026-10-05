package com.NTFOODS.Api_tanty.shared.kernel.exception;

/**
 * Levée lorsqu'une règle métier (invariant) est violée. Mappée en HTTP 409.
 */
public class RegleMetierException extends DomainException {

    public RegleMetierException(String message) {
        super(message);
    }
}
