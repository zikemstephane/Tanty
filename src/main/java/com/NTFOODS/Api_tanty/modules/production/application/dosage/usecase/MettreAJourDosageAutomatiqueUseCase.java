package com.NTFOODS.Api_tanty.modules.production.application.dosage.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.dosage.service.ResolveurFormuleParSessionService;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.aggregate.SessionDosage;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.repository.SessionDosageRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.aggregate.FormuleProduction;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.entity.BesoinMatierePremiere;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.Locale;
import java.util.List;
import java.util.Optional;

/**
 * Use Case — Mettre à jour automatiquement les matières premières d'une
 * session à partir du nombre de fûts produits.
 *
 * Les quantités par matière première viennent de la {@link FormuleProduction}
 * du/des produit(s) réellement commandé(s) sur le BC associé à la session
 * (via son PPH), jamais d'un « type de mélange » choisi à la main ni d'une
 * recette figée : ce use case portait auparavant deux ratios maïs/soja/
 * arachide en dur (40/9/1 et 40/1/9) sélectionnés par un paramètre
 * {@code typeMelange} — supprimé, car strictement redondant avec la formule
 * du produit une fois celle-ci disponible, et potentiellement incohérent
 * avec elle si l'agent se trompait de sélection.
 */
@Service
@Transactional
public class MettreAJourDosageAutomatiqueUseCase {

    private static final double POUDRE_PAR_FUT_KG = 50.0;

    private final SessionDosageRepository repository;
    private final ResolveurFormuleParSessionService resolveurFormule;

    public MettreAJourDosageAutomatiqueUseCase(SessionDosageRepository repository,
                                               ResolveurFormuleParSessionService resolveurFormule) {
        this.repository = repository;
        this.resolveurFormule = resolveurFormule;
    }

    public SessionDosage executer(Long sessionId, int nbFuts) {
        if (nbFuts <= 0) {
            throw new IllegalArgumentException("Le nombre de fûts doit être positif");
        }

        SessionDosage session = repository.trouverParId(sessionId)
            .orElseThrow(() -> EntiteNonTrouveeException.de("SessionDosage", String.valueOf(sessionId)));

        Optional<FormuleProduction> formuleOpt = resolveurFormule.resoudre(session.getReferencePPH());
        FormuleProduction formule = formuleOpt.orElseThrow(() -> new RegleMetierException(
            "Impossible de déterminer automatiquement les matières premières de la session " + sessionId
                + " : aucune formule de production unique n'a pu être identifiée pour le bon de commande associé "
                + "(produit sans formule, ou plusieurs formules différentes en jeu). Saisir les matières manuellement."));

        BigDecimal poudreTotale = BigDecimal.valueOf(POUDRE_PAR_FUT_KG * nbFuts);
        List<BesoinMatierePremiere> besoins = formule.calculerBesoins(poudreTotale);
        for (BesoinMatierePremiere b : besoins) {
            session.definirMatiereUtilisee(normaliser(b.matierePremiere()), b.quantiteKg().doubleValue());
        }

        return repository.sauvegarder(session);
    }

    /** Convention des matières de session (« MAIS », « SOJA »...) : majuscules sans accents, comme les saisies manuelles de l'Agent Doseur. */
    private String normaliser(String nom) {
        return Normalizer.normalize(nom, Normalizer.Form.NFD).replaceAll("\\p{M}", "").trim().toUpperCase(Locale.ROOT);
    }
}
