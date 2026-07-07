package com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp_codes", indexes = {
    @Index(name = "idx_otp_matricule", columnList = "matricule"),
    @Index(name = "idx_otp_expiration", columnList = "date_expiration")
})
@Getter @Setter @NoArgsConstructor
public class OtpCodeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String matricule;

    @Column(nullable = false, length = 10)
    private String codeHash;

    @Column(name = "telephone", length = 20)
    private String telephone;

    @Column(nullable = false)
    private boolean utilise;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_expiration", nullable = false)
    private LocalDateTime dateExpiration;

    @Column(name = "tentatives_verification", nullable = false)
    private int tentativesVerification;
}
