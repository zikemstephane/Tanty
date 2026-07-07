package com.NTFOODS.Api_tanty.shared.infrastructure.audit;

import com.NTFOODS.Api_tanty.shared.kernel.audit.TypeActionAudit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entité de persistance — Journal d'Audit.
 *
 * Table volontairement APPEND-ONLY : aucune mise à jour ni suppression n'est
 * exposée par le repository. C'est la preuve numérique immuable exigée par
 * le Pilier 1 (Immuabilité) pour tous les modules de l'ERP.
 */
@Entity
@Table(name = "journal_audit", indexes = {
    @Index(name = "idx_audit_matricule", columnList = "matricule"),
    @Index(name = "idx_audit_horodatage", columnList = "horodatage"),
    @Index(name = "idx_audit_type_action", columnList = "typeAction"),
    @Index(name = "idx_audit_ressource", columnList = "ressource")
})
@Getter @Setter @NoArgsConstructor
public class JournalAuditJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String matricule;

    @Column(length = 40)
    private String role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TypeActionAudit typeAction;

    /** Module concerné (ex: "stock", "comptabilite", "commercial"...). */
    @Column(length = 40)
    private String module;

    /** Ressource / entité métier concernée (ex: "Facture", "Reception"...). */
    @Column(length = 80)
    private String ressource;

    /** Référence métier de l'objet concerné si connue (ex: numéro de facture). */
    @Column(length = 100)
    private String referenceEntite;

    @Column(length = 10)
    private String methodeHttp;

    @Column(length = 300)
    private String endpoint;

    @Column(length = 500)
    private String details;

    @Column(nullable = false)
    private boolean succes;

    @Column(length = 500)
    private String messageErreur;

    @Column(length = 45)
    private String adresseIp;

    @Column(nullable = false)
    private long dureeMs;

    @Column(nullable = false, updatable = false)
    private LocalDateTime horodatage;
}
