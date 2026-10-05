package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.jpa;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "besoins_emballage_jour")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BesoinEmballageJourJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repartition_jour_id", nullable = false)
    private RepartitionJourJpaEntity repartitionJour;

    @Column(name = "type_composant", nullable = false, length = 20)
    private String typeComposant;

    @Column(name = "quantite", nullable = false)
    private long quantite;
}
