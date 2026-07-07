package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.recouvrement.jpa;

import com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.enums.StatutRecouvrement;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "recouvrements")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RecouvrementJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String referenceRecouvrement;

    @Column(nullable = false)
    private String codeClient;

    @Column(nullable = false)
    private String nomClient;

    @Column(nullable = false)
    private double montantDu;

    @Column(nullable = false)
    private double montantRembourse;

    @Column(nullable = false)
    private LocalDate dateEcheance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutRecouvrement statut;

    @Column(nullable = false)
    private String matriculeCommercial;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    @Version
    private Long version;
}
