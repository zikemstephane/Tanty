package com.NTFOODS.Api_tanty.modules.dg.application.usecase;

import com.NTFOODS.Api_tanty.modules.production.domain.lot.aggregate.LotProduitFini;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.enums.StatutLot;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.repository.LotRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.of.enums.StatutOF;
import com.NTFOODS.Api_tanty.modules.production.domain.of.repository.OFRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.enums.StatutPPH;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
public class DashboardProductionDgUseCase {

    private final PPHRepository pphRepository;
    private final OFRepository ofRepository;
    private final LotRepository lotRepository;

    public DashboardProductionDgUseCase(PPHRepository pphRepository,
                                         OFRepository ofRepository,
                                         LotRepository lotRepository) {
        this.pphRepository = pphRepository;
        this.ofRepository = ofRepository;
        this.lotRepository = lotRepository;
    }

    public DashboardProduction executer() {
        LocalDate today = LocalDate.now();

        int nbPPHEnCours = pphRepository.listerParStatut(StatutPPH.EN_COURS).size();
        int nbPPHClotures = pphRepository.listerParStatut(StatutPPH.CLOTURE).size();

        int nbOFEnCours = ofRepository.listerParStatut(StatutOF.EN_COURS).size();
        int nbOFHonores = ofRepository.listerParStatut(StatutOF.HONORE).size();
        int nbOFAnnules = ofRepository.listerParStatut(StatutOF.ANNULE).size();
        int nbOFPlanifies = ofRepository.listerParStatut(StatutOF.PLANIFIE).size();

        var lotsDeclares = lotRepository.listerParStatut(StatutLot.DECLARE);
        var lotsReceptionnes = lotRepository.listerParStatut(StatutLot.RECEPTIONNE_STOCK);
        var lotsValides = lotRepository.listerParStatut(StatutLot.VALIDATED_BY_STOCK);
        var lotsRejetes = lotRepository.listerParStatut(StatutLot.REJETE);

        var lotsJour = lotRepository.listerParPeriode(today, today);
        double quantiteProduiteKg = lotsJour.stream()
            .mapToDouble(LotProduitFini::getDeclaredQuantityKg).sum();
        int nbCartons = lotsJour.stream()
            .mapToInt(LotProduitFini::getEquivalentUnits).sum();

        return new DashboardProduction(
            today,
            nbPPHEnCours, nbPPHClotures,
            nbOFPlanifies, nbOFEnCours, nbOFHonores, nbOFAnnules,
            lotsDeclares.size(), lotsReceptionnes.size(), lotsValides.size(), lotsRejetes.size(),
            quantiteProduiteKg, nbCartons
        );
    }

    public record DashboardProduction(
        LocalDate date,
        int nbPPHEnCours,
        int nbPPHClotures,
        int nbOFPlanifies,
        int nbOFEnCours,
        int nbOFHonores,
        int nbOFAnnules,
        int nbLotsDeclares,
        int nbLotsReceptionnes,
        int nbLotsValides,
        int nbLotsRejetes,
        double quantiteProduiteKg,
        int nbCartons
    ) {}
}
