package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.inventaire.jpa;

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
@Table(name = "sessions_inventaire",
    uniqueConstraints = @UniqueConstraint(name = "uq_inventaire_numero", columnNames = "numero"),
    indexes = @Index(name = "idx_inventaire_statut", columnList = "statut"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionInventaireJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", nullable = false, unique = true, length = 30)
    private String numero;

    @Column(name = "type", nullable = false, length = 20)
    private String type;

    @Column(name = "code_entrepot", nullable = false, length = 60)
    private String codeEntrepot;

    @Column(name = "perimetre", length = 120)
    private String perimetre;

    @Column(name = "statut", nullable = false, length = 20)
    private String statut;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<LigneInventaireJpaEntity> lignes = new ArrayList<>();

    @Column(name = "matricule_initiateur", nullable = false, length = 20)
    private String matriculeInitiateur;

    @Column(name = "matricule_validateur_dg", length = 20)
    private String matriculeValidateurDG;

    @Column(name = "justification_ecarts", length = 1000)
    private String justificationEcarts;

    @Column(name = "date_inventaire", nullable = false)
    private LocalDate dateInventaire;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_validation")
    private LocalDateTime dateValidation;

    @Version
    private Long version;
}
