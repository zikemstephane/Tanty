package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.terminal.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "terminaux")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TerminalJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 15)
    private String imei;

    @Column(nullable = false)
    private String modele;

    @Column
    private String matriculeCommercial;

    @Column(nullable = false)
    private double prixAchat;

    @Column(nullable = false)
    private double resteAPayer;

    @Column(nullable = false)
    private boolean actif;

    private LocalDateTime dateDerniereConnexion;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    @Version
    private Long version;
}
