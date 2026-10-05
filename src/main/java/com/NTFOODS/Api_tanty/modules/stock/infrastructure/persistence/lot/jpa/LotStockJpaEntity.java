package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.lot.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lots_stock",
    uniqueConstraints = @UniqueConstraint(name = "uq_lot_numero", columnNames = "numero_lot"),
    indexes = {
        @Index(name = "idx_lot_produit", columnList = "code_produit"),
        @Index(name = "idx_lot_sku", columnList = "sku"),
        @Index(name = "idx_lot_entrepot", columnList = "code_entrepot"),
        @Index(name = "idx_lot_peremption", columnList = "date_peremption"),
        @Index(name = "idx_lot_statut", columnList = "statut")
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LotStockJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_lot", nullable = false, unique = true, length = 40)
    private String numeroLot;

    @Column(name = "code_produit", nullable = false, length = 50)
    private String codeProduit;

    @Column(name = "sku", length = 60)
    private String sku;

    @Column(name = "designation", length = 200)
    private String designation;

    @Column(name = "code_fournisseur", length = 50)
    private String codeFournisseur;

    @Column(name = "code_entrepot", nullable = false, length = 60)
    private String codeEntrepot;

    @Column(name = "date_fabrication")
    private LocalDate dateFabrication;

    @Column(name = "date_peremption")
    private LocalDate datePeremption;

    @Column(name = "quantite_initiale", nullable = false)
    private long quantiteInitiale;

    @Column(name = "quantite_restante", nullable = false)
    private long quantiteRestante;

    @Column(name = "statut", nullable = false, length = 20)
    private String statut;

    @Column(name = "observations", length = 1000)
    private String observations;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_dernier_mouvement")
    private LocalDateTime dateDernierMouvement;

    @Version
    private Long version;
}
