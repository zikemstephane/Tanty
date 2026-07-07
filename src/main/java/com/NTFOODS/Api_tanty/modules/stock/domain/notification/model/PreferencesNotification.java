package com.NTFOODS.Api_tanty.modules.stock.domain.notification.model;

import java.time.LocalTime;
import java.util.Objects;
import java.util.Set;

/**
 * Agrégat — Préférences de notification par utilisateur.
 *
 * Permet à chaque utilisateur de choisir ses canaux (in-app, email, SMS),
 * de filtrer par priorité minimale, et de définir une plage horaire de
 * réception pour les notifications non critiques.
 */
public class PreferencesNotification {

    private Long id;
    private final String matriculeUtilisateur;
    private boolean notificationInApp;
    private boolean notificationEmail;
    private boolean notificationSms;
    private boolean notificationPush;
    private String prioriteMinimale;
    private LocalTime heureDebutReception;
    private LocalTime heureFinReception;
    private Set<String> typesFiltres;
    private Long version;

    public PreferencesNotification(String matriculeUtilisateur) {
        this.matriculeUtilisateur = Objects.requireNonNull(matriculeUtilisateur);
        this.notificationInApp = true;
        this.notificationEmail = true;
        this.notificationSms = false;
        this.notificationPush = false;
        this.prioriteMinimale = "BASSE";
        this.heureDebutReception = LocalTime.of(6, 0);
        this.heureFinReception = LocalTime.of(22, 0);
        this.version = 0L;
    }

    public static PreferencesNotification reconstituer(Long id, String matriculeUtilisateur,
            boolean inApp, boolean email, boolean sms, boolean push,
            String prioriteMinimale, LocalTime heureDebut, LocalTime heureFin,
            Set<String> typesFiltres, Long version) {
        PreferencesNotification p = new PreferencesNotification(matriculeUtilisateur);
        p.id = id;
        p.notificationInApp = inApp;
        p.notificationEmail = email;
        p.notificationSms = sms;
        p.notificationPush = push;
        p.prioriteMinimale = prioriteMinimale;
        p.heureDebutReception = heureDebut;
        p.heureFinReception = heureFin;
        p.typesFiltres = typesFiltres;
        p.version = version;
        return p;
    }

    public void mettreAJour(boolean inApp, boolean email, boolean sms, boolean push,
            String prioriteMin, LocalTime heureDebut, LocalTime heureFin,
            Set<String> typesFiltres) {
        this.notificationInApp = inApp;
        this.notificationEmail = email;
        this.notificationSms = sms;
        this.notificationPush = push;
        this.prioriteMinimale = prioriteMin;
        this.heureDebutReception = heureDebut;
        this.heureFinReception = heureFin;
        this.typesFiltres = typesFiltres;
    }

    public boolean accepteCanal(String canal) {
        return switch (canal) {
            case "IN_APP" -> notificationInApp;
            case "EMAIL" -> notificationEmail;
            case "SMS" -> notificationSms;
            case "PUSH" -> notificationPush;
            default -> false;
        };
    }

    public boolean acceptePriorite(String priorite) {
        int ordrePriorite = ordre(priorite);
        int ordreMin = ordre(prioriteMinimale);
        return ordrePriorite >= ordreMin;
    }

    private int ordre(String p) {
        return switch (p) {
            case "CRITIQUE" -> 4;
            case "HAUTE" -> 3;
            case "MOYENNE" -> 2;
            case "BASSE" -> 1;
            default -> 0;
        };
    }

    public Long getId() { return id; }
    public String getMatriculeUtilisateur() { return matriculeUtilisateur; }
    public boolean isNotificationInApp() { return notificationInApp; }
    public boolean isNotificationEmail() { return notificationEmail; }
    public boolean isNotificationSms() { return notificationSms; }
    public boolean isNotificationPush() { return notificationPush; }
    public String getPrioriteMinimale() { return prioriteMinimale; }
    public LocalTime getHeureDebutReception() { return heureDebutReception; }
    public LocalTime getHeureFinReception() { return heureFinReception; }
    public Set<String> getTypesFiltres() { return typesFiltres; }
    public Long getVersion() { return version; }
}
