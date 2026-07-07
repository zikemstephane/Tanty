package com.NTFOODS.Api_tanty.modules.comptabilite.application.dashboard.usecase;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.aggregate.JournalCaisse;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.repository.JournalCaisseRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.aggregate.Decaissement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.enums.StatutDecaissement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.repository.DecaissementRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.aggregate.Facture;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.enums.StatutFacture;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.enums.TypeFacture;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.repository.FactureRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.aggregate.PrimeCommerciale;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.enums.StatutPrime;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.repository.PrimeRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.aggregate.Versement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.enums.StatutVersement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.repository.VersementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DashboardComptabiliteUseCase {

    private final JournalCaisseRepository caisseRepository;
    private final FactureRepository factureRepository;
    private final DecaissementRepository decaissementRepository;
    private final VersementRepository versementRepository;
    private final PrimeRepository primeRepository;

    public DashboardComptabiliteUseCase(JournalCaisseRepository caisseRepository,
                                          FactureRepository factureRepository,
                                          DecaissementRepository decaissementRepository,
                                          VersementRepository versementRepository,
                                          PrimeRepository primeRepository) {
        this.caisseRepository = caisseRepository;
        this.factureRepository = factureRepository;
        this.decaissementRepository = decaissementRepository;
        this.versementRepository = versementRepository;
        this.primeRepository = primeRepository;
    }

    public DashboardComptabilite executer() {
        LocalDate today = LocalDate.now();

        Optional<JournalCaisse> caisseJour = caisseRepository.trouverParDate(today);
        double soldeCaisse = caisseJour.map(JournalCaisse::getSoldeCourant).orElse(0.0);
        double totalEntrees = caisseJour.map(JournalCaisse::getTotalEntrees).orElse(0.0);
        double totalSorties = caisseJour.map(JournalCaisse::getTotalSorties).orElse(0.0);

        List<Facture> facturesJour = factureRepository.listerParPeriode(today, today);
        double totalFacturesEmises = facturesJour.stream()
            .filter(f -> f.getTypeFacture() == TypeFacture.FACTURE)
            .mapToDouble(Facture::getMontantTotalTTC).sum();
        double totalFacturesPayees = facturesJour.stream()
            .filter(f -> f.getStatut() == StatutFacture.PAYEE)
            .mapToDouble(Facture::getMontantTotalTTC).sum();
        int nbFacturesImpayees = (int) facturesJour.stream()
            .filter(f -> f.getStatut() == StatutFacture.EMISE).count();

        List<Decaissement> decaissementsJour = decaissementRepository.listerParPeriode(today, today);
        double totalDecaissementsExecutes = decaissementsJour.stream()
            .filter(d -> d.getStatut() == StatutDecaissement.EXECUTE)
            .mapToDouble(Decaissement::getMontantFCFA).sum();
        int nbDecaissementsEnAttente = (int) decaissementsJour.stream()
            .filter(d -> d.getStatut() == StatutDecaissement.PROPOSE
                || d.getStatut() == StatutDecaissement.VALIDEE_CG
                || d.getStatut() == StatutDecaissement.VALIDEE_DG).count();

        List<Versement> versementsJour = versementRepository.listerParDate(today);
        int nbVersementsEnAttente = versementsJour.stream()
            .filter(v -> v.getStatut() == StatutVersement.EN_ATTENTE).toList().size();
        int nbAlertesRouges = versementRepository.listerAlertesRouges().size();

        List<PrimeCommerciale> primesValidees = primeRepository.listerParStatut(StatutPrime.VALIDEE);
        double totalPrimesAVerser = primesValidees.stream()
            .mapToDouble(PrimeCommerciale::getMontantPrimeFCFA).sum();

        return new DashboardComptabilite(
            today, soldeCaisse, totalEntrees, totalSorties,
            totalFacturesEmises, totalFacturesPayees, nbFacturesImpayees,
            totalDecaissementsExecutes, nbDecaissementsEnAttente,
            nbVersementsEnAttente, nbAlertesRouges,
            primesValidees.size(), totalPrimesAVerser
        );
    }

    public record DashboardComptabilite(
        LocalDate date,
        double soldeCaisse,
        double totalEntreesCaisse,
        double totalSortiesCaisse,
        double totalFacturesEmises,
        double totalFacturesPayees,
        int nbFacturesImpayees,
        double totalDecaissementsExecutes,
        int nbDecaissementsEnAttente,
        int nbVersementsEnAttente,
        int nbAlertesRouges,
        int nbPrimesValideesNonVersées,
        double totalPrimesAVerser
    ) {}
}
