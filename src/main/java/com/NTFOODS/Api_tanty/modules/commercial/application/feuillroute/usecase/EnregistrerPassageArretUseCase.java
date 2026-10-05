package com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.usecase;

import com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.service.CodeObservationService;
import com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.service.JourneeCommercialeService;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.aggregate.FeuillRoute;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.SaisieArret;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.repository.FeuillRouteRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.Vente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.VenteRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Use Case — Le commercial constate son passage chez un client.
 *
 * <p>Deux constats, et deux seulement : il est passé, ou il n'a pas pu. Le second
 * porte un code InfoClient qui en donne la raison (§5.9). Le relevé GPS et
 * l'écart au client ont été retirés — ils imposaient d'attendre un point
 * satellite avant chaque marquage, pour une donnée que personne n'exploitait.</p>
 *
 * <p>La tournée est retrouvée par sa <strong>clé naturelle</strong> : un
 * commercial, une date. C'est ce qui rend l'opération rejouable après une saisie
 * hors ligne, où le numéro attribué par le serveur n'est pas connu du terminal.
 * L'arrêt, lui, a toujours été désigné par le code client.</p>
 */
@Service
@Transactional
public class EnregistrerPassageArretUseCase {

    private final FeuillRouteRepository feuillRouteRepository;
    private final VenteRepository venteRepository;
    private final JourneeCommercialeService journee;
    private final CodeObservationService codeObservationService;

    public EnregistrerPassageArretUseCase(FeuillRouteRepository feuillRouteRepository,
                                           VenteRepository venteRepository,
                                           JourneeCommercialeService journee,
                                           CodeObservationService codeObservationService) {
        this.feuillRouteRepository = feuillRouteRepository;
        this.venteRepository = venteRepository;
        this.journee = journee;
        this.codeObservationService = codeObservationService;
    }

    /**
     * Le commercial est passé chez le client.
     *
     * <p>La vente ne se saisit pas : elle se retrouve. Si une vente a été
     * enregistrée pour ce client dans la journée, l'arrêt s'y rattache seul ; un
     * passage sans commande reste un passage.</p>
     */
    public FeuillRoute marquerVisite(String matriculeCommercial, LocalDate date, String codeClient) {
        journee.journeeOuverte(matriculeCommercial, date);
        FeuillRoute tournee = tourneeDuJour(matriculeCommercial, date);
        tournee.marquerVisite(codeClient, venteDuJourChez(matriculeCommercial, date, codeClient));
        return feuillRouteRepository.sauvegarder(tournee);
    }

    /** Le commercial n'a pas pu passer ; le motif est consigné tel quel. */
    public FeuillRoute marquerNonVisite(String matriculeCommercial, LocalDate date, SaisieArret saisie) {
        journee.journeeOuverte(matriculeCommercial, date);
        validerSaisie(saisie);
        FeuillRoute tournee = tourneeDuJour(matriculeCommercial, date);
        tournee.marquerNonVisite(saisie.getCodeClient(), saisie);
        return feuillRouteRepository.sauvegarder(tournee);
    }

    private void validerSaisie(SaisieArret saisie) {
        if (saisie == null || saisie.getCodeInfoClient() == null) {
            throw new IllegalArgumentException("Le motif est obligatoire pour un client non visité");
        }
        boolean exigeQuantite = codeObservationService.exigeQuantite(saisie.getCodeInfoClient());
        if (exigeQuantite && (saisie.getQuantiteInfo() == null || saisie.getQuantiteInfo() <= 0)) {
            throw new IllegalArgumentException(
                "Le code " + saisie.getCodeInfoClient() + " exige une quantité > 0 (§5.9)");
        }
    }

    /** Inscrit un client rencontré en cours de route. */
    public FeuillRoute ajouterArret(String matriculeCommercial, LocalDate date, String codeClient) {
        journee.journeeOuverte(matriculeCommercial, date);
        FeuillRoute tournee = tourneeDuJour(matriculeCommercial, date);
        tournee.ajouterArret(codeClient);
        return feuillRouteRepository.sauvegarder(tournee);
    }

    /**
     * Numéro de la vente faite chez ce client ce jour-là, ou {@code null}.
     *
     * <p>Le rattachement est fait une fois, à l'écriture, plutôt que recalculé à
     * chaque lecture. S'il y a plusieurs ventes, la première suffit à établir que
     * le passage a produit une commande.</p>
     */
    private String venteDuJourChez(String matriculeCommercial, LocalDate date, String codeClient) {
        return venteRepository.listerParCommercialEtDate(matriculeCommercial, date).stream()
            .filter(v -> codeClient.equals(v.getCodeClient()))
            .map(Vente::getNumeroVente)
            .findFirst()
            .orElse(null);
    }

    /**
     * Retrouve la tournée du jour.
     *
     * <p>Un index unique interdit depuis la migration V8 qu'il y en ait deux, mais
     * la garde reste : elle documente ce sur quoi la clé naturelle repose, et
     * couvre une base restaurée d'avant cette migration.</p>
     */
    private FeuillRoute tourneeDuJour(String matriculeCommercial, LocalDate date) {
        List<FeuillRoute> tournees =
            feuillRouteRepository.listerParCommercialEtDate(matriculeCommercial, date);
        if (tournees.isEmpty()) {
            throw new EntiteNonTrouveeException("Aucune tournée pour " + matriculeCommercial
                + " au " + date + " : le passage ne peut être rattaché à rien");
        }
        if (tournees.size() > 1) {
            throw new RegleMetierException("Plusieurs tournées existent pour " + matriculeCommercial
                + " au " + date + " : le passage ne peut pas être rattaché sans ambiguïté");
        }
        return tournees.get(0);
    }
}
