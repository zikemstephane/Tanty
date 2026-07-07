package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.commandedistance.jpa;

import com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.enums.CanalCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.enums.StatutCommandeDistance;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "commandes_distance")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CommandeDistanceJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroCommande;

    @Column(nullable = false)
    private String codeClient;

    @Column(nullable = false)
    private String matriculeCommercial;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CanalCommande canal;

    @Column(nullable = false)
    private LocalDate dateCommande;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "commande_distance_id")
    private List<LigneCommandeDistanceJpaEntity> lignes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutCommandeDistance statut;

    private String matriculeValidateurSecretaire;
    private String matriculeValidateurComptable;

    @Column(columnDefinition = "TEXT")
    private String motifAnnulation;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    @Version
    private Long version;
}
