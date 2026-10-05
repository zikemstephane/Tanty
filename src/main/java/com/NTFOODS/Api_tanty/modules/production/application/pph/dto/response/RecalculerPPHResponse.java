package com.NTFOODS.Api_tanty.modules.production.application.pph.dto.response;

/**
 * Résultat d'un recalcul de PPH. {@code ajustementsManuelsEcrases} est {@code
 * true} quand le PPH portait une répartition journalière ajustée manuellement
 * (Responsable de Salle) avant ce recalcul — elle est remplacée par la
 * répartition fraîchement calculée, jamais fusionnée.
 */
public record RecalculerPPHResponse(
    PPHResponse pph,
    boolean ajustementsManuelsEcrases
) {}
