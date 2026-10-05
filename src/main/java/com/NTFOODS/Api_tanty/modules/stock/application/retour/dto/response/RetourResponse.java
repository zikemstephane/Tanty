package com.NTFOODS.Api_tanty.modules.stock.application.retour.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Constat d'invendus tel qu'exposé aux clients.
 *
 * <p>Déclaré et compté voyagent ensemble et séparément : aucun consommateur ne
 * doit avoir à deviner laquelle des deux valeurs il regarde.</p>
 */
public record RetourResponse(
    Long id,
    String numero,
    String matriculeCommercial,
    String matriculeGestionnaire,
    LocalDate dateRetour,
    String statut,
    LocalDateTime dateComptage,
    Long ecartTotal,
    List<LigneResponse> lignes
) {
    public record LigneResponse(String id, String codeProduit, String sku,
                                long quantiteDeclaree, Long quantiteComptee, Long ecart,
                                String motif) {}
}
