package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.dosage.jpa;

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
@Table(name = "sessions_dosage")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SessionDosageJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String referencePPH;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String matriculeAgentDoseur;

    @ElementCollection
    @CollectionTable(name = "dosage_matieres", joinColumns = @JoinColumn(name = "session_id"))
    private List<MatierePremiereJpa> matieresUtilisees;

    @ElementCollection
    @CollectionTable(name = "dosage_machines", joinColumns = @JoinColumn(name = "session_id"))
    @Column(name = "machine_id")
    private List<String> machinesMobilisees;

    @Column(nullable = false)
    private int nbFutsProduits;

    @Column(nullable = false)
    private int nbFutsCasses;

    private String justificationEcart;

    @Column(nullable = false)
    private boolean validee;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    @Version
    private Long version;

    @Embeddable
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class MatierePremiereJpa {
        private String typeMatiere;
        private double quantiteKg;
    }
}
