package com.NTFOODS.Api_tanty.modules.commercial.application.commandedistance.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record CommandeDistanceResponse(
    Long id,
    String numeroCommande,
    String codeClient,
    String matriculeCommercial,
    String canal,
    LocalDate dateCommande,
    String statut,
    String matriculeValidateurSecretaire,
    String matriculeValidateurComptable,
    String motifAnnulation,
    String observations,
    double montantTotal,
    LocalDateTime dateCreation,
    List<LigneResponse> lignes
) {
    public record LigneResponse(String codeProduit, String designation, String conditionnement,
                                int quantite, double prixUnitaire) {}
}
