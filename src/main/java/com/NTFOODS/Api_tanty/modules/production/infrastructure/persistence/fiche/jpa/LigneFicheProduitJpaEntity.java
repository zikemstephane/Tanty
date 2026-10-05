package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.fiche.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lignes_fiche_produit")
@Getter @Setter @NoArgsConstructor
public class LigneFicheProduitJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_produit", nullable = false, length = 30)
    private String codeProduit;

    @Column(name = "nom_produit", nullable = false, length = 200)
    private String nomProduit;

    @Column(name = "quantite_produite", nullable = false)
    private double quantiteProduite;
}
