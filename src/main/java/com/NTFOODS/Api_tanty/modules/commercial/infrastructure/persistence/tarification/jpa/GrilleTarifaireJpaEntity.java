package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.tarification.jpa;

import com.NTFOODS.Api_tanty.modules.commercial.domain.client.enums.TypeClient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "grilles_tarifaires",
    uniqueConstraints = @UniqueConstraint(name = "uq_grille_produit_segment", columnNames = {"code_produit", "segment"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GrilleTarifaireJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_produit", nullable = false, length = 50)
    private String codeProduit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TypeClient segment;

    @Column(name = "prix_homologue_fcfa", nullable = false)
    private double prixHomologueFCFA;

    @Column(nullable = false)
    private boolean actif;

    @Column(name = "matricule_auteur", nullable = false, length = 20)
    private String matriculeAuteur;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Version
    private Long version;
}
