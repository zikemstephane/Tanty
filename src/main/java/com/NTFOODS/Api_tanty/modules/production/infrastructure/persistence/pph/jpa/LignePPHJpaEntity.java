package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.jpa;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lignes_pph")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LignePPHJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String codeProduit;

    @Column(nullable = false)
    private String nomProduit;

    @Column(nullable = false)
    private int objectifSemaine;

    @Column(nullable = false)
    private int productionRealisee;
}
