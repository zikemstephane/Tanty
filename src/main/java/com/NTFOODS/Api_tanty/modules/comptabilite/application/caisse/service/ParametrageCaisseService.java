package com.NTFOODS.Api_tanty.modules.comptabilite.application.caisse.service;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.aggregate.ParametrageCaisse;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.repository.ParametrageCaisseRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Administration et résolution du paramétrage de caisse.
 *
 * <p>Source unique des deux seuils qui étaient auparavant dispersés entre
 * l'agrégat {@code JournalCaisse}, le tableau de bord de la secrétaire et le
 * fichier de configuration.</p>
 */
@Service
@Transactional
public class ParametrageCaisseService {

    private static final DateTimeFormatter FORMAT_REFERENCE = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final ParametrageCaisseRepository repository;

    public ParametrageCaisseService(ParametrageCaisseRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public ParametrageCaisse parametrageApplicableA(LocalDate date) {
        return repository.trouverApplicableA(date).orElseGet(ParametrageCaisse::reglesHistoriques);
    }

    @Transactional(readOnly = true)
    public ParametrageCaisse parametrageCourant() {
        return repository.trouverCourant().orElseGet(ParametrageCaisse::reglesHistoriques);
    }

    @Transactional(readOnly = true)
    public List<ParametrageCaisse> historique() {
        return repository.listerHistorique();
    }

    public ParametrageCaisse ouvrirNouveauParametrage(double seuilSecurisationFCFA,
                                                       double seuilAlerteRougeVersementFCFA,
                                                       LocalDate dateEffet, String matriculeAuteur,
                                                       String motif) {
        repository.trouverCourant().ifPresent(courant -> {
            if (!dateEffet.isAfter(courant.getDateDebut())) {
                throw new RegleMetierException(
                    "Le nouveau paramétrage doit prendre effet après le " + courant.getDateDebut()
                    + ", date d'entrée en vigueur du paramétrage courant " + courant.getReference() + ".");
            }
            courant.cloturerAvant(dateEffet);
            repository.sauvegarder(courant);
        });

        ParametrageCaisse nouveau = ParametrageCaisse.creer(genererReference(dateEffet),
            seuilSecurisationFCFA, seuilAlerteRougeVersementFCFA, dateEffet, matriculeAuteur, motif);
        return repository.sauvegarder(nouveau);
    }

    private String genererReference(LocalDate dateEffet) {
        String base = "PARAM-CAISSE-" + dateEffet.format(FORMAT_REFERENCE);
        if (repository.trouverParReference(base).isEmpty()) {
            return base;
        }
        int suffixe = 2;
        while (repository.trouverParReference(base + "-" + suffixe).isPresent()) {
            suffixe++;
        }
        return base + "-" + suffixe;
    }
}
