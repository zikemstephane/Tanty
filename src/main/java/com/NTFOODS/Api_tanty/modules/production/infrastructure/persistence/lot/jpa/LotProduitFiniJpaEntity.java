package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.lot.jpa;

import com.NTFOODS.Api_tanty.modules.production.domain.lot.enums.StatutLot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lots_produits_finis",
    indexes = @Index(name = "idx_lot_numero", columnList = "numero_lot", unique = true))
@Getter @Setter @NoArgsConstructor
public class LotProduitFiniJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_lot", unique = true, nullable = false, length = 30)
    private String numeroLot;

    @Column(name = "reference_pph", length = 30)
    private String referencePPH;

    @Column(name = "product_sku", nullable = false, length = 50)
    private String productSku;

    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;

    @Column(name = "product_unit", length = 50)
    private String productUnit;

    @Column(name = "declared_quantity_kg", nullable = false)
    private double declaredQuantityKg;

    @Column(name = "equivalent_units", nullable = false)
    private int equivalentUnits;

    @Column(name = "date_production", nullable = false)
    private LocalDate dateProduction;

    @Column(name = "dlc")
    private LocalDate dlc;

    @Column(name = "matricule_chef_production", nullable = false, length = 20)
    private String matriculeChefProduction;

    @Column(name = "declared_by", length = 100)
    private String declaredBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatutLot statut;

    @Column(name = "matricule_gestionnaire_validation", length = 20)
    private String matriculeGestionnaireValidation;

    @Column(name = "motif_rejet")
    private String motifRejet;

    @Column(name = "conditioning_type", length = 50)
    private String conditioningType;

    @Column(name = "conditioning_qty")
    private Integer conditioningQty;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Column(name = "validated_at")
    private LocalDateTime validatedAt;

    @Version
    private Long version;
}
