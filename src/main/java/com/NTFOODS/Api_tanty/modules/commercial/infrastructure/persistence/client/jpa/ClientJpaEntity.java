package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.client.jpa;

import com.NTFOODS.Api_tanty.modules.commercial.domain.client.enums.TypeClient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "clients")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClientJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codeClient;

    @Column(nullable = false)
    private String nom;

    private String telephone;
    private String adresse;
    private String localite;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeClient type;

    private String matriculeCommercialReferent;

    @Column(nullable = false)
    private boolean actif;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    @Version
    private Long version;
}
