package com.NTFOODS.Api_tanty.modules.comptabilite.application.prime.service;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.aggregate.BaremePrime;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.repository.BaremePrimeRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Administration et résolution du barème des primes.
 *
 * <p>Ouvrir un barème clôture le précédent : les périodes se suivent sans se
 * chevaucher ni laisser de vide, condition pour qu'une date donnée désigne
 * toujours un barème et un seul.</p>
 */
@Service
@Transactional
public class BaremePrimeService {

    private static final DateTimeFormatter FORMAT_REFERENCE = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final BaremePrimeRepository repository;

    public BaremePrimeService(BaremePrimeRepository repository) {
        this.repository = repository;
    }

    /**
     * Barème régissant une date.
     *
     * <p>À défaut de barème couvrant la date — notamment pour une semaine antérieure
     * à la mise en place du paramétrage — les valeurs historiques s'appliquent, afin
     * qu'aucun recalcul ne produise un montant différent de celui d'origine.</p>
     */
    @Transactional(readOnly = true)
    public BaremePrime baremeApplicableA(LocalDate date) {
        return repository.trouverApplicableA(date).orElseGet(BaremePrime::reglesHistoriques);
    }

    @Transactional(readOnly = true)
    public BaremePrime baremeCourant() {
        return repository.trouverCourant().orElseGet(BaremePrime::reglesHistoriques);
    }

    @Transactional(readOnly = true)
    public List<BaremePrime> historique() {
        return repository.listerHistorique();
    }

    /**
     * Met un nouveau barème en vigueur à compter d'une date.
     *
     * @throws RegleMetierException si la prise d'effet n'est pas postérieure à celle
     *                              du barème courant, ce qui rendrait l'ordre des
     *                              périodes indéterminé
     */
    public BaremePrime ouvrirNouveauBareme(double tauxGlobalMinimumPourcent, double tauxGammeMinimumPourcent,
                                            double montantParGammeFCFA, double montantMaximumFCFA,
                                            LocalDate dateEffet, String matriculeAuteur, String motif) {
        repository.trouverCourant().ifPresent(courant -> {
            if (!dateEffet.isAfter(courant.getDateDebut())) {
                throw new RegleMetierException(
                    "Le nouveau barème doit prendre effet après le " + courant.getDateDebut()
                    + ", date d'entrée en vigueur du barème courant " + courant.getReference() + ".");
            }
            courant.cloturerAvant(dateEffet);
            repository.sauvegarder(courant);
        });

        BaremePrime nouveau = BaremePrime.creer(genererReference(dateEffet), tauxGlobalMinimumPourcent,
            tauxGammeMinimumPourcent, montantParGammeFCFA, montantMaximumFCFA, dateEffet,
            matriculeAuteur, motif);
        return repository.sauvegarder(nouveau);
    }

    private String genererReference(LocalDate dateEffet) {
        String base = "BAREME-" + dateEffet.format(FORMAT_REFERENCE);
        if (repository.trouverParReference(base).isEmpty()) {
            return base;
        }
        // Deux barèmes prenant effet le même jour restent distinguables.
        int suffixe = 2;
        while (repository.trouverParReference(base + "-" + suffixe).isPresent()) {
            suffixe++;
        }
        return base + "-" + suffixe;
    }
}
