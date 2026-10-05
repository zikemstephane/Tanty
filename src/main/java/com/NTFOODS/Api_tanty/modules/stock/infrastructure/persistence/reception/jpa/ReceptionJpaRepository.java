package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.reception.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReceptionJpaRepository extends JpaRepository<ReceptionJpaEntity, Long> {
    Optional<ReceptionJpaEntity> findByNumero(String numero);
    List<ReceptionJpaEntity> findByStatut(String statut);
    List<ReceptionJpaEntity> findByNumeroBonCommande(String numeroBonCommande);

    @Query("SELECT COUNT(r) FROM ReceptionJpaEntity r WHERE EXTRACT(YEAR FROM r.dateReception) = :annee")
    long compterParAnnee(@Param("annee") int annee);
}
