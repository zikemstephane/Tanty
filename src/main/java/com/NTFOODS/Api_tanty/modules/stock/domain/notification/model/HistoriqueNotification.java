package com.NTFOODS.Api_tanty.modules.stock.domain.notification.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entité — Historique des notifications envoyées (in-app, email, SMS).
 * Sert de piste d'audit pour les notifications diffusées via WebSocket, email ou SMS.
 */
public class HistoriqueNotification {

    private Long id;
    private final String typeNotification;
    private final String titre;
    private final String message;
    private final String destinataireRole;
    private final String matriculeDestinataire;
    private final String canal;
    private final String priorite;
    private final String referenceLiee;
    private boolean lu;
    private LocalDateTime dateCreation;
    private LocalDateTime dateLecture;
    private Long version;

    public HistoriqueNotification(String typeNotification, String titre, String message,
                                  String destinataireRole, String matriculeDestinataire,
                                  String canal, String priorite, String referenceLiee) {
        this.typeNotification = Objects.requireNonNull(typeNotification);
        this.titre = Objects.requireNonNull(titre);
        this.message = Objects.requireNonNull(message);
        this.destinataireRole = destinataireRole;
        this.matriculeDestinataire = matriculeDestinataire;
        this.canal = Objects.requireNonNull(canal);
        this.priorite = priorite != null ? priorite : "MOYENNE";
        this.referenceLiee = referenceLiee;
        this.lu = false;
        this.dateCreation = LocalDateTime.now();
        this.version = 0L;
    }

    public static HistoriqueNotification reconstituer(Long id, String typeNotification, String titre,
            String message, String destinataireRole, String matriculeDestinataire,
            String canal, String priorite, String referenceLiee, boolean lu,
            LocalDateTime dateCreation, LocalDateTime dateLecture, Long version) {
        HistoriqueNotification h = new HistoriqueNotification(
            typeNotification, titre, message, destinataireRole, matriculeDestinataire,
            canal, priorite, referenceLiee);
        h.id = id;
        h.lu = lu;
        h.dateCreation = dateCreation;
        h.dateLecture = dateLecture;
        h.version = version;
        return h;
    }

    public void marquerCommeLu() {
        if (!this.lu) {
            this.lu = true;
            this.dateLecture = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public String getTypeNotification() { return typeNotification; }
    public String getTitre() { return titre; }
    public String getMessage() { return message; }
    public String getDestinataireRole() { return destinataireRole; }
    public String getMatriculeDestinataire() { return matriculeDestinataire; }
    public String getCanal() { return canal; }
    public String getPriorite() { return priorite; }
    public String getReferenceLiee() { return referenceLiee; }
    public boolean isLu() { return lu; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateLecture() { return dateLecture; }
    public Long getVersion() { return version; }
}
