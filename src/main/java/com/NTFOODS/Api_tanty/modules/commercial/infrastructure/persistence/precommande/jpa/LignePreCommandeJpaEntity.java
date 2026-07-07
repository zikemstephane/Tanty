package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.precommande.jpa;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lignes_precommande")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LignePreCommandeJpaEntity {

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
    private int quantiteDemandee;

    @Column(nullable = false)
    private int quantiteValidee;
}
