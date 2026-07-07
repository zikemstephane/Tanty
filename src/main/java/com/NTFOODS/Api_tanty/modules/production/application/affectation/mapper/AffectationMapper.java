package com.NTFOODS.Api_tanty.modules.production.application.affectation.mapper;

import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response.AffectationJournaliereResponse;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response.DetailsRealisationPosteResponse;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.aggregate.AffectationJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.DetailsRealisationPoste;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.SaisieRealisationPoste;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class AffectationMapper {

    public AffectationJournaliereResponse versResponse(AffectationJournaliere a) {
        var lignes = a.getLignes().stream()
            .map(l -> new AffectationJournaliereResponse.LigneAffectationResponse(
                l.getPoste().getCode(), l.getPoste().getLibelle(),
                l.getPoste().getEffectifReference(), l.getEffectifReel(),
                l.getDuree().getLibelle(), l.getObjectifAjuste(),
                l.getPoste().getUnite().getLibelle(), l.getMatriculesEmployes()))
            .toList();

        var saisies = a.getSaisies().stream()
            .map(s -> mapSaisie(s))
            .toList();

        var classement = buildClassement(a.getClassementPostes());

        return new AffectationJournaliereResponse(
            a.getId(), a.getReferencePPH(), a.getDate(), a.getMatriculeAgent(),
            a.getStatut(), lignes, saisies, classement,
            a.getDateCreation(), a.getDateModification());
    }

    private AffectationJournaliereResponse.SaisieRealisationResponse mapSaisie(SaisieRealisationPoste s) {
        var prods = s.getProductionsIndividuelles().stream()
            .map(p -> new AffectationJournaliereResponse.ProductionIndividuelleResponse(
                p.getMatriculeEmploye(), p.getNomEmploye(),
                p.getQuantiteRealisee(), s.getUnite().getLibelle()))
            .toList();
        var details = mapDetails(s.getDetails());
        return new AffectationJournaliereResponse.SaisieRealisationResponse(
            s.getPoste().getCode(), s.getPoste().getLibelle(),
            s.getQuantiteRealisee(), s.getObjectifAjuste(),
            s.getPctRealisation(), s.getUnite().getLibelle(),
            prods, details, s.getWarnings(), s.getDateEnregistrement());
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
            .map(s -> new AffectationJournaliereResponse.ClassementPosteResponse(
                rang.getAndIncrement(), s.getPoste().getCode(), s.getPoste().getLibelle(),
                s.getQuantiteRealisee(), s.getObjectifAjuste(),
                s.getPctRealisation(), s.getUnite().getLibelle()))
            .toList();
    }
}
