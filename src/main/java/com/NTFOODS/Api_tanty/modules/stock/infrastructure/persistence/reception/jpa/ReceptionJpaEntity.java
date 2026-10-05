package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.reception.jpa;

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
@Table(name = "receptions",
    uniqueConstraints = @UniqueConstraint(name = "uq_reception_numero", columnNames = "numero"),
    indexes = {
        @Index(name = "idx_reception_statut", columnList = "statut"),
        @Index(name = "idx_reception_bc", columnList = "numero_bon_commande")
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceptionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", nullable = false, unique = true, length = 30)
    private String numero;

    @Column(name = "source", nullable = false, length = 20)
    private String source;

    @Column(name = "categorie", nullable = false, length = 30)
    private String categorie;

    @Column(name = "code_fournisseur", length = 50)
    private String codeFournisseur;

    @Column(name = "numero_bon_commande", length = 30)
    private String numeroBonCommande;

    @Column(name = "numero_lot_production", length = 40)
    private String numeroLotProduction;

    @Column(name = "code_entrepot_cible", nullable = false, length = 60)
    private String codeEntrepotCible;

    @Column(name = "statut", nullable = false, length = 30)
    private String statut;

    @OneToMany(mappedBy = "reception", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<LigneReceptionJpaEntity> lignes = new ArrayList<>();

    @Column(name = "matricule_validateur1", length = 20)
    private String matriculeValidateur1;

    @Column(name = "matricule_validateur2", length = 20)
    private String matriculeValidateur2;

    @Column(name = "notes_validation1", length = 1000)
    private String notesValidation1;

    @Column(name = "notes_validation2", length = 1000)
    private String notesValidation2;

    @Column(name = "date_validation1")
    private LocalDateTime dateValidation1;

    @Column(name = "date_validation2")
    private LocalDateTime dateValidation2;

    @Column(name = "motif_rejet", length = 500)
    private String motifRejet;

    @Column(name = "seuil_ecart_pourcentage", nullable = false)
    private double seuilEcartPourcentage;

    @Column(name = "date_reception", nullable = false)
    private LocalDate dateReception;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Version
    private Long version;
}
