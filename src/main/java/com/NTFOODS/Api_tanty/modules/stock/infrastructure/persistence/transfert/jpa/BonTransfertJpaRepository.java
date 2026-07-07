package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.transfert.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BonTransfertJpaRepository extends JpaRepository<BonTransfertJpaEntity, Long> {
    Optional<BonTransfertJpaEntity> findByNumero(String numero);
    List<BonTransfertJpaEntity> findByStatut(String statut);

    @Query("SELECT COUNT(t) FROM BonTransfertJpaEntity t WHERE EXTRACT(YEAR FROM t.dateCreation) = :annee")
    long compterParAnnee(@Param("annee") int annee);
}
