package com.NTFOODS.Api_tanty.modules.comptabilite.application.objectif.service;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.aggregate.ParametrageObjectif;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.repository.ParametrageObjectifRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** Administration et résolution du paramétrage des objectifs commerciaux. */
@Service
@Transactional
public class ParametrageObjectifService {

    private static final DateTimeFormatter FORMAT_REFERENCE = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final ParametrageObjectifRepository repository;

    public ParametrageObjectifService(ParametrageObjectifRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public ParametrageObjectif parametrageApplicableA(LocalDate date) {
        return repository.trouverApplicableA(date).orElseGet(ParametrageObjectif::reglesHistoriques);
    }

    @Transactional(readOnly = true)
    public ParametrageObjectif parametrageCourant() {
        return repository.trouverCourant().orElseGet(ParametrageObjectif::reglesHistoriques);
    }

    @Transactional(readOnly = true)
    public List<ParametrageObjectif> historique() {
        return repository.listerHistorique();
    }

    public ParametrageObjectif ouvrirNouveauParametrage(int nombreJoursOuvres, LocalDate dateEffet,
                                                         String matriculeAuteur, String motif) {
        repository.trouverCourant().ifPresent(courant -> {
            if (!dateEffet.isAfter(courant.getDateDebut())) {
                throw new RegleMetierException(
                    "Le nouveau paramétrage doit prendre effet après le " + courant.getDateDebut()
                    + ", date d'entrée en vigueur du paramétrage courant " + courant.getReference() + ".");
            }
            courant.cloturerAvant(dateEffet);
            repository.sauvegarder(courant);
        });

        ParametrageObjectif nouveau = ParametrageObjectif.creer(genererReference(dateEffet),
            nombreJoursOuvres, dateEffet, matriculeAuteur, motif);
        return repository.sauvegarder(nouveau);
    }

    private String genererReference(LocalDate dateEffet) {
        String base = "PARAM-OBJECTIF-" + dateEffet.format(FORMAT_REFERENCE);
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
