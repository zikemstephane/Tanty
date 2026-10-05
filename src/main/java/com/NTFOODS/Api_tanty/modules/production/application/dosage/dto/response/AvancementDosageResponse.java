package com.NTFOODS.Api_tanty.modules.production.application.dosage.dto.response;

import com.NTFOODS.Api_tanty.modules.production.domain.dosage.service.AvancementDosage;

import java.time.LocalDate;
import java.util.List;

/** Fûts dosés d'un plan PPH : totaux validés / en cours et détail par jour. */
public record AvancementDosageResponse(
    String referencePPH,
    int nbSessions,
    int nbSessionsValidees,
    int futsValides,
    int futsEnCours,
    int futsDoses,
    List<Jour> parJour
) {
    public record Jour(LocalDate date, int futsValides, int futsEnCours) {}

    public static AvancementDosageResponse de(AvancementDosage.Resultat r) {
        return new AvancementDosageResponse(
            r.referencePPH(), r.nbSessions(), r.nbSessionsValidees(),
            r.futsValides(), r.futsEnCours(), r.futsDoses(),
            r.parJour().stream().map(j -> new Jour(j.date(), j.futsValides(), j.futsEnCours())).toList());
    }
}
