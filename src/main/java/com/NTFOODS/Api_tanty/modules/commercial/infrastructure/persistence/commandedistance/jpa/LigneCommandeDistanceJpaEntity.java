package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.commandedistance.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lignes_commande_distance")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LigneCommandeDistanceJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String codeProduit;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private String conditionnement;

    @Column(nullable = false)
    private int quantite;

    @Column(nullable = false)
    private double prixUnitaire;
}
