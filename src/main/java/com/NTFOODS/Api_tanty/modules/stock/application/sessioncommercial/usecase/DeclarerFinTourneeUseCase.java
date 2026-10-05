package com.NTFOODS.Api_tanty.modules.stock.application.sessioncommercial.usecase;

import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.aggregate.FeuillRoute;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.repository.FeuillRouteRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.model.SessionCommercial;
import com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.repository.SessionCommercialRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Use Case — Le commercial déclare sa journée terminée.
 *
 * <p>Depuis que la tournée n'a plus d'état propre, c'est cette clôture qui la
 * ferme aussi. D'où la vérification portée ici : une journée ne se clôt pas sur
 * des clients dont personne ne sait s'ils ont été vus. Auparavant les deux
 * cycles vivaient côte à côte sans se parler, et une tournée à moitié saisie
 * partait au siège sans que rien ne le signale.</p>
 *
 * <p>Les arrêts encore en attente sont énoncés nommément : « il reste des
 * clients » obligerait le commercial à parcourir sa liste pour trouver
 * lesquels.</p>
 */
@Service
@Transactional
public class DeclarerFinTourneeUseCase {

    private final SessionCommercialRepository sessionRepository;
    private final FeuillRouteRepository feuillRouteRepository;

    public DeclarerFinTourneeUseCase(SessionCommercialRepository sessionRepository,
                                      FeuillRouteRepository feuillRouteRepository) {
        this.sessionRepository = sessionRepository;
        this.feuillRouteRepository = feuillRouteRepository;
    }

    public SessionCommercial executer(String numeroSession, String matriculeCloture) {
        SessionCommercial session = sessionRepository.trouverParNumero(numeroSession)
            .orElseThrow(() -> new EntiteNonTrouveeException("Session introuvable : " + numeroSession));

        exigerTourneeTranchee(session);

        session.cloturer(matriculeCloture);
        return sessionRepository.sauvegarder(session);
    }

    /**
     * Une journée sans tournée planifiée reste clôturable : le commercial a pu
     * passer sa journée sur autre chose. C'est la tournée <em>inachevée</em> qui
     * bloque, pas son absence.
     */
    private void exigerTourneeTranchee(SessionCommercial session) {
        List<FeuillRoute> tournees = feuillRouteRepository.listerParCommercialEtDate(
            session.getMatriculeCommercial(), session.getDateEffet());
        if (tournees.isEmpty()) return;

        FeuillRoute tournee = tournees.get(0);
        if (!tournee.tousArretsTranches()) {
            throw new RegleMetierException("Votre tournée n'est pas terminée : "
                + String.join(", ", tournee.clientsEnAttente())
                + " — indiquez pour chacun s'il a été visité avant de clôturer la journée");
        }
    }
}
