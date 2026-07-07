package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.prospect.jpa;

import com.NTFOODS.Api_tanty.modules.commercial.domain.prospect.enums.CanalProspection;
import com.NTFOODS.Api_tanty.modules.commercial.domain.prospect.enums.StatutProspect;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "prospects")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProspectJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codeProspect;

    @Column(nullable = false)
    private String nom;

    private String telephone;
    private String adresse;
    private String localite;
    private String matriculeCommercial;

    @Enumerated(EnumType.STRING)
    private CanalProspection canal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutProspect statut;

    @Column(columnDefinition = "TEXT")
    private String observations;

    private String codeClientConverti;

    @Column(nullable = false)
    private LocalDate dateProspection;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    @Version
    private Long version;
}
