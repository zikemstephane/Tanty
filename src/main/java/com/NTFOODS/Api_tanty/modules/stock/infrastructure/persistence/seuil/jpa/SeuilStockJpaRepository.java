package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.seuil.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeuilStockJpaRepository extends JpaRepository<SeuilStockJpaEntity, Long> {
    Optional<SeuilStockJpaEntity> findByCodeProduitAndTypeEntrepot(String codeProduit, String typeEntrepot);
    List<SeuilStockJpaEntity> findByTypeEntrepot(String typeEntrepot);
}
