package com.NTFOODS.Api_tanty.modules.comptabilite.application.facture.dto.response;

import java.time.LocalDate;
import java.util.List;

public record FactureResponse(
    Long id,
    String numeroFacture,
    String codeClient,
    String typeFacture,
    String referenceFactureOrigine,
    LocalDate dateEmission,
    String statut,
    String matriculeEmetteur,
    double montantTotalHT,
    double montantTotalTVA,
    double montantTotalTTC,
    List<LigneResponse> lignes
) {
    public record LigneResponse(String referenceArticle, String designation, int quantite,
                                double prixUnitaireHT, double tauxTVA, double remise,
                                double montantHT, double montantTVA, double montantTTC) {}
}
