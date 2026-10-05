package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.notification.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "historique_notifications",
    indexes = {
        @Index(name = "idx_histo_matricule", columnList = "matricule_destinataire"),
        @Index(name = "idx_histo_role", columnList = "destinataire_role"),
        @Index(name = "idx_histo_lu", columnList = "lu")
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoriqueNotificationJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_notification", nullable = false, length = 50)
    private String typeNotification;

    @Column(name = "titre", nullable = false, length = 200)
    private String titre;

    @Column(name = "message", nullable = false, length = 1000)
    private String message;

    @Column(name = "destinataire_role", length = 30)
    private String destinataireRole;

    @Column(name = "matricule_destinataire", length = 20)
    private String matriculeDestinataire;

    @Column(name = "canal", nullable = false, length = 10)
    private String canal;

    @Column(name = "priorite", nullable = false, length = 10)
    private String priorite;

    @Column(name = "reference_liee", length = 30)
    private String referenceLiee;

    @Column(name = "lu", nullable = false)
    private boolean lu;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_lecture")
    private LocalDateTime dateLecture;

    @Version
    private Long version;
}
