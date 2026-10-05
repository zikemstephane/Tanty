package com.NTFOODS.Api_tanty.modules.commercial.application.tarification.service;

import com.NTFOODS.Api_tanty.modules.commercial.domain.client.aggregate.Client;
import com.NTFOODS.Api_tanty.modules.commercial.domain.client.repository.ClientRepository;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.aggregate.Promotion;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.repository.PromotionRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;

/**
 * Garde applicative — Remise commerciale.
 *
 * <p>Prolonge le principe de la tarification verrouillée : un commercial ne peut
 * pas davantage contourner le prix homologué par une remise libre qu'en modifiant
 * le prix lui-même. La remise n'est donc jamais une saisie, mais une conséquence
 * des promotions en vigueur.</p>
 *
 * <p>L'autorité revient au serveur : le montant retenu est celui que produit la
 * promotion applicable au couple produit × segment client à la date de la vente.
 * Une remise transmise par le client n'est acceptée que si elle coïncide avec ce
 * montant ; sinon la vente est refusée, jamais corrigée en silence — le
 * commercial doit savoir que le montant annoncé au client ne sera pas celui
 * enregistré.</p>
 *
 * <p>La remise est exprimée en <strong>pourcentage</strong> de 0 à 100 et porte
 * sur la ligne entière, cohérente avec {@code LigneVente.montantHT()}. Les
 * promotions, elles, continuent de produire un montant en FCFA — qu'elles soient
 * définies en taux ou en valeur fixe : c'est ce service qui le rapporte au brut
 * de la ligne. La conversion est faite ici, en un seul endroit, plutôt que dans
 * chaque appelant.</p>
 */
@Service
public class RemiseGuardService {

    /** Tolérance d'arrondi, en points de pourcentage. */
    private static final double TOLERANCE_POINTS = 0.01;

    private final ClientRepository clientRepository;
    private final PromotionRepository promotionRepository;

    public RemiseGuardService(ClientRepository clientRepository,
                               PromotionRepository promotionRepository) {
        this.clientRepository = clientRepository;
        this.promotionRepository = promotionRepository;
    }

    /**
     * Détermine la remise applicable à une ligne, et refuse celle qui aurait été
     * transmise si elle ne lui correspond pas.
     *
     * @param codeClient     client de la vente, dont le segment conditionne les promotions
     * @param codeProduit    produit de la ligne
     * @param montantBrut    quantité × prix unitaire, base de calcul de la remise
     * @param remiseSoumise  remise transmise par le client, en pourcentage
     * @return la remise à enregistrer, en pourcentage
     * @throws RegleMetierException si la remise soumise diverge de la remise applicable
     */
    public double resoudreRemise(String codeClient, String codeProduit,
                                  double montantBrut, double remiseSoumise) {
        Client client = clientRepository.trouverParCode(codeClient)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Client", codeClient));

        Optional<Promotion> promotion = promotionApplicable(codeProduit, client, montantBrut);
        double remiseApplicableFCFA = promotion
            .map(p -> Math.min(p.calculerRemise(montantBrut), montantBrut))
            .orElse(0.0);
        double remiseApplicable = enPourcentage(remiseApplicableFCFA, montantBrut);

        if (Math.abs(remiseApplicable - remiseSoumise) < TOLERANCE_POINTS) {
            return remiseApplicable;
        }

        if (remiseApplicable == 0.0) {
            throw new RegleMetierException(
                "Aucune promotion en vigueur ne justifie une remise sur le produit " + codeProduit
                + " pour le segment " + client.getType() + " : remise de " + remiseSoumise
                + " % refusée. Une promotion doit être créée par le DG ou la Chargée RP.");
        }

        throw new RegleMetierException(
            "La remise soumise (" + remiseSoumise + " %) ne correspond pas à la promotion "
            + promotion.map(Promotion::getCode).orElse("—") + " applicable au produit " + codeProduit
            + ", qui ouvre droit à " + remiseApplicable + " % (" + remiseApplicableFCFA + " FCFA).");
    }

    /**
     * Rapporte un montant de remise au brut de la ligne.
     *
     * <p>Un brut nul ne donne pas droit à une remise : diviser par zéro
     * produirait {@code NaN} ou l'infini, qui traverseraient ensuite tous les
     * calculs sans jamais déclencher d'erreur. Zéro est la seule réponse
     * honnête — il n'y a rien sur quoi remiser.</p>
     */
    private double enPourcentage(double remiseFCFA, double montantBrut) {
        if (montantBrut <= 0) return 0.0;
        return remiseFCFA / montantBrut * 100.0;
    }

    /**
     * Promotion en vigueur pour ce produit et ce segment.
     *
     * <p>Lorsque plusieurs promotions se chevauchent, la plus avantageuse pour le
     * client est retenue — le commercial n'a ainsi aucun arbitrage à opérer, et
     * deux ventes identiques produisent toujours la même remise.</p>
     *
     * <p>La comparaison porte sur la remise <strong>réellement produite</strong> pour
     * ce montant, et non sur la valeur brute du paramètre. Comparer ces valeurs
     * directement revenait à opposer un pourcentage à un montant en FCFA : sur une
     * ligne à 20 000 FCFA, une promotion de 10 % (2 000 FCFA) perdait face à une
     * remise fixe de 500 FCFA, au détriment du client.</p>
     */
    private Optional<Promotion> promotionApplicable(String codeProduit, Client client, double montantBrut) {
        return promotionRepository.listerParProduit(codeProduit).stream()
            .filter(Promotion::estValide)
            .filter(p -> p.getSegmentCible() == null || p.getSegmentCible() == client.getType())
            .max(Comparator.comparingDouble(p -> Math.min(p.calculerRemise(montantBrut), montantBrut)));
    }
}
