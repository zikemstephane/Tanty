package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.alerte.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "alertes_stock",
    uniqueConstraints = @UniqueConstraint(name = "uq_alerte_reference", columnNames = "reference"),
    indexes = {
        @Index(name = "idx_alerte_statut", columnList = "statut"),
        @Index(name = "idx_alerte_destinataire", columnList = "destinataire_role")
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlerteJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference", nullable = false, unique = true, length = 30)
    private String reference;

    @Column(name = "type", nullable = false, length = 30)
    private String type;

    @Column(name = "message", nullable = false, length = 500)
    private String message;

    @Column(name = "destinataire_role", nullable = false, length = 30)
    private String destinataireRole;

    @Column(name = "action_suggeree", length = 200)
    private String actionSuggeree;

    @Column(name = "code_produit", length = 50)
    private String codeProduit;

    @Column(name = "code_entrepot", length = 60)
    private String codeEntrepot;

    @Column(name = "reference_liee", length = 30)
    private String referenceLiee;

    @Column(name = "statut", nullable = false, length = 20)
    private String statut;

    @Column(name = "matricule_traitant", length = 20)
    private String matriculeTraitant;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_lecture")
    private LocalDateTime dateLecture;

    @Column(name = "date_traitement")
    private LocalDateTime dateTraitement;

    @Version
    private Long version;
}
