package com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.response;

import com.NTFOODS.Api_tanty.modules.production.domain.broyage.service.AvancementBroyage;

import java.util.List;

/** Poudre prévue et écrasée d'un plan PPH, par matière. */
public record AvancementBroyageResponse(
    String referencePPH,
    int nbSessions,
    int nbSessionsCloturees,
    List<Matiere> matieres
) {
    public record Matiere(String code, boolean suivie, double prevuKg, double ecraseKg, double enCoursKg,
                          double restantKg, double ecartKg, double pourcent) {}

    public static AvancementBroyageResponse de(AvancementBroyage.Resultat r) {
        return new AvancementBroyageResponse(r.referencePPH(), r.nbSessions(), r.nbSessionsCloturees(),
            r.matieres().stream()
                .map(m -> new Matiere(m.code(), m.suivie(), m.prevuKg(), m.ecraseKg(), m.enCoursKg(),
                    m.restantKg(), m.ecartKg(), m.pourcent()))
                .toList());
    }
}
