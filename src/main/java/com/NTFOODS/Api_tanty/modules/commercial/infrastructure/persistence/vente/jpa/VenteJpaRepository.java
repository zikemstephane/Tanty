package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa;

import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.StatutVente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface VenteJpaRepository extends JpaRepository<VenteJpaEntity, Long> {
    Optional<VenteJpaEntity> findByNumeroVente(String numeroVente);
    List<VenteJpaEntity> findByMatriculeCommercial(String matricule);
    List<VenteJpaEntity> findByCodeClient(String codeClient);
    List<VenteJpaEntity> findByMatriculeCommercialAndDate(String matricule, LocalDate date);
    List<VenteJpaEntity> findByStatut(StatutVente statut);
    List<VenteJpaEntity> findByDateBetween(LocalDate debut, LocalDate fin);
    List<VenteJpaEntity> findByMatriculeCommercialAndDateBetween(String matricule, LocalDate debut, LocalDate fin);

    // ── Méthodes paginées ──────────────────────────────────────────
    Page<VenteJpaEntity> findByDateBetweenOrderByDateDesc(LocalDate debut, LocalDate fin, Pageable pageable);
    Page<VenteJpaEntity> findByMatriculeCommercialOrderByDateDesc(String matricule, Pageable pageable);
}
