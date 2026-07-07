package com.NTFOODS.Api_tanty.shared.infrastructure.audit;

import com.NTFOODS.Api_tanty.shared.kernel.audit.TypeActionAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface JournalAuditJpaRepository extends JpaRepository<JournalAuditJpaEntity, Long> {

    List<JournalAuditJpaEntity> findByMatriculeOrderByHorodatageDesc(String matricule);

    List<JournalAuditJpaEntity> findByHorodatageBetweenOrderByHorodatageDesc(LocalDateTime debut, LocalDateTime fin);

    List<JournalAuditJpaEntity> findByTypeActionOrderByHorodatageDesc(TypeActionAudit typeAction);

    List<JournalAuditJpaEntity> findByModuleOrderByHorodatageDesc(String module);

    List<JournalAuditJpaEntity> findBySuccesFalseOrderByHorodatageDesc();

    // ── Méthodes paginées ──────────────────────────────────────────
    Page<JournalAuditJpaEntity> findByMatriculeOrderByHorodatageDesc(String matricule, Pageable pageable);
    Page<JournalAuditJpaEntity> findByHorodatageBetweenOrderByHorodatageDesc(LocalDateTime debut, LocalDateTime fin, Pageable pageable);
    Page<JournalAuditJpaEntity> findByTypeActionOrderByHorodatageDesc(TypeActionAudit typeAction, Pageable pageable);
    Page<JournalAuditJpaEntity> findByModuleOrderByHorodatageDesc(String module, Pageable pageable);
    Page<JournalAuditJpaEntity> findBySuccesFalseOrderByHorodatageDesc(Pageable pageable);

    /**
     * Détection d'activités suspectes : compte, par matricule, les actions
     * d'annulation ou de modification répétée sur une ressource donnée dans
     * une fenêtre glissante. Utilisé pour repérer les tentatives de jonglage
     * financier (Pilier 3 — Surveillance et Sécurité Anti-Forçage).
     */
    @Query("""
        SELECT j.matricule as matricule, j.ressource as ressource, COUNT(j) as occurrences
        FROM JournalAuditJpaEntity j
        WHERE j.typeAction IN (com.NTFOODS.Api_tanty.shared.kernel.audit.TypeActionAudit.ANNULATION,
                                com.NTFOODS.Api_tanty.shared.kernel.audit.TypeActionAudit.SUPPRESSION_TENTATIVE,
                                com.NTFOODS.Api_tanty.shared.kernel.audit.TypeActionAudit.MODIFICATION)
          AND j.horodatage >= :depuis
        GROUP BY j.matricule, j.ressource
        HAVING COUNT(j) >= :seuil
        ORDER BY COUNT(j) DESC
        """)
    List<ActiviteSuspecteProjection> detecterActivitesSuspectes(@Param("depuis") LocalDateTime depuis,
                                                                 @Param("seuil") long seuil);

    interface ActiviteSuspecteProjection {
        String getMatricule();
        String getRessource();
        Long getOccurrences();
    }
}
