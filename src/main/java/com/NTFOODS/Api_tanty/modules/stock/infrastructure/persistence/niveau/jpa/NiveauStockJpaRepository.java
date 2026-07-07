package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.niveau.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NiveauStockJpaRepository extends JpaRepository<NiveauStockJpaEntity, Long> {
    Optional<NiveauStockJpaEntity> findByCodeProduitAndCodeEntrepot(String codeProduit, String codeEntrepot);
    List<NiveauStockJpaEntity> findByCodeEntrepot(String codeEntrepot);
    List<NiveauStockJpaEntity> findByCodeProduit(String codeProduit);
    List<NiveauStockJpaEntity> findByTypeEntrepot(String typeEntrepot);
}
