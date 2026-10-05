package com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.service;

import com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.enums.StatutSession;
import com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.model.SessionCommercial;
import com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.repository.SessionCommercialRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * La journée du commercial, vue depuis la tournée.
 *
 * <p>La feuille de route n'a plus d'état propre : c'est la session commerciale
 * qui dit si la journée est en préparation, ouverte ou close. Ce service est le
 * seul point où le module commercial interroge cet état, plutôt que de laisser
 * chaque use case redécouvrir les statuts admis.</p>
 *
 * <p>Les deux cycles de vie coexistaient auparavant sans se parler : on pouvait
 * viser des arrêts sans journée ouverte, et déclarer sa fin de tournée en
 * laissant des clients dont personne ne savait s'ils avaient été vus.</p>
 */
@Service
@Transactional(readOnly = true)
public class JourneeCommercialeService {

    private final SessionCommercialRepository sessionRepository;

    public JourneeCommercialeService(SessionCommercialRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    /**
     * Journée que le commercial peut encore préparer : la veille ou le jour même.
     *
     * <p>Une tournée se planifie sur une journée déjà dotée. Sans dotation, le
     * camion est vide et la tournée n'a rien à vendre.</p>
     */
    public SessionCommercial journeeAPreparer(String matriculeCommercial, LocalDate date) {
        SessionCommercial session = journeeDu(matriculeCommercial, date);
        if (session.getStatut() != StatutSession.PREPARATION
            && session.getStatut() != StatutSession.OUVERTE) {
            throw new RegleMetierException("La journée du " + date + " est " + session.getStatut()
                + " : elle n'accepte plus de tournée");
        }
        return session;
    }

    /**
     * Journée en cours, seule pendant laquelle un passage se constate.
     *
     * <p>Marquer un client visité sur une journée close reviendrait à modifier une
     * tournée déjà remise au siège.</p>
     */
    public SessionCommercial journeeOuverte(String matriculeCommercial, LocalDate date) {
        SessionCommercial session = journeeDu(matriculeCommercial, date);
        if (session.getStatut() != StatutSession.OUVERTE) {
            throw new RegleMetierException("La journée du " + date + " est " + session.getStatut()
                + " : la tournée n'est plus modifiable");
        }
        return session;
    }

    private SessionCommercial journeeDu(String matriculeCommercial, LocalDate date) {
        List<SessionCommercial> journees = sessionRepository.listerParCommercial(matriculeCommercial)
            .stream()
            .filter(s -> date.equals(s.getDateEffet()))
            .toList();
        if (journees.isEmpty()) {
            throw new EntiteNonTrouveeException("Aucune journée de travail pour " + matriculeCommercial
                + " au " + date + " : la dotation doit être préparée avant la tournée");
        }
        if (journees.size() > 1) {
            throw new RegleMetierException("Plusieurs journées existent pour " + matriculeCommercial
                + " au " + date + " : la tournée ne peut pas être rattachée sans ambiguïté");
        }
        return journees.get(0);
    }
}
