package com.NTFOODS.Api_tanty.shared.kernel.exception;

/**
 * Levée lorsqu'une entité référencée est introuvable. Mappée en HTTP 404.
 */
public class EntiteNonTrouveeException extends DomainException {

    public EntiteNonTrouveeException(String message) {
        super(message);
    }

    public static EntiteNonTrouveeException de(String typeEntite, String identifiant) {
        return new EntiteNonTrouveeException(typeEntite + " introuvable : " + identifiant);
    }
}
