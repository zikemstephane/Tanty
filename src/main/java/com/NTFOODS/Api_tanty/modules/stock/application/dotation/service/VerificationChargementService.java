package com.NTFOODS.Api_tanty.modules.stock.application.dotation.service;

import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.aggregate.PreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.enums.StatutPreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.repository.PreCommandeRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.enums.StatutVersement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.repository.VersementRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Service applicatif — Double Signature pour le Chargement (Pilier 2).
 *
 * Le Gestionnaire de Stock ne peut libérer les produits pour un commercial
 * (approuver une {@link com.NTFOODS.Api_tanty.modules.stock.domain.dotation.model.Dotation})
 * que si DEUX signatures numériques sont réunies :
 * - la SECRÉTAIRE atteste du versement du cash de la veille (un
 *   {@code Versement} validé — ou justifié après alerte rouge — existe pour
 *   ce commercial à la date de travail correspondante) ;
 * - le COMPTABLE atteste de la validité de la commande (la {@code PreCommande}
 *   du commercial pour cette date d'effet a atteint le statut
 *   {@code VALIDEE_COMPTABLE}, ce qui implique déjà la validation secrétaire
 *   dans le cycle de la précommande).
 *
 * Le module Stock ne gère jamais l'argent lui-même (règle d'isolation) — ce
 * service se contente de CONSTATER que ces deux validations existent déjà
 * dans les modules Commercial/Comptabilité, sans jamais manipuler de montant.
 */
@Service
public class VerificationChargementService {

    private final PreCommandeRepository preCommandeRepository;
    private final VersementRepository versementRepository;

    public VerificationChargementService(PreCommandeRepository preCommandeRepository,
                                          VersementRepository versementRepository) {
        this.preCommandeRepository = preCommandeRepository;
        this.versementRepository = versementRepository;
    }

    /**
     * @param matriculeCommercial commercial concerné par la dotation
     * @param dateDotation        date de travail du commercial (veille de la livraison) — c'est
     *                             la date à laquelle le cash aurait dû être versé à la secrétaire
     * @param dateEffet            date de livraison effective de la dotation (lendemain)
     * @throws RegleMetierException si l'une des deux signatures manque
     */
    public void verifierDoubleSignature(String matriculeCommercial, LocalDate dateDotation, LocalDate dateEffet) {
        boolean signatureComptable = preCommandeRepository.listerParDateEffet(dateEffet).stream()
            .filter(pc -> pc.getMatriculeCommercial().equals(matriculeCommercial))
            .anyMatch(this::estValideeParComptable);

        if (!signatureComptable) {
            throw new RegleMetierException(
                "Chargement bloqué : aucune pré-commande validée par le comptable pour " + matriculeCommercial
                + " à la date d'effet " + dateEffet + ". La signature comptable (validité de la commande) est manquante.");
        }

        boolean signatureSecretaire = versementRepository.listerParCommercial(matriculeCommercial).stream()
            .anyMatch(v -> v.getDate().equals(dateDotation)
                && (v.getStatut() == StatutVersement.VALIDE || v.getStatut() == StatutVersement.JUSTIFIE));

        if (!signatureSecretaire) {
            throw new RegleMetierException(
                "Chargement bloqué : aucun versement validé par la secrétaire pour " + matriculeCommercial
                + " à la date du " + dateDotation + " (cash de la veille non attesté).");
        }
    }

    private boolean estValideeParComptable(PreCommande pc) {
        return pc.getStatut() == StatutPreCommande.VALIDEE_COMPTABLE || pc.getStatut() == StatutPreCommande.LIVREE;
    }
}
