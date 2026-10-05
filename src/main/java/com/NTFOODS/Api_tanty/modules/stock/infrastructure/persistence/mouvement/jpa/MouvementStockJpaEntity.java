package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.mouvement.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "mouvements_stock",
    indexes = {
        @Index(name = "idx_mvt_produit", columnList = "code_produit"),
        @Index(name = "idx_mvt_horodatage", columnList = "horodatage"),
        @Index(name = "idx_mvt_type", columnList = "type"),
        @Index(name = "idx_mvt_document", columnList = "reference_document")
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MouvementStockJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference", nullable = false, length = 30)
    private String reference;

    @Column(name = "type", nullable = false, length = 40)
    private String type;

    @Column(name = "code_produit", nullable = false, length = 50)
    private String codeProduit;

    @Column(name = "sku", length = 60)
    private String sku;

    @Column(name = "quantite_en_base", nullable = false)
    private long quantiteEnBase;

    @Column(name = "code_entrepot_source", length = 60)
    private String codeEntrepotSource;

    @Column(name = "code_entrepot_cible", length = 60)
    private String codeEntrepotCible;

    @Column(name = "matricule_acteur", nullable = false, length = 20)
    private String matriculeActeur;

    @Column(name = "reference_document", length = 30)
    private String referenceDocument;

    @Column(name = "motif", length = 500)
    private String motif;

    @Column(name = "horodatage", nullable = false)
    private LocalDateTime horodatage;
}
