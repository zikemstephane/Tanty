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

    /**
     * Recherche filtrée du catalogue. Chaque critère est facultatif : passer
     * {@code null} revient à ne pas filtrer sur ce champ.
     *
     * ── Pourquoi les CAST ──────────────────────────────────────────────────
     * La requête s'écrivait {@code LOWER(p.marque) = LOWER(:marque)} sans
     * conversion. Quand le critère est nul, Hibernate ne peut déduire son type
     * d'aucun contexte et le transmet en binaire ; PostgreSQL reçoit alors
     * {@code lower(bytea)}, fonction qui n'existe pas, et rejette la requête.
     *
     * L'effet était brutal et silencieux à la lecture du code : tout appel
     * portant **au moins un** critère — {@code ?actif=true} en particulier,
     * seule forme qu'utilise le terminal mobile — retournait 500. Le catalogue
     * ne se chargeait donc jamais sur le téléphone du commercial, sur tous ses
     * écrans à la fois, tandis que l'appel sans aucun paramètre continuait de
     * fonctionner : le défaut se cachait derrière le cas le plus testé.
     *
     * Le CAST fixe le type de chaque occurrence du paramètre, y compris celle
     * imbriquée dans {@code LOWER(...)} — c'est elle qui échouait.
     */
    @Query("""
        SELECT DISTINCT p FROM ProduitJpaEntity p
        WHERE (CAST(:marque AS string) IS NULL
               OR LOWER(p.marque) = LOWER(CAST(:marque AS string)))
          AND (CAST(:gamme AS string) IS NULL
               OR LOWER(p.gamme) = LOWER(CAST(:gamme AS string)))
          AND (CAST(:variete AS string) IS NULL
               OR LOWER(p.variete) = LOWER(CAST(:variete AS string)))
          AND (CAST(:actif AS boolean) IS NULL
               OR p.actif = CAST(:actif AS boolean))
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
