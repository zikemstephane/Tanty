package com.NTFOODS.Api_tanty.modules.stock.domain.notification.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.notification.model.PreferencesNotification;

import java.util.Optional;

public interface PreferencesNotificationRepository {
    PreferencesNotification sauvegarder(PreferencesNotification preferences);
    Optional<PreferencesNotification> trouverParMatricule(String matricule);
}
