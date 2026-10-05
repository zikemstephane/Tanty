package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.precommande.jpa;

import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.enums.StatutPreCommande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

public interface PreCommandeJpaRepository extends JpaRepository<PreCommandeJpaEntity, Long> {
    Optional<PreCommandeJpaEntity> findByNumeroPreCommande(String numero);
    List<PreCommandeJpaEntity> findByMatriculeCommercial(String matricule);
    List<PreCommandeJpaEntity> findByDateEffet(LocalDate dateEffet);
    List<PreCommandeJpaEntity> findByStatut(StatutPreCommande statut);
    long countByDateSoumissionAndMatriculeCommercial(LocalDate date, String matricule);

    // ── Méthodes paginées ──────────────────────────────────────────
    Page<PreCommandeJpaEntity> findByStatutOrderByDateSoumissionDesc(StatutPreCommande statut, Pageable pageable);
    Page<PreCommandeJpaEntity> findByDateEffetOrderByDateSoumissionDesc(LocalDate dateEffet, Pageable pageable);
}
