package com.NTFOODS.Api_tanty.shared.application.audit.dto;

/**
 * Activité jugée suspecte : répétition d'annulations / modifications par le
 * même matricule sur la même ressource, au-delà du seuil configuré, dans la
 * fenêtre glissante observée.
 */
public record ActiviteSuspecteResponse(
    String matricule,
    String ressource,
    long occurrences
) {}
