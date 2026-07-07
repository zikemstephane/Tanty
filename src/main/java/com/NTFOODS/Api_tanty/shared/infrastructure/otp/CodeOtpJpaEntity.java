package com.NTFOODS.Api_tanty.shared.infrastructure.otp;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "codes_otp", indexes = {
    @Index(name = "idx_otp_matricule_operation", columnList = "matricule, operationCible")
})
@Getter @Setter @NoArgsConstructor
public class CodeOtpJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String matricule;

    @Column(nullable = false, length = 6)
    private String code;

    /** Identifie l'opération protégée (ex: "VERSEMENT:VRS-2026-0012", "PRIX_EXCEPTIONNEL:VTE-2026-0088"). */
    @Column(nullable = false, length = 150)
    private String operationCible;

    @Column(nullable = false)
    private boolean utilise = false;

    @Column(nullable = false)
    private int tentativesVerification = 0;

    @Column(nullable = false)
    private LocalDateTime dateGeneration;

    @Column(nullable = false)
    private LocalDateTime dateExpiration;
}
