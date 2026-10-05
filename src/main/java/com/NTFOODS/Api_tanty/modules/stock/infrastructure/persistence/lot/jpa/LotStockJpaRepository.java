package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.lot.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LotStockJpaRepository extends JpaRepository<LotStockJpaEntity, Long> {

    Optional<LotStockJpaEntity> findByNumeroLot(String numeroLot);

    boolean existsByNumeroLot(String numeroLot);

    List<LotStockJpaEntity> findByCodeProduitOrderByDatePeremptionAsc(String codeProduit);

    List<LotStockJpaEntity> findBySkuOrderByDatePeremptionAsc(String sku);

    List<LotStockJpaEntity> findByCodeEntrepotOrderByDatePeremptionAsc(String codeEntrepot);

    /**
     * Lots consommables d'un SKU dans un entrepôt, dans l'ordre de sortie :
     * ce qui périme le plus tôt part le premier, les lots sans date de
     * péremption viennent après (NULLS LAST), et à égalité c'est le plus
     * ancien qui sort.
     */
    @Query("""
        SELECT l FROM LotStockJpaEntity l
        WHERE l.sku = :sku
          AND l.codeEntrepot = :codeEntrepot
          AND l.statut IN ('DISPONIBLE', 'RESERVE')
          AND l.quantiteRestante > 0
        ORDER BY CASE WHEN l.datePeremption IS NULL THEN 1 ELSE 0 END,
                 l.datePeremption ASC,
                 l.dateCreation ASC
        """)
    List<LotStockJpaEntity> findConsommables(@Param("sku") String sku,
                                             @Param("codeEntrepot") String codeEntrepot);

    /** Lots pas encore périmés dont la date tombe au plus tard à {@code limite}. */
    @Query("""
        SELECT l FROM LotStockJpaEntity l
        WHERE l.datePeremption IS NOT NULL
          AND l.datePeremption <= :limite
          AND l.datePeremption > :aujourdhui
          AND l.statut NOT IN ('EPUISE', 'RETIRE')
        ORDER BY l.datePeremption ASC
        """)
    List<LotStockJpaEntity> findPerimantAvant(@Param("limite") LocalDate limite,
                                              @Param("aujourdhui") LocalDate aujourdhui);

    /**
     * Lots dont la date de péremption est atteinte ou dépassée. La comparaison
     * est {@code <=} : le jour de la date, le lot est déjà hors délai.
     */
    @Query("""
        SELECT l FROM LotStockJpaEntity l
        WHERE l.datePeremption IS NOT NULL
          AND l.datePeremption <= :aujourdhui
          AND l.statut <> 'RETIRE'
        ORDER BY l.datePeremption ASC
        """)
    List<LotStockJpaEntity> findPerimes(@Param("aujourdhui") LocalDate aujourdhui);
}
