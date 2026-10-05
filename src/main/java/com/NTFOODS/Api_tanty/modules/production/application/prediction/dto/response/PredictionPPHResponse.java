package com.NTFOODS.Api_tanty.modules.production.application.prediction.dto.response;

import com.NTFOODS.Api_tanty.modules.production.domain.prediction.aggregate.PredictionPPH;
import com.NTFOODS.Api_tanty.modules.production.domain.prediction.service.RepartitionPoudre;

import java.time.LocalDateTime;
import java.util.List;

/** Prédiction de production d'un plan PPH : produits attendus pour un nombre de fûts donné. */
public record PredictionPPHResponse(
    String referencePPH,
    int nbFuts,
    double poudreKg,
    LocalDateTime dateCalcul,
    String matricule,
    List<LignePrediction> predictions,
    List<Exclusion> exclusions
) {
    public record LignePrediction(String codeProduit, String designation, String composition,
                                  double poudreAlloueeKg, int quantiteUnitesBase, int quantiteCartons,
                                  double poidsUnitaireGrammes, int unitesParCarton) {}

    public record Exclusion(String codeProduit, String motif) {}

    public static PredictionPPHResponse de(PredictionPPH p) {
        return new PredictionPPHResponse(
            p.referencePPH(), p.nbFuts(), p.poudreKg(), p.dateCalcul(), p.matricule(),
            p.lignes().stream().map(PredictionPPHResponse::versLigne).toList(),
            p.exclusions().stream().map(x -> new Exclusion(x.codeProduit(), x.motif())).toList());
    }

    private static LignePrediction versLigne(RepartitionPoudre.Ligne l) {
        return new LignePrediction(l.codeProduit(), l.designation(), l.composition(), l.poudreAlloueeKg(),
            l.quantiteUnitesBase(), l.quantiteCartons(), l.poidsUnitaireGrammes(), l.unitesParCarton());
    }
}
