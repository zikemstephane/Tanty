package com.NTFOODS.Api_tanty.modules.production.application.affectation.mapper;

import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response.AffectationJournaliereResponse;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response.DetailsRealisationPosteResponse;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.service.PosteProductionService;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.aggregate.AffectationJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.DetailsRealisationPoste;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.SaisieRealisationPoste;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class AffectationMapper {

    private final PosteProductionService posteService;

    public AffectationMapper(PosteProductionService posteService) {
        this.posteService = posteService;
    }

    public AffectationJournaliereResponse versResponse(AffectationJournaliere a) {
        var lignes = a.getLignes().stream()
            .map(l -> {
                var poste = posteService.parCode(l.getPoste());
                return new AffectationJournaliereResponse.LigneAffectationResponse(
                    poste.getCode(), poste.getLibelle(),
                    poste.getEffectifReference(), l.getEffectifReel(),
                    l.getDuree().name(), l.getObjectifAjuste(),
                    poste.getUnite(), l.getMatriculesEmployes());
            })
            .toList();

        var saisies = a.getSaisies().stream()
            .map(s -> mapSaisie(a, s))
            .toList();

        var classement = buildClassement(a.getClassementPostes());

        return new AffectationJournaliereResponse(
            a.getId(), a.getReferencePPH(), a.getDate(), a.getMatriculeAgent(),
            a.getStatut(), lignes, saisies, classement,
            a.getDateCreation(), a.getDateModification());
    }

    private AffectationJournaliereResponse.SaisieRealisationResponse mapSaisie(AffectationJournaliere a,
                                                                              SaisieRealisationPoste s) {
        var poste = posteService.parCode(s.getPoste());
        double objectifIndividuel = objectifIndividuel(a, s.getPoste());
        var prods = s.getProductionsIndividuelles().stream()
            .map(p -> {
                double pct = objectifIndividuel > 0 ? p.getQuantiteRealisee() / objectifIndividuel * 100 : 0;
                return new AffectationJournaliereResponse.ProductionIndividuelleResponse(
                    p.getMatriculeEmploye(), p.getNomEmploye(),
                    p.getQuantiteRealisee(), poste.getUnite(),
                    objectifIndividuel, pct);
            })
            .toList();
        var details = mapDetails(s.getDetails());
        return new AffectationJournaliereResponse.SaisieRealisationResponse(
            poste.getCode(), poste.getLibelle(),
            s.getQuantiteRealisee(), s.getObjectifAjuste(),
            s.getPctRealisation(), poste.getUnite(),
            prods, details, s.getWarnings(), s.getDateEnregistrement());
    }

    /** Objectif ajusté du poste réparti entre les employés affectés à ce poste (0 si aucun employé). */
    private double objectifIndividuel(AffectationJournaliere a, String codePoste) {
        return a.getLignes().stream()
            .filter(l -> l.getPoste().equals(codePoste))
            .filter(l -> !l.getMatriculesEmployes().isEmpty())
            .findFirst()
            .map(l -> l.getObjectifAjuste() / l.getMatriculesEmployes().size())
            .orElse(0.0);
    }

    private DetailsRealisationPosteResponse mapDetails(DetailsRealisationPoste d) {
        if (d == null) return null;
        return new DetailsRealisationPosteResponse(
            d.getNbRegimesEpluches(), d.getNbRegimesDecoupes(),
            d.getNbSceauxEtiquetes(), d.getNbEtiquettesDatees(),
            d.getNbSachetsDates(), d.getNbCartonsProduits(),
            d.getKgChipsProduits());
    }

    private List<AffectationJournaliereResponse.ClassementPosteResponse> buildClassement(
            List<SaisieRealisationPoste> postes) {
        AtomicInteger rang = new AtomicInteger(1);
        return postes.stream()
            .map(s -> {
                var poste = posteService.parCode(s.getPoste());
                return new AffectationJournaliereResponse.ClassementPosteResponse(
                    rang.getAndIncrement(), poste.getCode(), poste.getLibelle(),
                    s.getQuantiteRealisee(), s.getObjectifAjuste(),
                    s.getPctRealisation(), poste.getUnite());
            })
            .toList();
    }
}
