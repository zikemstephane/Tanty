package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.stockmobile.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StockMobileJpaRepository extends JpaRepository<StockMobileJpaEntity, Long> {
    Optional<StockMobileJpaEntity> findByMatriculeCommercialAndDateSession(String matricule, LocalDate date);
    Optional<StockMobileJpaEntity> findFirstByMatriculeCommercialOrderByDateSessionDesc(String matricule);
    List<StockMobileJpaEntity> findByDateSession(LocalDate date);
    List<StockMobileJpaEntity> findByMatriculeCommercial(String matricule);
}
