package com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.command;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.enums.TypeVersement;

import java.time.LocalDate;

/**
 * @param montantAttendu Si {@code null}, calculé automatiquement (Réconciliation Financière
 *                        Immédiate — Pilier 2) à partir du CA déclaré sur les ventes du
 *                        commercial pour cette date, déduction faite des retours validés.
 */
public record EnregistrerVersementCommand(
        String matriculeCommercial,
        Double montantAttendu,
        double cashVerse,
        TypeVersement typeVersement,
        LocalDate date
) {}
