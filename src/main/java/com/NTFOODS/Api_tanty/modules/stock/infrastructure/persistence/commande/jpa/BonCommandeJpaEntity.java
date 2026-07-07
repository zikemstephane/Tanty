package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.commande.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bons_commande",
    uniqueConstraints = @UniqueConstraint(name = "uq_bc_numero", columnNames = "numero"),
    indexes = {
        @Index(name = "idx_bc_statut", columnList = "statut"),
        @Index(name = "idx_bc_fournisseur", columnList = "code_fournisseur")
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BonCommandeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", nullable = false, unique = true, length = 30)
    private String numero;

    @Column(name = "code_fournisseur", nullable = false, length = 50)
    private String codeFournisseur;

    @Column(name = "statut", nullable = false, length = 20)
    private String statut;

    @Column(name = "date_commande", nullable = false)
    private LocalDate dateCommande;

    @Column(name = "date_livraison_prevue")
    private LocalDate dateLivraisonPrevue;

    @Column(name = "matricule_createur", nullable = false, length = 20)
    private String matriculeCreateur;

    @OneToMany(mappedBy = "bonCommande", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<LigneBonCommandeJpaEntity> lignes = new ArrayList<>();

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Version
    private Long version;
}
