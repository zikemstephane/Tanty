package com.NTFOODS.Api_tanty.modules.commercial.application.precommande.service;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.enums.StatutVersement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.repository.VersementRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.enums.StatutSession;
import com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.model.SessionCommercial;
import com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.repository.SessionCommercialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * RG-01 — Un commercial ne peut préparer la journée J+1 que si la journée J est apurée.
 *
 * <p>Deux conditions, et une exception qui compte autant qu'elles.</p>
 *
 * <ol>
 *   <li>Le <strong>versement financier</strong> de la journée écoulée a été validé
 *       par la caisse.</li>
 *   <li>L'<strong>inventaire de retour</strong> de cette journée est clôturé et
 *       validé par le siège.</li>
 * </ol>
 *
 * <p><strong>Démarrage à froid.</strong> Un commercial qui ouvre l'application pour
 * la première fois n'a ni versement ni inventaire : il n'a jamais fait de tournée.
 * Rien n'est à apurer, donc rien ne doit être bloqué. Traiter l'absence de journée
 * antérieure comme une journée non apurée l'enfermerait dehors — il ne pourrait
 * jamais commander, donc jamais vendre, donc jamais verser. La règle porte sur une
 * journée qui existe ; s'il n'y en a pas, elle ne s'applique pas.</p>
 *
 * <p>Cette règle vivait jusqu'ici dans l'écran mobile, sous forme de deux booléens
 * qu'un appui sur une pastille faisait basculer — une simulation. Le serveur ne
 * vérifiait rien. Elle est ramenée ici : le terminal affiche la réponse, il ne la
 * fabrique pas.</p>
 */
@Service
public class EligibilitePrecommandeService {

    private final SessionCommercialRepository sessionRepository;
    private final VersementRepository versementRepository;

    public EligibilitePrecommandeService(SessionCommercialRepository sessionRepository,
                                          VersementRepository versementRepository) {
        this.sessionRepository = sessionRepository;
        this.versementRepository = versementRepository;
    }

    @Transactional(readOnly = true)
    public Eligibilite evaluer(String matriculeCommercial) {
        Optional<SessionCommercial> derniereJournee = derniereJourneeTravaillee(matriculeCommercial);

        if (derniereJournee.isEmpty()) {
            return Eligibilite.aucuneJourneeAApurer();
        }

        SessionCommercial journee = derniereJournee.get();
        boolean retourCloture = inventaireApure(journee);
        boolean versementValide = versementValide(matriculeCommercial, journee.getDateEffet());

        return new Eligibilite(
            versementValide && retourCloture,
            false,
            versementValide,
            retourCloture,
            journee.getDateEffet(),
            motif(versementValide, retourCloture, journee.getDateEffet()));
    }

    /**
     * Dernière journée pour laquelle le commercial a réellement travaillé.
     *
     * <p>Une session en préparation ne compte pas : elle décrit une journée à venir,
     * pas une journée à apurer. Bloquer dessus reviendrait à interdire la commande
     * du lendemain au motif que le lendemain n'a pas encore eu lieu.</p>
     */
    private Optional<SessionCommercial> derniereJourneeTravaillee(String matriculeCommercial) {
        List<SessionCommercial> sessions = sessionRepository.listerParCommercial(matriculeCommercial);
        return sessions.stream()
            .filter(s -> s.getStatut() != StatutSession.PREPARATION)
            .max(Comparator.comparing(SessionCommercial::getDateEffet));
    }

    /**
     * L'inventaire de retour est apuré lorsque la session a franchi la validation
     * du siège. Une session simplement clôturée par le commercial ne suffit pas :
     * elle n'a pas encore été confrontée au comptage.
     */
    private boolean inventaireApure(SessionCommercial journee) {
        return journee.getStatut() == StatutSession.VALIDE_SECRETAIRE
            || journee.getStatut() == StatutSession.VALIDE_COMPTABLE
            || journee.getStatut() == StatutSession.TERMINEE;
    }

    /**
     * Le versement de la journée est validé par la caisse.
     *
     * <p>{@code JUSTIFIE} vaut acceptation : un écart expliqué et accepté par la
     * comptabilité clôt la journée au même titre qu'un versement exact. Seuls
     * {@code EN_ATTENTE} et {@code ALERTE_ROUGE} laissent la journée ouverte.</p>
     */
    private boolean versementValide(String matriculeCommercial, LocalDate date) {
        return versementRepository.listerParCommercial(matriculeCommercial).stream()
            .filter(v -> date.equals(v.getDate()))
            .anyMatch(v -> v.getStatut() == StatutVersement.VALIDE
                        || v.getStatut() == StatutVersement.JUSTIFIE);
    }

    private String motif(boolean versementValide, boolean retourCloture, LocalDate journee) {
        if (versementValide && retourCloture) {
            return "Journée du " + journee + " apurée : la préparation du lendemain est ouverte.";
        }
        if (!versementValide && !retourCloture) {
            return "Journée du " + journee + " non apurée : le versement n'est pas validé par la caisse "
                 + "et l'inventaire de retour n'est pas clôturé.";
        }
        if (!versementValide) {
            return "Journée du " + journee + " non apurée : le versement n'est pas validé par la caisse.";
        }
        return "Journée du " + journee + " non apurée : l'inventaire de retour n'est pas clôturé.";
    }

    /**
     * @param autorisee          la saisie J+1 est ouverte
     * @param premiereUtilisation aucune journée antérieure : rien à apurer
     * @param versementValide     versement de la journée écoulée validé par la caisse
     * @param retourCloture       inventaire de retour clôturé et validé
     * @param journeeConcernee    journée sur laquelle porte l'évaluation, nulle au premier usage
     * @param motif               formulation destinée au commercial
     */
    public record Eligibilite(
        boolean autorisee,
        boolean premiereUtilisation,
        boolean versementValide,
        boolean retourCloture,
        LocalDate journeeConcernee,
        String motif
    ) {
        /** Fabrique nommée distinctement de l'accesseur du même concept. */
        static Eligibilite aucuneJourneeAApurer() {
            return new Eligibilite(true, true, false, false, null,
                "Première tournée : aucune journée antérieure à apurer.");
        }
    }
}
