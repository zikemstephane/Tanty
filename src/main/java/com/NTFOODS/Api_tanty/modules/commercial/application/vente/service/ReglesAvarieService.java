package com.NTFOODS.Api_tanty.modules.commercial.application.vente.service;

import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.ReglesAvarie;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.ReglesAvarieRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** Administration et résolution des règles de déclaration d'avarie. */
@Service
@Transactional
public class ReglesAvarieService {

    private static final DateTimeFormatter FORMAT_REFERENCE = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final ReglesAvarieRepository repository;

    public ReglesAvarieService(ReglesAvarieRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public ReglesAvarie reglesApplicablesA(LocalDate date) {
        return repository.trouverApplicableA(date).orElseGet(ReglesAvarie::reglesHistoriques);
    }

    @Transactional(readOnly = true)
    public ReglesAvarie reglesCourantes() {
        return repository.trouverCourant().orElseGet(ReglesAvarie::reglesHistoriques);
    }

    @Transactional(readOnly = true)
    public List<ReglesAvarie> historique() {
        return repository.listerHistorique();
    }

    public ReglesAvarie ouvrirNouvellesRegles(int quantiteSeuilPhotoObligatoire, LocalDate dateEffet,
                                               String matriculeAuteur, String motif) {
        repository.trouverCourant().ifPresent(courant -> {
            if (!dateEffet.isAfter(courant.getDateDebut())) {
                throw new RegleMetierException(
                    "Les nouvelles règles doivent prendre effet après le " + courant.getDateDebut()
                    + ", date d'entrée en vigueur des règles courantes " + courant.getReference() + ".");
            }
            courant.cloturerAvant(dateEffet);
            repository.sauvegarder(courant);
        });

        ReglesAvarie nouvelles = ReglesAvarie.creer(genererReference(dateEffet),
            quantiteSeuilPhotoObligatoire, dateEffet, matriculeAuteur, motif);
        return repository.sauvegarder(nouvelles);
    }

    private String genererReference(LocalDate dateEffet) {
        String base = "REGLES-AVARIE-" + dateEffet.format(FORMAT_REFERENCE);
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
