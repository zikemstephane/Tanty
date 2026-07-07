package com.NTFOODS.Api_tanty.modules.stock.application.dotation.service;

import com.NTFOODS.Api_tanty.modules.stock.domain.stockmobile.model.StockMobile;
import com.NTFOODS.Api_tanty.modules.stock.domain.stockmobile.repository.StockMobileRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;

/**
 * Service applicatif — Vérification de surstock mobile.
 *
 * On ne peut pas donner un même produit à un commercial si la quantité
 * dans son stock mobile est déjà importante. Le seuil de surstock est
 * configurable par produit.
 */
@Service
public class VerificationSurstockService {

    private final StockMobileRepository stockMobileRepository;

    public VerificationSurstockService(StockMobileRepository stockMobileRepository) {
        this.stockMobileRepository = stockMobileRepository;
    }

    /**
     * Vérifie qu'une dotation ne crée pas de surstock chez le commercial.
     *
     * @param matriculeCommercial commercial concerné
     * @param codeProduit         code du produit
     * @param quantiteDemandee    quantité demandée dans la dotation
     * @param seuilSurstock       seuil max acceptable pour ce produit
     * @throws RegleMetierException si le stock mobile + la dotation dépasse le seuil
     */
    public void verifierPasSurstock(String matriculeCommercial, String codeProduit,
                                     long quantiteDemandee, long seuilSurstock) {
        StockMobile stock = stockMobileRepository.trouverDernierParCommercial(matriculeCommercial).orElse(null);
        if (stock == null) return;

        long disponible = stock.quantiteDisponible(codeProduit);
        long totalApresDotation = disponible + quantiteDemandee;

        if (totalApresDotation > seuilSurstock) {
            throw new RegleMetierException(
                "Surstock détecté pour " + codeProduit + " : stock mobile actuel = " + disponible
                + ", dotation demandée = " + quantiteDemandee
                + ", total = " + totalApresDotation + " > seuil autorisé = " + seuilSurstock
                + ". Impossible de donner plus de ce produit au commercial " + matriculeCommercial);
        }
    }
}
