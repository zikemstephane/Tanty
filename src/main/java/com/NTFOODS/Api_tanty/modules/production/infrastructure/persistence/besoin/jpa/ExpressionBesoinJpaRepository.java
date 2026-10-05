package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.besoin.jpa;

import com.NTFOODS.Api_tanty.modules.production.domain.besoin.enums.StatutBesoin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpressionBesoinJpaRepository extends JpaRepository<ExpressionBesoinJpaEntity, Long> {
    Optional<ExpressionBesoinJpaEntity> findByNumero(String numero);
    List<ExpressionBesoinJpaEntity> findByStatut(StatutBesoin statut);
    boolean existsByNumero(String numero);
}
