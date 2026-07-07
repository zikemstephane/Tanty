package com.NTFOODS.Api_tanty.modules.dg.application.usecase;

import com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.enums.StatutRecouvrement;
import com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.repository.RecouvrementRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.repository.JournalCaisseRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.enums.StatutDecaissement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.repository.DecaissementRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.enums.StatutFacture;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.enums.TypeFacture;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.repository.FactureRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.enums.StatutPrime;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.repository.PrimeRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.enums.StatutVersement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.repository.VersementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
public class DashboardFinancierDgUseCase {

    private final JournalCaisseRepository caisseRepository;
    private final FactureRepository factureRepository;
    private final DecaissementRepository decaissementRepository;
    private final VersementRepository versementRepository;
    private final PrimeRepository primeRepository;
    private final RecouvrementRepository recouvrementRepository;

    public DashboardFinancierDgUseCase(JournalCaisseRepository caisseRepository,
                                        FactureRepository factureRepository,
                                        DecaissementRepository decaissementRepository,
                                        VersementRepository versementRepository,
                                        PrimeRepository primeRepository,
                                        RecouvrementRepository recouvrementRepository) {
        this.caisseRepository = caisseRepository;
        this.factureRepository = factureRepository;
        this.decaissementRepository = decaissementRepository;
        this.versementRepository = versementRepository;
        this.primeRepository = primeRepository;
        this.recouvrementRepository = recouvrementRepository;
    }

    public DashboardFinancier executer() {
        LocalDate today = LocalDate.now();

        double soldeCaisse = caisseRepository.trouverParDate(today)
            .map(j -> j.getSoldeCourant()).orElse(0.0);

        var facturesJour = factureRepository.listerParPeriode(today, today);
        double totalFacturesEmises = facturesJour.stream()
            .filter(f -> f.getTypeFacture() == TypeFacture.FACTURE)
            .mapToDouble(f -> f.getMontantTotalTTC()).sum();
        double totalFacturesPayees = facturesJour.stream()
            .filter(f -> f.getStatut() == StatutFacture.PAYEE)
            .mapToDouble(f -> f.getMontantTotalTTC()).sum();
        int nbFacturesImpayees = (int) facturesJour.stream()
            .filter(f -> f.getStatut() == StatutFacture.EMISE).count();

        double totalDecaissementsExecutes = decaissementRepository
            .listerParStatut(StatutDecaissement.EXECUTE).stream()
            .mapToDouble(d -> d.getMontantFCFA()).sum();
        int nbDecaissementsEnAttente = decaissementRepository
            .listerParStatut(StatutDecaissement.PROPOSE).size()
            + decaissementRepository.listerParStatut(StatutDecaissement.VALIDEE_CG).size()
            + decaissementRepository.listerParStatut(StatutDecaissement.VALIDEE_DG).size();

        int nbVersementsEnAttente = versementRepository.listerParStatut(StatutVersement.EN_ATTENTE).size();
        int nbAlertesRouges = versementRepository.listerAlertesRouges().size();
        double totalVersementsValides = versementRepository.listerParStatut(StatutVersement.VALIDE).stream()
            .mapToDouble(v -> v.getCashVerse()).sum();

        var primesValidees = primeRepository.listerParStatut(StatutPrime.VALIDEE);
        double totalPrimesAVerser = primesValidees.stream()
            .mapToDouble(p -> p.getMontantPrimeFCFA()).sum();

        int nbRecouvrementsEnCours = recouvrementRepository.listerParStatut(StatutRecouvrement.EN_COURS).size();
        int nbRecouvrementsContentieux = recouvrementRepository.listerEnContentieux().size();
        double totalCreances = recouvrementRepository.listerParStatut(StatutRecouvrement.EN_COURS).stream()
            .mapToDouble(r -> r.montantRestant()).sum()
            + recouvrementRepository.listerEnContentieux().stream()
            .mapToDouble(r -> r.montantRestant()).sum();

        return new DashboardFinancier(
            today, soldeCaisse,
            totalFacturesEmises, totalFacturesPayees, nbFacturesImpayees,
            totalDecaissementsExecutes, nbDecaissementsEnAttente,
            nbVersementsEnAttente, nbAlertesRouges, totalVersementsValides,
            primesValidees.size(), totalPrimesAVerser,
            nbRecouvrementsEnCours, nbRecouvrementsContentieux, totalCreances
        );
    }

    public record DashboardFinancier(
        LocalDate date,
        double soldeCaisse,
        double totalFacturesEmises,
        double totalFacturesPayees,
        int nbFacturesImpayees,
        double totalDecaissementsExecutes,
        int nbDecaissementsEnAttente,
        int nbVersementsEnAttente,
        int nbAlertesRouges,
        double totalVersementsValides,
        int nbPrimesValideesNonVersées,
        double totalPrimesAVerser,
        int nbRecouvrementsEnCours,
        int nbRecouvrementsContentieux,
        double totalCreances
    ) {}
}
