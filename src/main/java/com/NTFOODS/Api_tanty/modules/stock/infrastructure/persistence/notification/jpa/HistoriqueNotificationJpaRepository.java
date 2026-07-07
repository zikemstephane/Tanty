package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.notification.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoriqueNotificationJpaRepository extends JpaRepository<HistoriqueNotificationJpaEntity, Long> {
    List<HistoriqueNotificationJpaEntity> findByMatriculeDestinataireOrderByDateCreationDesc(String matricule);
    List<HistoriqueNotificationJpaEntity> findByDestinataireRoleOrderByDateCreationDesc(String role);
    List<HistoriqueNotificationJpaEntity> findByMatriculeDestinataireAndLuFalseOrderByDateCreationDesc(String matricule);
}
