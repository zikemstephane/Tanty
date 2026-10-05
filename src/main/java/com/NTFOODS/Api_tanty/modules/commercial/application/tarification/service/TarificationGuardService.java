package com.NTFOODS.Api_tanty.modules.commercial.application.tarification.service;

import com.NTFOODS.Api_tanty.modules.commercial.domain.client.aggregate.Client;
import com.NTFOODS.Api_tanty.modules.commercial.domain.client.repository.ClientRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.model.GrilleTarifaire;
import com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.model.PrixExceptionnel;
import com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.repository.GrilleTarifaireRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.repository.PrixExceptionnelRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;

/**
 * Garde applicative — Tarification Verrouillée (Pilier 3).
 *
 * Les champs de saisie du prix sont grisés côté mobile, mais le serveur DOIT
 * appliquer la même règle : un commercial ne peut jamais faire passer un prix
 * différent du prix homologué, sauf s'il présente la référence d'un
 * {@link PrixExceptionnel} explicitement accordé par le DG ou le Directeur
 * Commercial pour ce client, ce produit et ce prix exact.
 *
 * Comportement volontairement non-bloquant si AUCUN prix homologué n'existe
 * encore pour la combinaison produit × segment (grille en cours de
 * constitution) — cohérent avec l'esprit de fluidité du système : on
 * n'immobilise pas les ventes pour un oubli de paramétrage, mais on ne laisse
 * jamais passer un écart par rapport à un prix qui, lui, EST homologué.
 */
@Service
public class TarificationGuardService {

    private final ClientRepository clientRepository;
    private final GrilleTarifaireRepository grilleTarifaireRepository;
    private final PrixExceptionnelRepository prixExceptionnelRepository;

    public TarificationGuardService(ClientRepository clientRepository,
                                     GrilleTarifaireRepository grilleTarifaireRepository,
                                     PrixExceptionnelRepository prixExceptionnelRepository) {
        this.clientRepository = clientRepository;
        this.grilleTarifaireRepository = grilleTarifaireRepository;
        this.prixExceptionnelRepository = prixExceptionnelRepository;
    }

    /**
     * @param referencePrixExceptionnel référence d'un déblocage DG/Directeur Commercial déjà
     *                                   accordé (peut être {@code null} si aucun n'est invoqué)
     * @throws RegleMetierException si le prix soumis diverge du prix homologué sans dérogation valide
     */
    public void validerPrix(String codeClient, String codeProduit, double prixUnitaireSoumis,
                             String referencePrixExceptionnel) {
        Client client = clientRepository.trouverParCode(codeClient)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Client", codeClient));

        GrilleTarifaire grille = grilleTarifaireRepository.trouver(codeProduit, client.getType()).orElse(null);
        if (grille == null || !grille.isActif()) {
            // Aucun prix homologué connu pour ce couple produit/segment : on laisse passer
            // (fluidité) plutôt que de bloquer toute vente pour un paramétrage incomplet.
            return;
        }

        if (Math.abs(grille.getPrixHomologueFCFA() - prixUnitaireSoumis) < 0.01) {
            return; // Prix conforme à la grille homologuée.
        }

        if (referencePrixExceptionnel != null && !referencePrixExceptionnel.isBlank()) {
            PrixExceptionnel deroga = prixExceptionnelRepository.trouverParReference(referencePrixExceptionnel)
                .orElseThrow(() -> new RegleMetierException(
                    "Référence de prix exceptionnel introuvable : " + referencePrixExceptionnel));

            boolean correspond = deroga.getCodeClient().equals(codeClient)
                && deroga.getCodeProduit().equalsIgnoreCase(codeProduit)
                && Math.abs(deroga.getPrixAppliqueFCFA() - prixUnitaireSoumis) < 0.01;

            if (correspond) return;

            throw new RegleMetierException(
                "Le prix exceptionnel " + referencePrixExceptionnel + " ne correspond pas à cette ligne "
                + "(client/produit/montant) — vente refusée.");
        }

        throw new RegleMetierException(
            "Prix non conforme à la grille homologuée pour le produit " + codeProduit + " (segment "
            + client.getType() + ") : attendu " + grille.getPrixHomologueFCFA() + " FCFA, soumis "
            + prixUnitaireSoumis + " FCFA. Un déblocage DG/Directeur Commercial (prix exceptionnel) "
            + "est requis pour vendre à ce tarif.");
    }
}
