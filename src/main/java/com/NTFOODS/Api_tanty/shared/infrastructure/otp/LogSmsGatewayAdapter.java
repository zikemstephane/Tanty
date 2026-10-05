package com.NTFOODS.Api_tanty.shared.infrastructure.otp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Adaptateur SMS par défaut : journalise le message au lieu de l'envoyer.
 *
 * Actif par défaut (app.sms.provider=log). Pour brancher un vrai opérateur
 * (Orange CM, MTN CM, Twilio...), implémenter {@link SmsGatewayPort} dans un
 * nouveau bean, l'annoter @Primary, et régler app.sms.provider en conséquence.
 * Ce mécanisme permet au backend de tourner immédiatement en local, sans
 * dépendre d'un compte SMS payant, tout en gardant le code OTP pleinement
 * fonctionnel (le code est visible dans les logs applicatifs).
 */
@Component
@ConditionalOnProperty(name = "app.sms.provider", havingValue = "log", matchIfMissing = true)
public class LogSmsGatewayAdapter implements SmsGatewayPort {

    private static final Logger log = LoggerFactory.getLogger(LogSmsGatewayAdapter.class);

    @Value("${app.sms.provider:log}")
    private String provider;

    @Override
    public void envoyer(String destinataire, String message) {
        log.warn("[SMS-SIMULE][provider={}] Destinataire={} | Message=\"{}\"", provider, destinataire, message);
    }
}
