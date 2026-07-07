package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lignes_vente")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LigneVenteJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String codeProduit;

    @Column(nullable = false)
    private String designationProduit;

    @Column(nullable = false)
    private String conditionnement;

    @Column(nullable = false)
    private int quantite;

    @Column(nullable = false)
    private double prixUnitaire;

    @Column(nullable = false)
    private double remise;
}
