package com.NTFOODS.Api_tanty.modules.stock.infrastructure.messaging;

import com.NTFOODS.Api_tanty.modules.stock.application.notification.dto.NotificationMessage;
import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.event.AlerteCreeeEvent;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.event.DotationApprouveeEvent;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.event.DotationSoumiseEvent;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.event.MouvementEnregistreEvent;
import com.NTFOODS.Api_tanty.modules.stock.domain.notification.model.HistoriqueNotification;
import com.NTFOODS.Api_tanty.modules.stock.domain.notification.repository.HistoriqueNotificationRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.event.ReceptionValideeEvent;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.websocket.NotificationWebSocketService;
import com.NTFOODS.Api_tanty.shared.infrastructure.config.RabbitMQConfig;
import com.NTFOODS.Api_tanty.shared.infrastructure.notification.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Relais des événements de domaine Stock vers :
 * - le bus RabbitMQ (exchange tanty.events) pour les autres modules ;
 * - les notifications WebSocket temps réel ;
 * - l'historique des notifications (persistance pour consultation ultérieure).
 *
 * Les méthodes sont déclenchées par les événements applicatifs publiés via
 * {@code EventPublisher} (ApplicationEventPublisher).
 */
@Component
public class StockEventRelay {

    private static final Logger log = LoggerFactory.getLogger(StockEventRelay.class);

    private final RabbitTemplate rabbitTemplate;
    private final NotificationWebSocketService webSocketService;
    private final EmailService emailService;
    private final HistoriqueNotificationRepository historiqueRepository;

    public StockEventRelay(RabbitTemplate rabbitTemplate, NotificationWebSocketService webSocketService,
                           EmailService emailService, HistoriqueNotificationRepository historiqueRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.webSocketService = webSocketService;
        this.emailService = emailService;
        this.historiqueRepository = historiqueRepository;
    }

    @EventListener
    public void surAlerteCreee(AlerteCreeeEvent event) {
        String routingKey = switch (event.getType()) {
            case TAMPON_SOUS_SEUIL -> "stock.tampon.seuilAtteint";
            case CENTRAL_SOUS_SEUIL -> "stock.central.seuilAtteint";
            case BC_RETARD_LIVRAISON -> "stock.bc.retardLivraison";
            default -> "stock.alerte.creee";
        };
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_TANTY, routingKey, event);
        } catch (Exception e) {
            log.warn("[STOCK-EVENT] RabbitMQ indisponible — alerte {} non relayée (local only)", event.getReference());
        }
        webSocketService.diffuserAlerte(NotificationMessage.de(
            event.getType().name(), event.getType().getLibelle(), event.getMessage(),
            event.getDestinataireRole(), event.getType().getActionSuggeree(), event.getReference()));

        historiqueRepository.sauvegarder(new HistoriqueNotification(
            event.getType().name(), event.getType().getLibelle(), event.getMessage(),
            event.getDestinataireRole(), null,
            "IN_APP", event.getType().getPriorite().name(), event.getReference()));

        if (event.getType().getPriorite().requireEmail()) {
            String sujet = "[" + event.getType().getPriorite().getLibelle() + "] " + event.getType().getLibelle();
            emailService.envoyer(event.getDestinataireRole().toLowerCase() + "@tanty-erp.local",
                sujet, event.getMessage() + "\n\nAction suggérée : " + event.getType().getActionSuggeree());
        }
        if (event.getType().getPriorite().requireSMS()) {
            log.warn("[SMS] Alerte CRITIQUE {} : {} — SMS à envérer au destinataire {}",
                event.getReference(), event.getMessage(), event.getDestinataireRole());
        }
    }

    @EventListener
    public void surMouvementEnregistre(MouvementEnregistreEvent event) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_TANTY, "stock.mouvement.enregistre", event);
        } catch (Exception e) {
            log.warn("[STOCK-EVENT] RabbitMQ indisponible — mouvement {} non relayé (local only)", event.getReference());
        }
    }

    @EventListener
    public void surDotationApprouvee(DotationApprouveeEvent event) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_TANTY, "stock.dotation.approuvee", event);
        } catch (Exception e) {
            log.warn("[STOCK-EVENT] RabbitMQ indisponible — dotation {} non relayée (local only)", event.getNumero());
        }
        webSocketService.diffuser(NotificationMessage.de(
            "DOTATION_APPROUVEE", "Dotation approuvée",
            "La dotation " + event.getNumero() + " a été approuvée pour le " + event.getDateEffet(),
            "COMMERCIAL", "Voir le détail de la dotation", event.getNumero()));
        historiqueRepository.sauvegarder(new HistoriqueNotification(
            "DOTATION_APPROUVEE", "Dotation approuvée",
            "La dotation " + event.getNumero() + " a été approuvée pour le " + event.getDateEffet(),
            "COMMERCIAL", event.getMatriculeCommercial(), "IN_APP", "MOYENNE", event.getNumero()));
    }

    @EventListener
    public void surDotationSoumise(DotationSoumiseEvent event) {
        webSocketService.diffuser(NotificationMessage.de(
            "NOUVELLE_DOTATION", "Nouvelle demande de dotation",
            "Dotation " + event.getNumero() + " soumise par " + event.getMatriculeCommercial()
                + " (effet " + event.getDateEffet() + ")",
            "GESTIONNAIRE_STOCK", "Consulter et approuver/modifier", event.getNumero()));
        historiqueRepository.sauvegarder(new HistoriqueNotification(
            "NOUVELLE_DOTATION", "Nouvelle demande de dotation",
            "Dotation " + event.getNumero() + " soumise par " + event.getMatriculeCommercial()
                + " (effet " + event.getDateEffet() + ")",
            "GESTIONNAIRE_STOCK", null, "IN_APP", "MOYENNE", event.getNumero()));
    }

    @EventListener
    public void surReceptionValidee(ReceptionValideeEvent event) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_TANTY, "stock.reception.validee", event);
        } catch (Exception e) {
            log.warn("[STOCK-EVENT] RabbitMQ indisponible — reception {} non relayée (local only)", event.getNumero());
        }
    }
}
