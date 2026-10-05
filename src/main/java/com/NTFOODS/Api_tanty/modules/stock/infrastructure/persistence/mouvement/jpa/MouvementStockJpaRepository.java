package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.mouvement.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MouvementStockJpaRepository extends JpaRepository<MouvementStockJpaEntity, Long> {
    List<MouvementStockJpaEntity> findByCodeProduitOrderByHorodatageDesc(String codeProduit);
    List<MouvementStockJpaEntity> findByCodeEntrepotSourceOrCodeEntrepotCibleOrderByHorodatageDesc(String source, String cible);
    List<MouvementStockJpaEntity> findByTypeOrderByHorodatageDesc(String type);
    List<MouvementStockJpaEntity> findByHorodatageBetweenOrderByHorodatageDesc(LocalDateTime debut, LocalDateTime fin);
    List<MouvementStockJpaEntity> findByReferenceDocument(String referenceDocument);

    // ── Méthodes paginées ──────────────────────────────────────────
    Page<MouvementStockJpaEntity> findByCodeProduitOrderByHorodatageDesc(String codeProduit, Pageable pageable);
    Page<MouvementStockJpaEntity> findByCodeEntrepotSourceOrCodeEntrepotCibleOrderByHorodatageDesc(String source, String cible, Pageable pageable);
    Page<MouvementStockJpaEntity> findByTypeOrderByHorodatageDesc(String type, Pageable pageable);
    Page<MouvementStockJpaEntity> findByHorodatageBetweenOrderByHorodatageDesc(LocalDateTime debut, LocalDateTime fin, Pageable pageable);
    Page<MouvementStockJpaEntity> findAllByOrderByHorodatageDesc(Pageable pageable);
}
