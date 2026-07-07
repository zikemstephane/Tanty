package com.NTFOODS.Api_tanty.modules.stock.infrastructure.websocket;

import com.NTFOODS.Api_tanty.modules.stock.application.notification.dto.NotificationMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Service d'envoi de notifications temps réel via STOMP/WebSocket.
 *
 * Canaux :
 * - /topic/notifications        → flux global
 * - /topic/notifications/{role} → flux par rôle destinataire
 * - /topic/alertes              → alertes stock
 */
@Service
public class NotificationWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationWebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void diffuser(NotificationMessage message) {
        messagingTemplate.convertAndSend("/topic/notifications", message);
        if (message.destinataireRole() != null) {
            messagingTemplate.convertAndSend("/topic/notifications/" + message.destinataireRole(), message);
        }
    }

    public void diffuserAlerte(NotificationMessage message) {
        messagingTemplate.convertAndSend("/topic/alertes", message);
        diffuser(message);
    }
}
