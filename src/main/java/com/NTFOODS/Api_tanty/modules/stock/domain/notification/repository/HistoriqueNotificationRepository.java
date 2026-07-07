package com.NTFOODS.Api_tanty.modules.stock.domain.notification.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.notification.model.HistoriqueNotification;

import java.util.List;
import java.util.Optional;

public interface HistoriqueNotificationRepository {
    HistoriqueNotification sauvegarder(HistoriqueNotification notification);
    Optional<HistoriqueNotification> trouverParId(Long id);
    List<HistoriqueNotification> trouverParMatricule(String matricule);
    List<HistoriqueNotification> trouverParRole(String role);
    List<HistoriqueNotification> trouverNonLusParMatricule(String matricule);
    List<HistoriqueNotification> trouverTous();
    void supprimerParId(Long id);
}
