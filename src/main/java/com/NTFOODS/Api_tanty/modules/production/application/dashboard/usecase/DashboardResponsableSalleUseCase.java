package com.NTFOODS.Api_tanty.modules.production.application.dashboard.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.dashboard.dto.response.DashboardResponsableSalleResponse;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.repository.AffectationJournaliereRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.besoin.enums.StatutBesoin;
import com.NTFOODS.Api_tanty.modules.production.domain.besoin.repository.ExpressionBesoinRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.dashboard.service.SyntheseResponsableSalle;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.enums.StatutLot;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.repository.LotRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.of.enums.StatutOF;
import com.NTFOODS.Api_tanty.modules.production.domain.of.repository.OFRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.enums.StatutPPH;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.registre.repository.RegistreProductionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/** Tableau de bord du responsable de salle : plan en cours, affectation, registre, besoins, lots et OF du jour. */
@Service
@Transactional(readOnly = true)
public class DashboardResponsableSalleUseCase {

    private final PPHRepository pphRepository;
    private final AffectationJournaliereRepository affectationRepository;
    private final RegistreProductionRepository registreRepository;
    private final ExpressionBesoinRepository besoinRepository;
    private final LotRepository lotRepository;
    private final OFRepository ofRepository;

    public DashboardResponsableSalleUseCase(PPHRepository pphRepository,
                                            AffectationJournaliereRepository affectationRepository,
                                            RegistreProductionRepository registreRepository,
                                            ExpressionBesoinRepository besoinRepository,
                                            LotRepository lotRepository,
                                            OFRepository ofRepository) {
        this.pphRepository = pphRepository;
        this.affectationRepository = affectationRepository;
        this.registreRepository = registreRepository;
        this.besoinRepository = besoinRepository;
        this.lotRepository = lotRepository;
        this.ofRepository = ofRepository;
    }

    public DashboardResponsableSalleResponse executer(LocalDate date) {
        var pphEnCours = pphRepository.trouverEnCours();

        DashboardResponsableSalleResponse.PlanEnCours plan = null;
        SyntheseResponsableSalle.Affectation affectation = SyntheseResponsableSalle.Affectation.absente();
        boolean registreSaisi = false;
        if (pphEnCours.isPresent()) {
            var pph = pphEnCours.get();
            var av = SyntheseResponsableSalle.avancementPph(pph.getLignes());
            plan = new DashboardResponsableSalleResponse.PlanEnCours(pph.getReferencePPH(), pph.getSemaine(),
                pph.getDateDebut(), pph.getDateFin(), av.nbProduits(), av.objectif(), av.realise(), av.pct());
            registreSaisi = registreRepository.existeParPPHEtDate(pph.getReferencePPH(), date);
            var aff = affectationRepository.trouverParPPHEtDate(pph.getReferencePPH(), date);
            if (aff.isPresent()) {
                affectation = SyntheseResponsableSalle.resumeAffectation(aff.get().getStatut().name(),
                    aff.get().getLignes(), aff.get().getSaisies());
            }
        }

        var besoins = new DashboardResponsableSalleResponse.Besoins(
            besoinRepository.listerParStatut(StatutBesoin.EN_ATTENTE).size(),
            besoinRepository.listerParStatut(StatutBesoin.VALIDE).size(),
            besoinRepository.listerParStatut(StatutBesoin.REJETE).size());

        var lots = new DashboardResponsableSalleResponse.Lots(
            lotRepository.listerParStatut(StatutLot.DECLARE).size()
                + lotRepository.listerParStatut(StatutLot.RECEPTIONNE_STOCK).size(),
            lotRepository.listerParStatut(StatutLot.VALIDATED_BY_STOCK).size(),
            lotRepository.listerParStatut(StatutLot.REJETE).size());

        var ofs = new DashboardResponsableSalleResponse.Ofs(
            ofRepository.listerParStatut(StatutOF.EN_COURS).size(),
            ofRepository.listerParStatut(StatutOF.PLANIFIE).size(),
            ofRepository.listerParStatut(StatutOF.HONORE).size());

        return new DashboardResponsableSalleResponse(date, plan,
            pphRepository.listerParStatut(StatutPPH.VALIDE).size(),
            new DashboardResponsableSalleResponse.AffectationDuJour(affectation.existe(), affectation.statut(),
                affectation.postes(), affectation.employesAffectes(), affectation.saisies(),
                affectation.objectifsAtteints(), affectation.ecarts(), affectation.quantiteRealisee()),
            registreSaisi, besoins, lots, ofs);
    }
}
