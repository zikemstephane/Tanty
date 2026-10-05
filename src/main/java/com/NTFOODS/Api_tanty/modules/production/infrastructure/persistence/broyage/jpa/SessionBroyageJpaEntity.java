package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.broyage.jpa;

import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.StatutSessionBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.TypePoudre;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sessions_broyage",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_session_pph_date_poudre",
        columnNames = {"reference_pph", "date", "type_poudre"}))
@Getter @Setter @NoArgsConstructor
public class SessionBroyageJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference_pph", nullable = false, length = 30)
    private String referencePPH;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_poudre", nullable = false, length = 20)
    private TypePoudre typePoudre;

    @Column(name = "objectif_journalier_kg", nullable = false)
    private double objectifJournalierKg;

    @Column(name = "matricule_chef_machiniste", nullable = false, length = 20)
    private String matriculeChefMachiniste;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<ObjectifMachinisteJpaEntity> objectifsMachinistes = new HashSet<>();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<RealisationMachinisteJpaEntity> realisations = new HashSet<>();

    @Column(name = "quantite_nette_broyee_kg")
    private Double quantiteNetteBroyeeKg;

    @Column(name = "pertes_kg")
    private Double pertesKg;

    // Corrigé : Double (wrapper) au lieu de double (primitif).
    // La colonne SQL n'a pas de contrainte NOT NULL et contient déjà des
    // lignes à NULL en base ; un double primitif ne peut jamais recevoir
    // NULL, ce qui faisait planter Hibernate au chargement de ces lignes
    // avec un PropertyAccessException. Un Double gère nativement l'absence
    // de valeur (aucun dépassement autorisé renseigné pour cette session).
    @Column(name = "depassement_autorise_kg")
    private Double depassementAutoriseKg;

    @Column(name = "justificatif_non_validation", length = 1000)
    private String justificatifNonValidation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutSessionBroyage statut;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Version
    private Long version;
}