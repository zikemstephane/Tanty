package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.catalogue.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository Spring Data — Produits.
 */
public interface ProduitJpaRepository extends JpaRepository<ProduitJpaEntity, Long> {

    Optional<ProduitJpaEntity> findByCode(String code);

    boolean existsByCode(String code);

    List<ProduitJpaEntity> findByCategorie(String categorie);

    List<ProduitJpaEntity> findByCategorieMetier(String categorieMetier);

    @Query("""
        SELECT DISTINCT p FROM ProduitJpaEntity p
        WHERE (:marque IS NULL OR LOWER(p.marque) = LOWER(:marque))
          AND (:gamme IS NULL OR LOWER(p.gamme) = LOWER(:gamme))
          AND (:variete IS NULL OR LOWER(p.variete) = LOWER(:variete))
          AND (:actif IS NULL OR p.actif = :actif)
        """)
    List<ProduitJpaEntity> rechercher(@Param("marque") String marque,
                                      @Param("gamme") String gamme,
                                      @Param("variete") String variete,
                                      @Param("actif") Boolean actif);

    @Query("""
        SELECT p FROM ProduitJpaEntity p JOIN p.conditionnements c
        WHERE LOWER(c.sku) = LOWER(:sku)
        """)
    Optional<ProduitJpaEntity> findByConditionnementSku(@Param("sku") String sku);

    void deleteByCode(String code);
}
