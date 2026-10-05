package com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.command;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.enums.TypeVersement;

import java.time.LocalDate;

/**
 * @param montantAttendu     Si {@code null}, calculé automatiquement (Réconciliation Financière
 *                            Immédiate — Pilier 2) à partir du CA déclaré sur les ventes du
 *                            commercial pour cette date, déduction faite des retours validés.
 * @param justificationEcart Motif de l'écart, saisi par le commercial au moment de sa
 *                            déclaration de fin de tournée. Transmis dès l'enregistrement
 *                            plutôt qu'en second appel : la justification est indissociable
 *                            de l'écart qu'elle explique, et l'exiger séparément conduisait
 *                            à la perdre. Ignoré si le versement ne présente pas d'alerte.
 */
public record EnregistrerVersementCommand(
        String matriculeCommercial,
        Double montantAttendu,
        double cashVerse,
        TypeVersement typeVersement,
        LocalDate date,
        String justificationEcart
) {}
