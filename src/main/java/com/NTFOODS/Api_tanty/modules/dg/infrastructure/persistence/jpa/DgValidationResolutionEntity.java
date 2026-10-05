package com.NTFOODS.Api_tanty.modules.dg.infrastructure.persistence.jpa;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Décision DG sur une validation agrégée. Permet au DG de marquer comme résolue
 * une validation affichée dans sa vue synthétique sans passer par les workflows métier.
 */
@Entity
@Table(name = "dg_validation_resolution")
public class DgValidationResolutionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String reference;

    @Column(nullable = false, length = 64)
    private String type;

    @Column(nullable = false)
    private boolean approbation;

    @Column(nullable = false, length = 32)
    private String matriculeDg;

    @Column(nullable = false)
    private LocalDateTime dateResolution;

    protected DgValidationResolutionEntity() {
    }

    public DgValidationResolutionEntity(String reference, String type, boolean approbation, String matriculeDg, LocalDateTime dateResolution) {
        this.reference = reference;
        this.type = type;
        this.approbation = approbation;
        this.matriculeDg = matriculeDg;
        this.dateResolution = dateResolution;
    }

    public Long getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public String getType() {
        return type;
    }

    public boolean isApprobation() {
        return approbation;
    }

    public String getMatriculeDg() {
        return matriculeDg;
    }

    public LocalDateTime getDateResolution() {
        return dateResolution;
    }
}
