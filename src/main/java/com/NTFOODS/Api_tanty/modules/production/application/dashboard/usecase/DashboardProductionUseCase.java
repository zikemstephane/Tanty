package com.NTFOODS.Api_tanty.modules.production.application.dashboard.usecase;

import com.NTFOODS.Api_tanty.modules.production.domain.fiche.repository.FicheProduitRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.aggregate.LotProduitFini;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.enums.StatutLot;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.repository.LotRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.of.aggregate.OrdreFabrication;
import com.NTFOODS.Api_tanty.modules.production.domain.of.enums.StatutOF;
import com.NTFOODS.Api_tanty.modules.production.domain.of.repository.OFRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DashboardProductionUseCase {

    private final LotRepository lotRepository;
    private final OFRepository ofRepository;
    private final PPHRepository pphRepository;
    private final FicheProduitRepository ficheRepository;

    public DashboardProductionUseCase(LotRepository lotRepository, OFRepository ofRepository,
                                       PPHRepository pphRepository, FicheProduitRepository ficheRepository) {
        this.lotRepository = lotRepository;
        this.ofRepository = ofRepository;
        this.pphRepository = pphRepository;
        this.ficheRepository = ficheRepository;
    }

    public DashboardChefProduction executerChef() {
        LocalDate today = LocalDate.now();
        LocalDate debutSemaine = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate finSemaine = debutSemaine.plusDays(6);

        List<LotProduitFini> lotsSemaine = lotRepository.listerParPeriode(debutSemaine, finSemaine);
        List<OrdreFabrication> ofsEnCours = ofRepository.listerParStatut(StatutOF.EN_COURS);
        List<OrdreFabrication> ofsPlanifies = ofRepository.listerParStatut(StatutOF.PLANIFIE);
        List<OrdreFabrication> ofsHonores = ofRepository.listerParStatut(StatutOF.HONORE);
        long lotsEnAttente = lotRepository.compterParStatutEtDate(StatutLot.DECLARE, today);

        double quantiteKgSemaine = lotsSemaine.stream()
            .filter(l -> l.getStatut() == StatutLot.VALIDATED_BY_STOCK)
            .mapToDouble(LotProduitFini::getQuantiteKg).sum();
        int cartonsSemaine = lotsSemaine.stream()
            .filter(l -> l.getStatut() == StatutLot.VALIDATED_BY_STOCK)
            .mapToInt(LotProduitFini::getNbCartons).sum();

        int tauxAvancementGlobal = 0;
        PlanProductionHebdomadaire pphEnCours = pphRepository.trouverEnCours().orElse(null);
        if (pphEnCours != null) {
            tauxAvancementGlobal = pphEnCours.tauxAvancementGlobal();
        }

        long totalFiches = ficheRepository.listerParPeriode(debutSemaine, finSemaine).size();

        return new DashboardChefProduction(
            quantiteKgSemaine, cartonsSemaine, lotsEnAttente,
            ofsEnCours.size(), ofsPlanifies.size(), ofsHonores.size(),
            tauxAvancementGlobal, totalFiches,
            pphEnCours != null ? pphEnCours.getReferencePPH() : null,
            pphEnCours != null ? pphEnCours.getSemaine() : null,
            debutSemaine, finSemaine
        );
    }

    public DashboardAgentProduction executerAgent(String matriculeAgent) {
        LocalDate today = LocalDate.now();
        LocalDate debutSemaine = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate finSemaine = debutSemaine.plusDays(6);

        List<OrdreFabrication> ofsEnCours = ofRepository.listerParStatut(StatutOF.EN_COURS);
        List<OrdreFabrication> ofsPlanifies = ofRepository.listerParStatut(StatutOF.PLANIFIE);

        var fichesJour = ficheRepository.listerParPeriode(today, today);
        var fichesSemaine = ficheRepository.listerParPeriode(debutSemaine, finSemaine);

        boolean ficheJourExistante = fichesJour.stream()
            .anyMatch(f -> f.getMatriculeAgentProduction().equals(matriculeAgent));

        return new DashboardAgentProduction(
            today, ofsEnCours.size(), ofsPlanifies.size(),
            fichesSemaine.size(), ficheJourExistante,
            ofsEnCours.stream().map(of -> new OFResume(of.getIdOF(), of.getCodeProduit(),
                of.getDesignationProduit(), of.getQteDemandee(), of.getQteRealisee(),
                of.tauxAvancement())).toList()
        );
    }

    public record DashboardChefProduction(
        double quantiteKgSemaine, int cartonsSemaine, long lotsEnAttenteValidation,
        int ofsEnCours, int ofsPlanifies, int ofsHonores,
        int tauxAvancementPPH, long fichesSemaine,
        String pphEnCoursReference, String pphEnCoursSemaine,
        LocalDate debutSemaine, LocalDate finSemaine
    ) {}

    public record DashboardAgentProduction(
        LocalDate date, int ofsEnCours, int ofsPlanifies,
        int fichesSemaine, boolean ficheJourExistante,
        List<OFResume> ofs
    ) {}

    public record OFResume(
        String idOF, String codeProduit, String designationProduit,
        int qteDemandee, int qteRealisee, int tauxAvancement
    ) {}
}
