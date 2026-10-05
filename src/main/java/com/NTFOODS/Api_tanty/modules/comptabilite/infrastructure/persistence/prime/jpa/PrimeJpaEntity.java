package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.prime.jpa;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.enums.StatutPrime;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "primes_commerciales")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PrimeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String referencePrime;

    @Column(nullable = false)
    private String matriculeCommercial;

    @Column(nullable = false)
    private LocalDate semaineDebut;

    @Column(nullable = false)
    private LocalDate semaineFin;

    @Column(nullable = false)
    private double objectifGlobalFCFA;

    @Column(nullable = false)
    private double totalVentesGlobalFCFA;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "prime_objectifs_par_gamme", joinColumns = @JoinColumn(name = "prime_id"))
    @MapKeyColumn(name = "gamme")
    @Column(name = "objectif_fcfa")
    private Map<String, Double> objectifsParGammeFCFA = new HashMap<>();

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "prime_realisations_par_gamme", joinColumns = @JoinColumn(name = "prime_id"))
    @MapKeyColumn(name = "gamme")
    @Column(name = "realise_fcfa")
    private Map<String, Double> realisationsParGammeFCFA = new HashMap<>();

    @Column(nullable = false)
    private double montantPrimeFCFA;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPrime statut;

    private String matriculeValidateur;

    /**
     * Barème ayant servi au calcul. Nul pour les primes antérieures à la mise en
     * place du paramétrage, calculées avec les valeurs alors compilées.
     */
    @Column(length = 60)
    private String referenceBareme;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    @Version
    private Long version;
}
