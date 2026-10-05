package com.NTFOODS.Api_tanty.modules.secretaire.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "secretaire_micro_stock")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MicroStockJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private int qte;

    @Column(nullable = false)
    private double prixUnite;

    @Column(nullable = false)
    private int seuil;
}
