package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.notification.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "preferences_notification",
    uniqueConstraints = @UniqueConstraint(name = "uq_prefs_matricule", columnNames = "matricule_utilisateur"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreferencesNotificationJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "matricule_utilisateur", nullable = false, unique = true, length = 20)
    private String matriculeUtilisateur;

    @Column(name = "notification_in_app", nullable = false)
    private boolean notificationInApp;

    @Column(name = "notification_email", nullable = false)
    private boolean notificationEmail;

    @Column(name = "notification_sms", nullable = false)
    private boolean notificationSms;

    @Column(name = "notification_push", nullable = false)
    private boolean notificationPush;

    @Column(name = "priorite_minimale", nullable = false, length = 10)
    private String prioriteMinimale;

    @Column(name = "heure_debut_reception", nullable = false)
    private LocalTime heureDebutReception;

    @Column(name = "heure_fin_reception", nullable = false)
    private LocalTime heureFinReception;

    @Column(name = "types_filtres", length = 500)
    private String typesFiltres;

    @Version
    private Long version;
}
