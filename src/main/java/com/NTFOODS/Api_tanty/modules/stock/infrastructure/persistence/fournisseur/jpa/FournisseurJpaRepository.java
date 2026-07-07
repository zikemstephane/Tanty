package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.fournisseur.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FournisseurJpaRepository extends JpaRepository<FournisseurJpaEntity, Long> {
    Optional<FournisseurJpaEntity> findByCode(String code);
    boolean existsByCode(String code);
    List<FournisseurJpaEntity> findByRaisonSocialeContainingIgnoreCase(String partiel);
}
