package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.facture.jpa;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lignes_facture")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LigneFactureJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String referenceArticle;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private int quantite;

    @Column(nullable = false)
    private double prixUnitaireHT;

    @Column(nullable = false)
    private double tauxTVA;

    @Column(nullable = false)
    private double remise;
}
