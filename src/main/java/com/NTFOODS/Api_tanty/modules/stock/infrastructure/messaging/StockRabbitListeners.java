package com.NTFOODS.Api_tanty.modules.stock.infrastructure.messaging;

import com.NTFOODS.Api_tanty.modules.stock.application.notification.dto.NotificationMessage;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.websocket.NotificationWebSocketService;
import com.NTFOODS.Api_tanty.shared.infrastructure.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Consommateurs RabbitMQ du module Stock — réagit aux événements des autres modules.
 *
 * - production.lot.declare → notifie le gestionnaire de lancer la réception physique.
 * - commercial.precommande.validee → information de préparation (anticipation tampon).
 */
@Component
public class StockRabbitListeners {

    private static final Logger log = LoggerFactory.getLogger(StockRabbitListeners.class);

    private final NotificationWebSocketService webSocketService;

    public StockRabbitListeners(NotificationWebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_LOT_DECLARE)
    public void surLotProductionDeclare(Map<String, Object> message) {
        Object numeroLot = message.getOrDefault("numeroLot", message.get("reference"));
        log.info("Lot de production déclaré reçu : {}", numeroLot);
        webSocketService.diffuser(NotificationMessage.de(
            "LOT_PRODUCTION_DECLARE", "Lot de production déclaré",
            "Le lot " + numeroLot + " est prêt à être réceptionné en stock central",
            "GESTIONNAIRE_STOCK", "Lancer la réception physique",
            numeroLot != null ? numeroLot.toString() : null));
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PRECOMMANDE_VALIDEE)
    public void surPreCommandeValidee(Map<String, Object> message) {
        Object reference = message.getOrDefault("numero", message.get("reference"));
        log.info("Pré-commande validée reçue : {}", reference);
        webSocketService.diffuser(NotificationMessage.de(
            "PRECOMMANDE_VALIDEE", "Pré-commande validée",
            "Pré-commande " + reference + " — anticiper la disponibilité au tampon",
            "GESTIONNAIRE_STOCK", "Vérifier le stock tampon",
            reference != null ? reference.toString() : null));
    }
}
