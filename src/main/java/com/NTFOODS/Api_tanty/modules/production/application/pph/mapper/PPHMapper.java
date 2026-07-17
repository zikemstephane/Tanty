package com.NTFOODS.Api_tanty.modules.production.application.pph.mapper;

import com.NTFOODS.Api_tanty.modules.production.application.pph.dto.response.PPHResponse;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.LignePlanDetail;
import org.springframework.stereotype.Component;

@Component
public class PPHMapper {

    public PPHResponse versResponse(PlanProductionHebdomadaire pph) {
        var lignes = pph.getLignes().stream()
            .map(l -> {
                var detail = pph.getLignesDetaillees().stream()
                    .filter(d -> d.getCodeProduit().equals(l.getCodeProduit()))
                    .findFirst();
                return new PPHResponse.LigneResponse(
                    l.getCodeProduit(), l.getNomProduit(),
                    detail.map(LignePlanDetail::getCategorieMetier).orElse(null),
                    l.getObjectifSemaine(), l.getProductionRealisee(),
                    detail.map(LignePlanDetail::getTotalPoidsKg).orElse(0.0),
                    detail.map(LignePlanDetail::getTotalUnites).orElse(0)
                );
            })
            .toList();
        var lignesDetaillees = pph.getLignesDetaillees().stream()
            .map(l -> new PPHResponse.LigneDetailResponse(
                l.getCodeProduit(), l.getNomProduit(), l.getCategorieMetier(),
                l.getQuantiteCartons(), l.getNombreUnitesParConditionnement(), l.getPoidsUnitaireGrammes(),
                l.getRecette() != null ? l.getRecette().name() : null,
                l.getDateJour(), l.getQuantiteJour(),
                l.getPoudreMaisKg(), l.getPoudreSojaKg(), l.getPoudreArachideKg(),
                l.getNombreFuts50kg(), l.getTotalSachets(), l.getTotalCouvercles(), l.getTotalScelles(),
                l.getTotalPoidsGrammes(),
                l.getTotalPoidsKg(),
                l.getTotalUnites()
            ))
            .toList();
        var repartition = pph.getRepartitionJours().stream()
            .map(r -> new PPHResponse.RepartitionResponse(
                r.getDate(), r.getPoudreMaisKg(), r.getPoudreSojaKg(), r.getPoudreArachideKg(),
                r.getTotalFuts(), r.getTotalSachets(), r.getTotalCouvercles(), r.getTotalScelles()
            ))
            .toList();
        return new PPHResponse(pph.getId(), pph.getReferencePPH(), pph.getReferenceBC(), pph.getSemaine(),
            pph.getDateDebut(), pph.getDateFin(), pph.getDateSoumission(),
            pph.getMatriculeChefProduction(), pph.getStatut() != null ? pph.getStatut().name() : null,
            lignes, lignesDetaillees, repartition);
    }
}
