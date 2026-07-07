package com.NTFOODS.Api_tanty.modules.controle.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "controle_marges_produit")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MargeProduitJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private double prixVente;

    @Column(nullable = false)
    private double coutRevient;

    @Column(nullable = false)
    private int ventesMois;
}
