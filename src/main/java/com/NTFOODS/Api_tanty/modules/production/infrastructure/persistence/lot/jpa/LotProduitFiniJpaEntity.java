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

    @Column(name = "reference_pph", nullable = false, length = 30)
    private String referencePPH;

    @Column(name = "code_produit", nullable = false, length = 30)
    private String codeProduit;

    @Column(name = "designation_produit", nullable = false, length = 200)
    private String designationProduit;

    @Column(name = "quantite_kg", nullable = false)
    private double quantiteKg;

    @Column(name = "nb_cartons", nullable = false)
    private int nbCartons;

    @Column(name = "date_production", nullable = false)
    private LocalDate dateProduction;

    @Column(nullable = false)
    private LocalDate dlc;

    @Column(name = "matricule_chef_production", nullable = false, length = 20)
    private String matriculeChefProduction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatutLot statut;

    @Column(name = "matricule_gestionnaire_validation", length = 20)
    private String matriculeGestionnaireValidation;

    @Column(name = "motif_rejet")
    private String motifRejet;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Version
    private Long version;
}
