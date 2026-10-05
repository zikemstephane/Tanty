package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.lot.jpa;

import com.NTFOODS.Api_tanty.modules.production.domain.lot.enums.StatutLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LotJpaRepository extends JpaRepository<LotProduitFiniJpaEntity, Long> {
    Optional<LotProduitFiniJpaEntity> findByNumeroLot(String numeroLot);
    List<LotProduitFiniJpaEntity> findByStatut(StatutLot statut);
    List<LotProduitFiniJpaEntity> findByReferencePPH(String referencePPH);
    List<LotProduitFiniJpaEntity> findByProductSku(String productSku);
    List<LotProduitFiniJpaEntity> findByDateProductionBetween(LocalDate debut, LocalDate fin);
    long countByDateProduction(LocalDate date);
}
