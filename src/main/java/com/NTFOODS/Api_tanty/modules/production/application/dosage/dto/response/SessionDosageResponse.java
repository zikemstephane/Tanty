package com.NTFOODS.Api_tanty.modules.production.application.dosage.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record SessionDosageResponse(
    Long id,
    String referencePPH,
    LocalDate date,
    String matriculeAgentDoseur,
    List<MatiereResponse> matieresUtilisees,
    List<String> machinesMobilisees,
    int nbFutsProduits,
    int nbFutsNets,
    int nbFutsCasses,
    String justificationEcart,
    boolean validee,
    boolean cloturee,
    boolean enPause,
    LocalDateTime dateCreation,
    LocalDateTime dateModification
) {
    public record MatiereResponse(String typeMatiere, double quantiteKg) {}
}
