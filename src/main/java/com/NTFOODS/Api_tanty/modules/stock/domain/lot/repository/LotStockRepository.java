package com.NTFOODS.Api_tanty.modules.stock.domain.lot.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.lot.model.LotStock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/** Port — persistance des lots de stock. */
public interface LotStockRepository {

    LotStock sauvegarder(LotStock lot);

    Optional<LotStock> trouverParId(Long id);

    Optional<LotStock> trouverParNumero(String numeroLot);

    boolean existeParNumero(String numeroLot);

    List<LotStock> trouverTous();

    List<LotStock> trouverParProduit(String codeProduit);

    List<LotStock> trouverParSku(String sku);

    List<LotStock> trouverParEntrepot(String codeEntrepot);

    /**
     * Lots consommables d'un produit dans un entrepôt, ordonnés pour une sortie
     * au plus proche de la péremption : date de péremption croissante, les lots
     * sans date en dernier, puis date de création croissante.
     */
    List<LotStock> trouverConsommables(String sku, String codeEntrepot);

    /** Lots non périmés dont la péremption tombe au plus tard à {@code limite}. */
    List<LotStock> trouverPerimantAvant(LocalDate limite);

    /** Lots dont la date de péremption est atteinte ou dépassée. */
    List<LotStock> trouverPerimes();
}
