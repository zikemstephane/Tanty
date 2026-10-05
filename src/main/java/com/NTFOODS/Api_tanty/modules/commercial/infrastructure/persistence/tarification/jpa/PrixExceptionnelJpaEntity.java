package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.tarification.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "prix_exceptionnels")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PrixExceptionnelJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String reference;

    @Column(name = "code_produit", nullable = false, length = 50)
    private String codeProduit;

    @Column(name = "code_client", nullable = false, length = 50)
    private String codeClient;

    @Column(name = "prix_homologue_fcfa", nullable = false)
    private double prixHomologueFCFA;

    @Column(name = "prix_applique_fcfa", nullable = false)
    private double prixAppliqueFCFA;

    @Column(nullable = false, length = 300)
    private String motif;

    @Column(name = "matricule_autorisateur", nullable = false, length = 20)
    private String matriculeAutorisateur;

    @Column(name = "reference_vente_cible", length = 40)
    private String referenceVenteCible;

    @Column(name = "date_autorisation", nullable = false)
    private LocalDateTime dateAutorisation;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Version
    private Long version;
}
