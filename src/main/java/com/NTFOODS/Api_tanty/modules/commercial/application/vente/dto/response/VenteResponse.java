package com.NTFOODS.Api_tanty.modules.commercial.application.vente.dto.response;

import java.time.LocalDate;
import java.util.List;

public record VenteResponse(
    Long id,
    String numeroVente,
    String matriculeCommercial,
    String codeClient,
    LocalDate date,
    String modeReglement,
    String typeVente,
    String statut,
    String syncStatus,
    String coordonneesGPS,
    String idFeuillRoute,
    double montantTotal,
    String observations,
    String photoProduits,
    List<LigneResponse> lignes
) {
    public record LigneResponse(String codeProduit, String designationProduit, String conditionnement,
                                int quantite, double prixUnitaire, double remise) {}
}
