package com.NTFOODS.Api_tanty.modules.stock.application.notification.dto;

import java.time.LocalDateTime;

/**
 * Message de notification temps réel poussé via WebSocket.
 * {@code actionSuggeree} rend la notification actionnable côté client.
 */
public record NotificationMessage(
    String type,
    String titre,
    String message,
    String destinataireRole,
    String actionSuggeree,
    String referenceLiee,
    LocalDateTime horodatage
) {
    public static NotificationMessage de(String type, String titre, String message, String destinataireRole,
                                         String actionSuggeree, String referenceLiee) {
        return new NotificationMessage(type, titre, message, destinataireRole, actionSuggeree, referenceLiee,
            LocalDateTime.now());
    }
}
