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
                l.getDateJour(), l.getQuantiteJour(),
                l.getBesoinsMatieresPremiere().stream()
                    .map(b -> new PPHResponse.BesoinMatierePremiereResponse(b.matierePremiere(), b.quantiteKg().doubleValue()))
                    .toList(),
                l.isFormuleManquante(),
                l.getBesoinsEmballage().stream()
                    .map(b -> new PPHResponse.BesoinEmballageResponse(b.type().name(), b.quantite()))
                    .toList(),
                l.isCompositionEmballageManquante(),
                l.getTotalMatieresPremieresKg().doubleValue(),
                l.getNombreFuts50kg(),
                l.getTotalPoidsGrammes(),
                l.getTotalPoidsKg(),
                l.getTotalUnites()
            ))
            .toList();
        var repartition = pph.getRepartitionJours().stream()
            .map(r -> new PPHResponse.RepartitionResponse(
                r.getDate(),
                r.getBesoinsMatieresPremiere().stream()
                    .map(b -> new PPHResponse.BesoinMatierePremiereResponse(b.matierePremiere(), b.quantiteKg().doubleValue()))
                    .toList(),
                r.getBesoinsEmballage().stream()
                    .map(b -> new PPHResponse.BesoinEmballageResponse(b.type().name(), b.quantite()))
                    .toList(),
                r.getTotalMatieresPremieresKg().doubleValue(),
                r.getTotalFuts()
            ))
            .toList();
        return new PPHResponse(pph.getId(), pph.getReferencePPH(), pph.getReferenceBC(), pph.getSemaine(),
            pph.getDateDebut(), pph.getDateFin(), pph.getDateSoumission(),
            pph.getMatriculeChefProduction(), pph.getStatut() != null ? pph.getStatut().name() : null,
            lignes, lignesDetaillees, repartition);
    }
}
