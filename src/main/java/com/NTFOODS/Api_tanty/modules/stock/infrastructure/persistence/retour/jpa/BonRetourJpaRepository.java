package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.retour.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BonRetourJpaRepository extends JpaRepository<BonRetourJpaEntity, Long> {
    Optional<BonRetourJpaEntity> findByNumero(String numero);
    List<BonRetourJpaEntity> findByMatriculeCommercial(String matriculeCommercial);

    @Query("SELECT COUNT(r) FROM BonRetourJpaEntity r WHERE EXTRACT(YEAR FROM r.dateRetour) = :annee")
    long compterParAnnee(@Param("annee") int annee);
}
