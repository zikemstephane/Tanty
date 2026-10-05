package com.NTFOODS.Api_tanty.modules.production.application.formule.dto.response;

import java.math.BigDecimal;
import java.util.List;

/** Un produit du catalogue avec sa formule de production (si elle existe) et la composition d'emballage de chaque conditionnement. */
public record ReferentielProductionResponse(
    String codeProduit,
    String designation,
    String categorieMetier,
    boolean actif,
    FormuleResponse formule,
    List<ConditionnementResponse> conditionnements
) {
    public record FormuleResponse(BigDecimal baseKg, List<LigneResponse> lignes) {}

    public record LigneResponse(String matierePremiere, BigDecimal proportionKg) {}

    public record ConditionnementResponse(
        String id,
        String sku,
        String libelle,
        String niveau,
        double poidsUnitaireGrammes,
        String type,
        List<ComposantResponse> composants
    ) {}

    public record ComposantResponse(String type, int quantiteComposant, int quantiteUnitesProduites) {}
}
