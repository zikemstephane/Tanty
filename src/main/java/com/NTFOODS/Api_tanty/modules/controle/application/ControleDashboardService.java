package com.NTFOODS.Api_tanty.modules.controle.application;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.repository.DecaissementRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.repository.VersementRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.enums.StatutRecouvrement;
import com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.repository.RecouvrementRepository;
import com.NTFOODS.Api_tanty.modules.controle.application.dto.ControleDashboardResponse;
import com.NTFOODS.Api_tanty.modules.controle.application.dto.ControleDashboardResponse.BudgetResponse;
import com.NTFOODS.Api_tanty.modules.controle.application.dto.ControleDashboardResponse.MargeProduitResponse;
import com.NTFOODS.Api_tanty.modules.controle.application.dto.ControleDashboardResponse.VarianceResponse;
import com.NTFOODS.Api_tanty.modules.controle.infrastructure.persistence.jpa.BudgetJpaRepository;
import com.NTFOODS.Api_tanty.modules.controle.infrastructure.persistence.jpa.MargeProduitJpaRepository;
import com.NTFOODS.Api_tanty.modules.controle.infrastructure.persistence.jpa.VarianceJpaRepository;
import com.NTFOODS.Api_tanty.modules.dg.application.rapport.AnomaliesService;
import com.NTFOODS.Api_tanty.modules.dg.application.rapport.AnomalieResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ControleDashboardService {

    private final DecaissementRepository decaissementRepository;
    private final VersementRepository versementRepository;
    private final RecouvrementRepository recouvrementRepository;
    private final BudgetJpaRepository budgetJpaRepository;
    private final VarianceJpaRepository varianceJpaRepository;
    private final MargeProduitJpaRepository margeProduitJpaRepository;
    private final AnomaliesService anomaliesService;

    public ControleDashboardService(DecaissementRepository decaissementRepository,
                                     VersementRepository versementRepository,
                                     RecouvrementRepository recouvrementRepository,
                                     BudgetJpaRepository budgetJpaRepository,
                                     VarianceJpaRepository varianceJpaRepository,
                                     MargeProduitJpaRepository margeProduitJpaRepository,
                                     AnomaliesService anomaliesService) {
        this.decaissementRepository = decaissementRepository;
        this.versementRepository = versementRepository;
        this.recouvrementRepository = recouvrementRepository;
        this.budgetJpaRepository = budgetJpaRepository;
        this.varianceJpaRepository = varianceJpaRepository;
        this.margeProduitJpaRepository = margeProduitJpaRepository;
        this.anomaliesService = anomaliesService;
    }

    public ControleDashboardResponse dashboard() {
        var versements = versementRepository.listerParDate(LocalDate.now());
        double tresorerie = versements.stream().mapToDouble(v -> v.getCashVerse()).sum();
        int ecartsRouges = (int) versements.stream().filter(v -> v.isAlerteRouge()).count();

        var recouvrements = new java.util.ArrayList<>(
            recouvrementRepository.listerParStatut(StatutRecouvrement.EN_COURS));
        recouvrements.addAll(recouvrementRepository.listerParStatut(StatutRecouvrement.REMBOURSE_PARTIEL));
        double ventesCredit = recouvrements.stream()
            .mapToDouble(r -> r.montantRestant()).sum();

        var marges = margeProduitJpaRepository.findAll().stream()
            .map(m -> new MargeProduitResponse(
                m.getCode(), m.getDesignation(), m.getPrixVente(), m.getCoutRevient(),
                m.getPrixVente() - m.getCoutRevient(),
                m.getPrixVente() > 0 ? (int) Math.round(((m.getPrixVente() - m.getCoutRevient()) / m.getPrixVente()) * 100) : 0,
                (m.getPrixVente() - m.getCoutRevient()) * m.getVentesMois(),
                m.getVentesMois()))
            .sorted((a, b) -> Double.compare(b.margeTotale(), a.margeTotale()))
            .collect(Collectors.toList());
        int margeMoyenne = marges.isEmpty() ? 0 : (int) Math.round(
            marges.stream().mapToInt(MargeProduitResponse::margePct).average().orElse(0));

        var budgetEntities = budgetJpaRepository.findAll();
        var budget = budgetEntities.stream()
            .map(b -> new BudgetResponse(b.getPoste(), b.getBudget(), b.getEngage(), b.getRealise()))
            .collect(Collectors.toList());
        double budgetEngage = budget.stream().mapToDouble(BudgetResponse::engage).sum();
        double budgetTotal = budget.stream().mapToDouble(BudgetResponse::budget).sum();
        int budgetDepasse = (int) budget.stream().filter(b -> b.engage() > b.budget()).count();

        var variances = varianceJpaRepository.findAll().stream()
            .map(v -> new VarianceResponse(v.getPoste(), v.getStandard(), v.getReel(), v.getUnite()))
            .collect(Collectors.toList());

        var decaissements = decaissementRepository.listerTous();
        int decaissementsAttente = (int) decaissements.stream()
            .filter(d -> d.getStatut().name().equals("PROPOSE") || d.getStatut().name().equals("VALIDEE_CG"))
            .count();

        List<AnomalieResponse> anomalies = anomaliesService.detecter();
        int anomaliesElevees = anomalies.size();

        return new ControleDashboardResponse(
            tresorerie, ecartsRouges, ventesCredit, margeMoyenne, 0,
            budgetEngage, budgetTotal, budgetDepasse, decaissementsAttente, anomaliesElevees,
            marges, budget, variances
        );
    }

    public List<MargeProduitResponse> marges() {
        return margeProduitJpaRepository.findAll().stream()
            .map(m -> new MargeProduitResponse(
                m.getCode(), m.getDesignation(), m.getPrixVente(), m.getCoutRevient(),
                m.getPrixVente() - m.getCoutRevient(),
                m.getPrixVente() > 0 ? (int) Math.round(((m.getPrixVente() - m.getCoutRevient()) / m.getPrixVente()) * 100) : 0,
                (m.getPrixVente() - m.getCoutRevient()) * m.getVentesMois(),
                m.getVentesMois()))
            .sorted((a, b) -> Double.compare(b.margeTotale(), a.margeTotale()))
            .collect(Collectors.toList());
    }

    public List<BudgetResponse> budget() {
        return budgetJpaRepository.findAll().stream()
            .map(b -> new BudgetResponse(b.getPoste(), b.getBudget(), b.getEngage(), b.getRealise()))
            .collect(Collectors.toList());
    }

    public List<VarianceResponse> variances() {
        return varianceJpaRepository.findAll().stream()
            .map(v -> new VarianceResponse(v.getPoste(), v.getStandard(), v.getReel(), v.getUnite()))
            .collect(Collectors.toList());
    }
}
