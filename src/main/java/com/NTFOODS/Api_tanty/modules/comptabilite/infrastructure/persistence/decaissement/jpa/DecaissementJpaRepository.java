package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.decaissement.jpa;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.enums.StatutDecaissement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DecaissementJpaRepository extends JpaRepository<DecaissementJpaEntity, Long> {
    Optional<DecaissementJpaEntity> findByNumeroDecaissement(String numero);
    List<DecaissementJpaEntity> findByStatut(StatutDecaissement statut);
    List<DecaissementJpaEntity> findByDateDepenseBetween(LocalDate debut, LocalDate fin);
}
