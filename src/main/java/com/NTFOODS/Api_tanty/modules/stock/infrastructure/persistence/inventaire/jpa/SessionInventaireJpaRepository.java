package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.inventaire.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SessionInventaireJpaRepository extends JpaRepository<SessionInventaireJpaEntity, Long> {
    Optional<SessionInventaireJpaEntity> findByNumero(String numero);
    List<SessionInventaireJpaEntity> findByStatut(String statut);

    @Query("SELECT COUNT(s) FROM SessionInventaireJpaEntity s WHERE EXTRACT(YEAR FROM s.dateInventaire) = :annee")
    long compterParAnnee(@Param("annee") int annee);
}
