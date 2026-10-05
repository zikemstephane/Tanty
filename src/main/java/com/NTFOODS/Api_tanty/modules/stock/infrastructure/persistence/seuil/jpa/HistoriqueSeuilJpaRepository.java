package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.seuil.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoriqueSeuilJpaRepository extends JpaRepository<HistoriqueSeuilJpaEntity, Long> {
    List<HistoriqueSeuilJpaEntity> findByCodeProduitOrderByDateModificationDesc(String codeProduit);
    List<HistoriqueSeuilJpaEntity> findAllByOrderByDateModificationDesc();
}
