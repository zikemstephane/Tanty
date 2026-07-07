package com.NTFOODS.Api_tanty.shared.infrastructure.otp;

/**
 * Port sortant — passerelle d'envoi de SMS pour les codes OTP.
 *
 * Implémentation par défaut : {@link LogSmsGatewayAdapter} (journalise le code
 * au lieu de l'envoyer réellement — pratique pour le développement local sans
 * dépendre d'un opérateur télécom). En production, fournir un bean
 * {@code @Primary} implémentant cette interface avec un fournisseur SMS réel
 * (Orange CM, MTN CM, Twilio...) configuré via {@code app.sms.provider}.
 */
public interface SmsGatewayPort {
    void envoyer(String destinataire, String message);
}
