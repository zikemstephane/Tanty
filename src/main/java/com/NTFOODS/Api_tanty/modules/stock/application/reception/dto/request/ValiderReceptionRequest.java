package com.NTFOODS.Api_tanty.modules.stock.application.reception.dto.request;

import jakarta.validation.constraints.Size;

/**
 * Corps facultatif des deux étapes de validation d'une réception.
 *
 * Ne contient que les observations du validateur. L'écran demandait aussi un
 * « code d'autorisation » : il a été retiré. Il n'était comparé à rien — ni
 * secret partagé, ni second facteur — et le système ne comporte pas de code
 * secret par fonctionnalité (décision du 18/08/2026). Un champ qui ressemble
 * à un contrôle sans en être un est pire que pas de champ du tout.
 */
public record ValiderReceptionRequest(
    @Size(max = 1000, message = "Les observations ne peuvent excéder 1000 caractères")
    String notes
) {
    /** Un corps absent vaut une validation sans observation. */
    public static ValiderReceptionRequest vide() {
        return new ValiderReceptionRequest(null);
    }

    public String notesOuNull() {
        return notes;
    }
}
