package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.facture.jpa;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.enums.StatutFacture;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.enums.TypeFacture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

public interface FactureJpaRepository extends JpaRepository<FactureJpaEntity, Long> {
    Optional<FactureJpaEntity> findByNumeroFacture(String numeroFacture);
    List<FactureJpaEntity> findByCodeClient(String codeClient);
    List<FactureJpaEntity> findByDateEmissionBetween(LocalDate debut, LocalDate fin);
    List<FactureJpaEntity> findByStatut(StatutFacture statut);
    List<FactureJpaEntity> findByTypeFacture(TypeFacture typeFacture);
    long countByTypeFactureAndDateEmission(TypeFacture typeFacture, LocalDate date);

    // ── Méthodes paginées ──────────────────────────────────────────
    Page<FactureJpaEntity> findByDateEmissionBetweenOrderByDateEmissionDesc(LocalDate debut, LocalDate fin, Pageable pageable);
    Page<FactureJpaEntity> findByStatutOrderByDateEmissionDesc(StatutFacture statut, Pageable pageable);
    Page<FactureJpaEntity> findByCodeClientOrderByDateEmissionDesc(String codeClient, Pageable pageable);
}
