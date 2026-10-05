package com.NTFOODS.Api_tanty.modules.commercial.application.precommande.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PreCommandeResponse(
    Long id,
    String numeroPreCommande,
    String matriculeCommercial,
    String nomCommercial,
    LocalDate dateSoumission,
    LocalDate dateEffet,
    String statut,
    String matriculeValidateurSecretaire,
    String matriculeValidateurComptable,
    String motifRefus,
    boolean soumiseEnSoiree,
    String matriculeArbitre,
    LocalDateTime dateArbitrage,
    String motifArbitrage,
    List<LigneResponse> lignes
) {
    /**
     * {@code quantiteDemandee} est celle saisie par le commercial et n'est jamais
     * modifiée ; {@code quantiteValidee} est celle retenue après arbitrage du
     * gestionnaire de stock, et sert de base à la livraison.
     */
    public record LigneResponse(String codeProduit, String designationProduit, String conditionnement,
                                int quantiteDemandee, int quantiteValidee) {}
}
