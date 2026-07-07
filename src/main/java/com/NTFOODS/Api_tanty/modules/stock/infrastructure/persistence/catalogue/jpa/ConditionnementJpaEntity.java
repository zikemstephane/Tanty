package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.catalogue.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entité JPA — Conditionnement d'un produit (table : conditionnements).
 */
@Entity
@Table(name = "conditionnements",
    indexes = {
        @Index(name = "idx_cond_sku", columnList = "sku"),
        @Index(name = "idx_cond_code_barres", columnList = "code_barres")
    })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ConditionnementJpaEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id", nullable = false)
    private ProduitJpaEntity produit;

    @Column(name = "code_produit", nullable = false, length = 50)
    private String codeProduit;

    @Column(name = "sku", nullable = false, length = 60)
    private String sku;

    @Column(name = "niveau", nullable = false, length = 20)
    private String niveau;

    @Column(name = "libelle", nullable = false, length = 120)
    private String libelle;

    @Column(name = "facteur_conversion_vers_base", nullable = false)
    private long facteurConversionVersBase;

    @Column(name = "nombre_unites", nullable = false)
    private int nombreUnites;

    @Column(name = "unite_conditionnement", nullable = false, length = 30)
    private String uniteConditionnement;

    @Column(name = "poids_unitaire_grammes", nullable = false)
    private double poidsUnitaireGrammes;

    @Column(name = "volume_unitaire_cm3", nullable = false)
    private double volumeUnitaireCm3;

    @Column(name = "prix_unitaire_ht", precision = 15, scale = 2)
    private BigDecimal prixUnitaireHT;

    @Column(name = "code_barres", length = 60)
    private String codeBarres;
}
