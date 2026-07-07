package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.broyage.jpa;

import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.TypePoudre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SessionBroyageJpaRepository extends JpaRepository<SessionBroyageJpaEntity, Long> {

    Optional<SessionBroyageJpaEntity> findByReferencePPHAndDateAndTypePoudre(
        String referencePPH, LocalDate date, TypePoudre typePoudre);

    boolean existsByReferencePPHAndDateAndTypePoudre(
        String referencePPH, LocalDate date, TypePoudre typePoudre);

    List<SessionBroyageJpaEntity> findByReferencePPHOrderByDateDesc(String referencePPH);

    List<SessionBroyageJpaEntity> findByDateBetweenOrderByDateDesc(LocalDate debut, LocalDate fin);

    @Query("SELECT DISTINCT s FROM SessionBroyageJpaEntity s " +
           "JOIN s.realisations r " +
           "WHERE r.machinisteMatricule = :matricule AND s.date BETWEEN :debut AND :fin " +
           "ORDER BY s.date DESC")
    List<SessionBroyageJpaEntity> findByMachinisteEtPeriode(
        @Param("matricule") String matricule,
        @Param("debut") LocalDate debut,
        @Param("fin") LocalDate fin);
}
