package com.NTFOODS.Api_tanty.modules.commercial.application.precommande.dto.response;

import java.time.LocalDate;
import java.util.List;

public record PreCommandeResponse(
    Long id,
    String numeroPreCommande,
    String matriculeCommercial,
    LocalDate dateSoumission,
    LocalDate dateEffet,
    String statut,
    String matriculeValidateurSecretaire,
    String matriculeValidateurComptable,
    String motifRefus,
    boolean soumiseEnSoiree,
    List<LigneResponse> lignes
) {
    public record LigneResponse(String codeProduit, String designationProduit, String conditionnement,
                                int quantiteDemandee, int quantiteValidee) {}
}
