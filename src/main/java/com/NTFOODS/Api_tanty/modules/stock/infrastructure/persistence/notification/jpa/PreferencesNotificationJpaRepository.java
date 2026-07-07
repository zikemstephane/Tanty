package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.notification.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PreferencesNotificationJpaRepository extends JpaRepository<PreferencesNotificationJpaEntity, Long> {
    Optional<PreferencesNotificationJpaEntity> findByMatriculeUtilisateur(String matricule);
}
