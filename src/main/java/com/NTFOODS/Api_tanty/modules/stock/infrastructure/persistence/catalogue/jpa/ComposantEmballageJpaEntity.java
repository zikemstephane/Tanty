package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.catalogue.jpa;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "composants_emballage")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ComposantEmballageJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conditionnement_id", nullable = false)
    private ConditionnementJpaEntity conditionnement;

    @Column(name = "type_composant", nullable = false, length = 20)
    private String typeComposant;

    @Column(name = "quantite_composant", nullable = false)
    private int quantiteComposant;

    @Column(name = "quantite_unites_produites", nullable = false)
    private int quantiteUnitesProduites;
}
