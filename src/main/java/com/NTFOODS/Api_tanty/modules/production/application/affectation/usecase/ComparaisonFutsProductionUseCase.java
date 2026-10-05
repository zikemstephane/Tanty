package com.NTFOODS.Api_tanty.modules.production.application.affectation.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response.ComparaisonFutsProductionResponse;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.service.PosteProductionService;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.aggregate.AffectationJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.SaisieRealisationPoste;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.repository.AffectationJournaliereRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.aggregate.SessionDosage;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.repository.SessionDosageRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Compare les fûts nets dosés, pour une journée et un PPH, avec la production réalisée en kg.
 * Toutes les sessions de dosage du PPH pour la date sont additionnées. Seuls les postes dont l'unité est le kg
 * entrent dans la production en kg : les sachets, sceaux et étiquettes ne sont pas convertis.
 */
@Service
@Transactional(readOnly = true)
public class ComparaisonFutsProductionUseCase {

    static final double KG_PAR_FUT = 50.0;

    private final SessionDosageRepository dosageRepository;
    private final AffectationJournaliereRepository affectationRepository;
    private final PosteProductionService posteService;

    public ComparaisonFutsProductionUseCase(SessionDosageRepository dosageRepository,
                                             AffectationJournaliereRepository affectationRepository,
                                             PosteProductionService posteService) {
        this.dosageRepository = dosageRepository;
        this.affectationRepository = affectationRepository;
        this.posteService = posteService;
    }

    public ComparaisonFutsProductionResponse executer(String referencePPH, LocalDate date) {
        List<SessionDosage> sessions = sessionsDuJour(referencePPH, date);
        if (sessions.isEmpty()) {
            throw EntiteNonTrouveeException.de("SessionDosage", referencePPH + " / " + date);
        }

        AffectationJournaliere affectation = affectationRepository.trouverParPPHEtDate(referencePPH, date).orElse(null);

        int nbFutsNets = sessions.stream().mapToInt(SessionDosage::getNbFutsNets).sum();
        double poidsTotalPoudreKg = nbFutsNets * KG_PAR_FUT;
        double productionReelleKg = calculerProductionKg(affectation);
        double ecartKg = productionReelleKg - poidsTotalPoudreKg;
        double ecartPct = poidsTotalPoudreKg > 0 ? (ecartKg / poidsTotalPoudreKg) * 100.0 : 0.0;

        return new ComparaisonFutsProductionResponse(
            referencePPH, date, nbFutsNets, poidsTotalPoudreKg, productionReelleKg,
            ecartKg, ecartPct, "kg");
    }

    /** Sessions de dosage du PPH pour la date (un agent doseur peut en ouvrir plusieurs par jour). */
    List<SessionDosage> sessionsDuJour(String referencePPH, LocalDate date) {
        return dosageRepository.listerParDate(date).stream()
            .filter(s -> referencePPH.equals(s.getReferencePPH()))
            .toList();
    }

    /** Production réalisée dont l'unité est le kg ; les autres unités sont exclues. */
    double calculerProductionKg(AffectationJournaliere affectation) {
        if (affectation == null) return 0.0;
        Map<String, String> unites = unitesParPoste();
        return affectation.getSaisies().stream()
            .filter(s -> "KG".equalsIgnoreCase(unites.get(s.getPoste())))
            .mapToDouble(SaisieRealisationPoste::getQuantiteRealisee)
            .sum();
    }

    Map<String, String> unitesParPoste() {
        return posteService.listerTous().stream()
            .collect(Collectors.toMap(p -> p.getCode(), p -> p.getUnite(), (a, b) -> a));
    }
}
