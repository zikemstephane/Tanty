package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.precommande.jpa;

import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.enums.StatutPreCommande;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "precommandes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PreCommandeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroPreCommande;

    @Column(nullable = false)
    private String matriculeCommercial;

    @Column(nullable = false)
    private LocalDate dateSoumission;

    @Column(nullable = false)
    private LocalDate dateEffet;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "precommande_id")
    private List<LignePreCommandeJpaEntity> lignes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPreCommande statut;

    private String matriculeValidateurSecretaire;
    private String matriculeValidateurComptable;
    private String motifRefus;

    @Column(nullable = false)
    private boolean soumiseEnSoiree;

    // Traçabilité de l'arbitrage des quantités par le gestionnaire de stock.
    private String matriculeArbitre;
    private LocalDateTime dateArbitrage;

    @Column(length = 500)
    private String motifArbitrage;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    @Version
    private Long version;
}
