package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.alerte.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlerteJpaRepository extends JpaRepository<AlerteJpaEntity, Long> {
    Optional<AlerteJpaEntity> findByReference(String reference);
    List<AlerteJpaEntity> findByStatutOrderByDateCreationDesc(String statut);
    List<AlerteJpaEntity> findByDestinataireRoleOrderByDateCreationDesc(String destinataireRole);
    boolean existsByTypeAndCodeProduitAndCodeEntrepotAndStatutIn(
        String type, String codeProduit, String codeEntrepot, List<String> statuts);
}
