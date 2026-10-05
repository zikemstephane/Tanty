package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.objectif.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Journal des révisions d'objectif. Écrit une fois, jamais modifié : c'est la
 * seule trace de ce qu'un objectif valait avant d'être abaissé.
 */
@Entity
@Table(name = "objectifs_revisions",
    indexes = @Index(name = "idx_revision_objectif", columnList = "objectif_commercial_id"))
@Getter @Setter @NoArgsConstructor
public class RevisionObjectifJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objectif_commercial_id", nullable = false)
    private ObjectifCommercialJpaEntity objectif;

    @Column(name = "objectif_global_avant_fcfa", nullable = false)
    private double objectifGlobalAvantFCFA;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "objectifs_revisions_gammes",
        joinColumns = @JoinColumn(name = "revision_id"))
    @MapKeyColumn(name = "gamme")
    @Column(name = "objectif_fcfa")
    private Map<String, Double> objectifsParGammeAvantFCFA = new HashMap<>();

    @Column(name = "objectif_global_apres_fcfa", nullable = false)
    private double objectifGlobalApresFCFA;

    @Column(name = "matricule_auteur", nullable = false, length = 20)
    private String matriculeAuteur;

    @Column(name = "motif", length = 500)
    private String motif;

    @Column(name = "date_revision", nullable = false, updatable = false)
    private LocalDateTime dateRevision;
}
