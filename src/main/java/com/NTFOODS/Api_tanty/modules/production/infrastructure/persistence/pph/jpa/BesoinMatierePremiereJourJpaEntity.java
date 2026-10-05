package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "besoins_matiere_premiere_jour")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BesoinMatierePremiereJourJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repartition_jour_id", nullable = false)
    private RepartitionJourJpaEntity repartitionJour;

    @Column(name = "matiere_premiere", nullable = false, length = 100)
    private String matierePremiere;

    @Column(name = "quantite_kg", nullable = false, precision = 12, scale = 2)
    private BigDecimal quantiteKg;
}
