package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.objectif.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "objectifs_commerciaux",
    uniqueConstraints = @UniqueConstraint(name = "uq_objectif_commercial_semaine",
        columnNames = {"matricule_commercial", "semaine_debut"}))
@Getter @Setter @NoArgsConstructor
public class ObjectifCommercialJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "matricule_commercial", nullable = false, length = 20)
    private String matriculeCommercial;

    @Column(name = "semaine_debut", nullable = false)
    private LocalDate semaineDebut;

    @Column(name = "semaine_fin", nullable = false)
    private LocalDate semaineFin;

    @Column(name = "objectif_global_fcfa", nullable = false)
    private double objectifGlobalFCFA;

    @ElementCollection
    @CollectionTable(name = "objectifs_par_gamme",
        joinColumns = @JoinColumn(name = "objectif_commercial_id"))
    @MapKeyColumn(name = "gamme")
    @Column(name = "objectif_fcfa")
    private Map<String, Double> objectifsParGammeFCFA = new HashMap<>();

    @Column(name = "matricule_auteur", nullable = false, length = 20)
    private String matriculeAuteur;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Version
    private Long version;
}
