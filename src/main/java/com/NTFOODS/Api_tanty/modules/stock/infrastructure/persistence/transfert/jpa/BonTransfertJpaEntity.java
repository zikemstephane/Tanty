package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.transfert.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bons_transfert",
    uniqueConstraints = @UniqueConstraint(name = "uq_transfert_numero", columnNames = "numero"),
    indexes = @Index(name = "idx_transfert_statut", columnList = "statut"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BonTransfertJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", nullable = false, unique = true, length = 30)
    private String numero;

    @Column(name = "code_entrepot_source", nullable = false, length = 60)
    private String codeEntrepotSource;

    @Column(name = "code_entrepot_cible", nullable = false, length = 60)
    private String codeEntrepotCible;

    @Column(name = "statut", nullable = false, length = 20)
    private String statut;

    @Column(name = "matricule_gestionnaire", nullable = false, length = 20)
    private String matriculeGestionnaire;

    @Column(name = "declenche_par_seuil", nullable = false)
    private boolean declencheParSeuil;

    @OneToMany(mappedBy = "bonTransfert", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<LigneTransfertJpaEntity> lignes = new ArrayList<>();

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_execution")
    private LocalDateTime dateExecution;

    @Version
    private Long version;
}
