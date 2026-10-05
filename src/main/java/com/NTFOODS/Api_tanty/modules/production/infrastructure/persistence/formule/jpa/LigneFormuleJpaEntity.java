package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.formule.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "lignes_formule")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LigneFormuleJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formule_id", nullable = false)
    private FormuleProductionJpaEntity formule;

    @Column(name = "matiere_premiere", nullable = false, length = 100)
    private String matierePremiere;

    @Column(name = "proportion_kg", nullable = false, precision = 12, scale = 3)
    private BigDecimal proportionKg;
}
