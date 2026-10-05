package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.catalogue.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité JPA — Produit du catalogue (table : produits).
 */
@Entity
@Table(name = "produits",
    indexes = {
        @Index(name = "idx_produit_marque", columnList = "marque"),
        @Index(name = "idx_produit_gamme", columnList = "gamme"),
        @Index(name = "idx_produit_categorie", columnList = "categorie")
    },
    uniqueConstraints = @UniqueConstraint(name = "uq_produit_code", columnNames = "code"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProduitJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, length = 50, unique = true)
    private String code;

    @Column(name = "marque", nullable = false, length = 100)
    private String marque;

    @Column(name = "gamme", nullable = false, length = 100)
    private String gamme;

    @Column(name = "variete", nullable = false, length = 100)
    private String variete;

    @Column(name = "designation", nullable = false, length = 255)
    private String designation;

    @Column(name = "categorie", nullable = false, length = 30)
    private String categorie;

    @Column(name = "categorie_metier", length = 30)
    private String categorieMetier;

    @Column(name = "actif", nullable = false)
    private boolean actif;

    @Column(name = "prix_distributeur")
    private Double prixDistributeur;

    @Column(name = "prix_grossiste")
    private Double prixGrossiste;

    @Column(name = "prix_detaillant")
    private Double prixDetaillant;

    @Column(name = "tva", nullable = false)
    private boolean tva;

    @Column(name = "conditionnement_commercial", length = 50)
    private String conditionnementCommercial;

    @Column(name = "unite_vente_commerciale", length = 50)
    private String uniteVenteCommerciale;

    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<ConditionnementJpaEntity> conditionnements = new ArrayList<>();

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}
