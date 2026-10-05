package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.feuillroute.jpa;

import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.StatutArret;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "arrets_feuill_route")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ArretFeuillRouteJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String codeClient;

    @Column(nullable = false)
    private int ordre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutArret statut;

    private String numeroVente;

    @Column(length = 10)
    private String codeInfoClient;

    private Integer quantiteInfo;

    @Column(columnDefinition = "TEXT")
    private String observationsArret;

    @Column(nullable = false, updatable = false)
    private LocalDateTime heurePlanifiee;

    private LocalDateTime heureEffective;
}
