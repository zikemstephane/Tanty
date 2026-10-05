package com.NTFOODS.Api_tanty.shared.infrastructure.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service applicatif — canal Email des notifications.
 *
 * Complète le canal WebSocket déjà en place dans le module Stock
 * (NotificationWebSocketService) et le canal SMS (OtpService / SmsGatewayPort).
 * Toute erreur d'envoi est journalisée mais n'interrompt jamais le flux métier
 * appelant (principe de fluidité : on ne bloque jamais une opération pour une
 * notification, on l'enregistre et on continue).
 */
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final String expediteur;

    public EmailService(JavaMailSender mailSender, @Value("${app.mail.expediteur:no-reply@tanty-erp.local}") String expediteur) {
        this.mailSender = mailSender;
        this.expediteur = expediteur;
    }

    public void envoyer(String destinataire, String sujet, String corps) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(expediteur);
            message.setTo(destinataire);
            message.setSubject(sujet);
            message.setText(corps);
            mailSender.send(message);
        } catch (Exception e) {
            log.warn("Échec d'envoi d'email à {} (sujet: {}) : {}", destinataire, sujet, e.getMessage());
        }
    }
}
